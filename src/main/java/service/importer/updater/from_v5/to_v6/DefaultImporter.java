package service.importer.updater.from_v5.to_v6;

import model.dto.*;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.person.PersonRepository;
import repository.repos.sensor.SensorRepository;
import service.importer.ImportOption;
import service.importer.Importer;
import service.importer.JsonParser;
import service.importer.Transformer;
import service.importer.model.Model;
import service.importer.model.ModelHolder;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static service.importer.model.ModelField.*;

public class DefaultImporter implements Importer {
    private final ImportOption option;
    private final RepositoryFactory repositoryFactory;
    private final JsonParser jsonParser = JsonParser_v5.getInstance();
    private final Transformer transformer = Transformer_v6.getInstance();

    public DefaultImporter(@Nonnull ImportOption option, @Nonnull RepositoryFactory repositoryFactory) {
        this.option = option;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public boolean importing(@Nonnull List<ModelHolder> in) {
        if (!importMeasurements(in, repositoryFactory)) return false;
        if (!importChannels(in, repositoryFactory)) return false;
        if (!importSensors(in, repositoryFactory)) return false;
        if (!importCalibrators(in, repositoryFactory)) return false;
        if (!importControlPoints(in, repositoryFactory)) return false;
        if (!importMeasurementTransformFactors(in, repositoryFactory)) return false;

        return importPersons(in, repositoryFactory);
    }

    /**
     * Import measurements ignores ImportOption to avoid channel, sensor, transform factors and calibrators incompatibilities.
     * ImportOption is always = {@link ImportOption#REPLACE_EXISTED}
     * @param in input of {@link ModelHolder} of Measurements
     * @param repositoryFactory to save importing measurements
     * @return true if everything passed without errors. Otherwise, returns false;
     * @see Channel#measurementName
     * @see Channel#measurementValue
     * @see Sensor#measurementName
     * @see Sensor#measurementValue
     * @see MeasurementTransformFactor#transformFrom
     * @see MeasurementTransformFactor#transformTo
     * @see Calibrator#measurementName
     * @see Calibrator#measurementValue
     * @see ImportOption
     */
    private boolean importMeasurements(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.MEASUREMENT).collect(Collectors.toList());
        if (input.size() > 0) {
            MeasurementRepository repository = repositoryFactory.getImplementation(MeasurementRepository.class);
            List<Measurement> oldMeasurements = new ArrayList<>(repository.getAll());

            for (ModelHolder modelHolder : input) {
                String name = modelHolder.getValue(MEASUREMENT_NAME);
                String value = modelHolder.getValue(MEASUREMENT_VALUE);
                if (name.isEmpty() || value.isEmpty()) continue;

                Measurement newMeasurement = new Measurement(name, value);

                int oldMeasurementIndex = oldMeasurements.indexOf(newMeasurement);
                if (oldMeasurementIndex >= 0) {
                    oldMeasurements.set(oldMeasurementIndex, newMeasurement);
                } else {
                    oldMeasurements.add(newMeasurement);
                }
            }
            return repository.rewrite(oldMeasurements);
        }
        return true;
    }

    private boolean importChannels(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.CHANNEL).collect(Collectors.toList());

        if (input.size() > 0) {
            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
            List<ModelHolder> oldMeasurements = in.stream().filter(e -> e.getModel() == Model.MEASUREMENT).collect(Collectors.toList());

            List<Channel> oldChannels = new ArrayList<>(channelRepository.getAll());
            for (ModelHolder modelHolder : input) {
                String measurementValue = modelHolder.getValue(CHANNEL_MEASUREMENT_VALUE);
                Measurement measurement = oldMeasurements.stream()
                        .filter(e -> e.getValue(MEASUREMENT_VALUE).equals(measurementValue))
                        .findAny()
                        .map(e -> new Measurement(e.getValue(MEASUREMENT_NAME), e.getValue(MEASUREMENT_VALUE)))
                        .orElse(null);
                if (measurement == null) measurement = measurementRepository.getByValue(measurementValue);
                if (measurement == null) continue;

                Channel newChannel = transformer.transform(modelHolder, Channel.class);
                if (newChannel != null) {
                    String code = newChannel.getCode();
                    if (code.isEmpty()) continue;

                    newChannel.setMeasurementName(measurement.getName());
                    newChannel.setMeasurementValue(measurement.getValue());
                    int oldIndex = oldChannels.indexOf(newChannel);
                    if (oldIndex >= 0) {
                        if (option == ImportOption.REPLACE_EXISTED) oldChannels.set(oldIndex, newChannel);
                    } else {
                        oldChannels.add(newChannel);
                    }
                }
            }
            return channelRepository.rewrite(oldChannels);
        }
        return true;
    }

    private boolean importSensors(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.CHANNEL).collect(Collectors.toList());

        if (input.size() > 0) {
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            List<Sensor> oldSensors = new ArrayList<>(sensorRepository.getAll());
            for (ModelHolder modelHolder : input) {
                String code = modelHolder.getValue(CHANNEL_CODE);
                if (code == null || code.isEmpty()) continue;

                ModelHolder sensorModelHolder = jsonParser.parse(modelHolder.getValue(CHANNEL_SENSOR_JSON), Model.SENSOR);
                Sensor newSensor = transformer.transform(sensorModelHolder, Sensor.class);
                if (newSensor != null) {
                    newSensor.setChannelCode(code);
                    int oldIndex = oldSensors.indexOf(newSensor);
                    if (oldIndex >= 0) {
                        if (option == ImportOption.REPLACE_EXISTED) oldSensors.set(oldIndex, newSensor);
                    } else {
                        oldSensors.add(newSensor);
                    }
                }
            }
            return sensorRepository.rewrite(oldSensors);
        }
        return true;
    }

    private boolean importCalibrators(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.CALIBRATOR).collect(Collectors.toList());
        if (input.size() > 0) {
            CalibratorRepository repository = repositoryFactory.getImplementation(CalibratorRepository.class);

            List<Calibrator> oldCalibrators = new ArrayList<>(repository.getAll());
            for (ModelHolder modelHolder : input) {
                Calibrator newCalibrator = transformer.transform(modelHolder, Calibrator.class);
                if (newCalibrator != null) {
                    String name = newCalibrator.getName();
                    if (name.isEmpty()) continue;

                    int oldIndex = oldCalibrators.indexOf(newCalibrator);
                    if (oldIndex >= 0) {
                        if (option == ImportOption.REPLACE_EXISTED) oldCalibrators.set(oldIndex, newCalibrator);
                    } else {
                        oldCalibrators.add(newCalibrator);
                    }
                }
            }
            return repository.rewrite(oldCalibrators);
        }
        return true;
    }

    private boolean importControlPoints(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.CONTROL_POINTS).collect(Collectors.toList());
        if (input.size() > 0) {
            ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);

            List<ControlPoints> oldControlPoints = new ArrayList<>(repository.getAll());
            for (ModelHolder modelHolder : input) {
                ControlPoints newControlPoints = transformer.transform(modelHolder, ControlPoints.class);
                if (newControlPoints != null) {
                    String name = newControlPoints.getName();
                    if (name.isEmpty()) continue;

                    int oldIndex = oldControlPoints.indexOf(newControlPoints);
                    if (oldIndex >= 0) {
                        if (option == ImportOption.REPLACE_EXISTED) oldControlPoints.set(oldIndex, newControlPoints);
                    } else {
                        oldControlPoints.add(newControlPoints);
                    }
                }
            }
            return repository.rewrite(oldControlPoints);
        }
        return true;
    }

    private boolean importMeasurementTransformFactors(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.MEASUREMENT).collect(Collectors.toList());
        if (input.size() > 0) {
            MeasurementFactorRepository repository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);

            for (ModelHolder modelHolder : input) {
                String from = modelHolder.getValue(MEASUREMENT_VALUE);
                if (from == null || from.isEmpty()) continue;

                Collection<MeasurementTransformFactor> oldFactors = repository.getBySource(from);

                Map<String, String> factors = jsonParser.parse(modelHolder.getValue(MEASUREMENT_FACTORS));
                for (Map.Entry<String, String> entry : factors.entrySet()) {
                    String to = entry.getKey();

                    String value = entry.getValue();
                    if (!StringHelper.isDouble(value)) continue;

                    double factor = Double.parseDouble(value);
                    MeasurementTransformFactor old = oldFactors.stream()
                            .filter(f -> f.getTransformTo().equals(to))
                            .findAny()
                            .orElse(null);
                    if (old == null) {
                        if (repository.add(from, to, factor) < 0) return false;
                    } else {
                        if (option == ImportOption.REPLACE_EXISTED) {
                            if (!repository.changeFactor(old.getId(), factor)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean importPersons(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.PERSON).collect(Collectors.toList());
        if (input.size() > 0) {
            PersonRepository repository = repositoryFactory.getImplementation(PersonRepository.class);
            Collection<Person> oldPersons = repository.getAll();

            for (ModelHolder modelHolder : input) {
                Person newPerson = transformer.transform(modelHolder, Person.class);
                if (newPerson != null) {
                    Optional<Person> op = oldPersons.stream().filter(p -> p.equalsIgnoreId(newPerson)).findAny();
                    if (!op.isPresent()) {
                        repository.add(newPerson);
                    }
                }
            }
        }
        return true;
    }
}
