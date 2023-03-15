package service.importer.updater.from_v5.to_v6;

import model.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.area.AreaRepository;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.department.DepartmentRepository;
import repository.repos.installation.InstallationRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.person.PersonRepository;
import repository.repos.process.ProcessRepository;
import repository.repos.sensor.SensorRepository;
import service.importer.*;
import service.importer.model.Model;
import service.importer.model.ModelHolder;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static service.importer.model.ModelField.*;

public class DefaultImporter implements Importer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultImporter.class);

    private final ImportOption option;
    private final JsonParser jsonParser = JsonParser_v5.getInstance();
    private final Transformer transformer = Transformer_v6.getInstance();

    public DefaultImporter(ImportOption option) {
        this.option = option;
    }

    @Override
    public boolean importing(@Nonnull List<ModelHolder> in, @Nonnull RepositoryFactory repositoryFactory) {
        if (!importMeasurements(in, repositoryFactory)) return false;
        if (!importChannels(in, repositoryFactory)) return false;
        if (!importSensors(in, repositoryFactory)) return false;
        if (!importCalibrators(in, repositoryFactory)) return false;
        if (!importDepartments(in, repositoryFactory)) return false;
        if (!importAreas(in, repositoryFactory)) return false;
        if (!importProcesses(in, repositoryFactory)) return false;
        if (!importInstallations(in, repositoryFactory)) return false;
        if (!importControlPoints(in, repositoryFactory)) return false;
        if (!importMeasurementTransformFactors(in, repositoryFactory)) return false;

        return importPersons(in, repositoryFactory);
    }

    private boolean importMeasurements(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.MEASUREMENT).collect(Collectors.toList());
        if (input.size() > 0) {
            MeasurementRepository repository = repositoryFactory.getImplementation(MeasurementRepository.class);

            for (ModelHolder modelHolder : input) {
                String name = modelHolder.getValue(MEASUREMENT_NAME);
                String value = modelHolder.getValue(MEASUREMENT_VALUE);

                Measurement measurement = repository.getByValue(value);
                if (measurement == null) {
                    if (!repository.add(new Measurement(name, value))) return false;
                } else {
                    if (option == ImportOption.REPLACE_EXISTED) {
                        if (!repository.set(measurement, new Measurement(name, value))) return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean importChannels(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.CHANNEL).collect(Collectors.toList());

        if (input.size() > 0) {
            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
            List<ModelHolder> oldMeasurements = in.stream().filter(e -> e.getModel() == Model.MEASUREMENT).collect(Collectors.toList());

            for (ModelHolder modelHolder : input) {
                String measurementValue = modelHolder.getValue(CHANNEL_MEASUREMENT_VALUE);
                Measurement measurement = oldMeasurements.stream()
                        .filter(e -> e.getValue(MEASUREMENT_VALUE).equals(measurementValue))
                        .findAny()
                        .map(e -> new Measurement(e.getValue(MEASUREMENT_NAME), e.getValue(MEASUREMENT_VALUE)))
                        .orElse(null);
                if (measurement == null) measurement = measurementRepository.getByValue(measurementValue);
                if (measurement == null) measurement = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);

                String code = modelHolder.getValue(CHANNEL_CODE);
                Channel newChannel = transformer.transform(modelHolder, Channel.class);
                if (newChannel != null) {
                    newChannel.setMeasurementName(measurement.getName());
                    newChannel.setMeasurementValue(measurement.getValue());
                    Channel oldChannel = channelRepository.get(code);
                    if (oldChannel == null) {
                        if (!channelRepository.add(newChannel)) return false;
                    } else {
                        if (option == ImportOption.REPLACE_EXISTED) {
                            if (!channelRepository.set(oldChannel, newChannel)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean importSensors(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.CHANNEL).collect(Collectors.toList());

        if (input.size() > 0) {
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);


            for (ModelHolder modelHolder : input) {
                String code = modelHolder.getValue(CHANNEL_CODE);
                ModelHolder sensorModelHolder = jsonParser.parse(modelHolder.getValue(CHANNEL_SENSOR_JSON), Model.SENSOR);
                Sensor newSensor = transformer.transform(sensorModelHolder, Sensor.class);
                if (newSensor != null) {
                    newSensor.setChannelCode(code);
                    Sensor oldSensor = sensorRepository.get(code);
                    if (oldSensor == null) {
                        if (!sensorRepository.add(newSensor)) return false;
                    } else {
                        if (option == ImportOption.REPLACE_EXISTED) {
                            if (!sensorRepository.set(oldSensor, newSensor)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean importCalibrators(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.CALIBRATOR).collect(Collectors.toList());
        if (input.size() > 0) {
            CalibratorRepository repository = repositoryFactory.getImplementation(CalibratorRepository.class);

            for (ModelHolder modelHolder : input) {
                String name = modelHolder.getValue(CALIBRATOR_NAME);
                Calibrator newCalibrator = transformer.transform(modelHolder, Calibrator.class);
                if (newCalibrator != null) {
                    Calibrator oldCalibrator = repository.get(name);
                    if (oldCalibrator == null) {
                        if (!repository.add(newCalibrator)) return false;
                    } else {
                        if (option == ImportOption.REPLACE_EXISTED) {
                            if (!repository.set(oldCalibrator, newCalibrator)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean importDepartments(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.DEPARTMENT).collect(Collectors.toList());
        if (input.size() > 0) {
            DepartmentRepository repository = repositoryFactory.getImplementation(DepartmentRepository.class);
            Collection<String> departments = repository.getAll();

            for (ModelHolder modelHolder : input) {
                String department = modelHolder.getValue(DEPARTMENT);
                if (department != null && !departments.contains(department)) {
                    if (!repository.add(department)) return false;
                }
            }
        }
        return true;
    }

    private boolean importAreas(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.AREA).collect(Collectors.toList());
        if (input.size() > 0) {
            AreaRepository repository = repositoryFactory.getImplementation(AreaRepository.class);
            Collection<String> areas = repository.getAll();

            for (ModelHolder modelHolder : input) {
                String area = modelHolder.getValue(AREA);
                if (area != null && !areas.contains(area)) {
                    if (!repository.add(area)) return false;
                }
            }
        }
        return true;
    }

    private boolean importProcesses(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.PROCESS).collect(Collectors.toList());
        if (input.size() > 0) {
            ProcessRepository repository = repositoryFactory.getImplementation(ProcessRepository.class);
            Collection<String> processes = repository.getAll();

            for (ModelHolder modelHolder : input) {
                String process = modelHolder.getValue(PROCESS);
                if (process != null && !processes.contains(process)) {
                    if (!repository.add(process)) return false;
                }
            }
        }
        return true;
    }

    private boolean importInstallations(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.INSTALLATION).collect(Collectors.toList());
        if (input.size() > 0) {
            InstallationRepository repository = repositoryFactory.getImplementation(InstallationRepository.class);
            Collection<String> installations = repository.getAll();

            for (ModelHolder modelHolder : input) {
                String installation = modelHolder.getValue(INSTALLATION);
                if (installation != null && !installations.contains(installation)) {
                    if (!repository.add(installation)) return false;
                }
            }
        }
        return true;
    }

    private boolean importControlPoints(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.CONTROL_POINTS).collect(Collectors.toList());
        if (input.size() > 0) {
            ControlPointsRepository repository = repositoryFactory.getImplementation(ControlPointsRepository.class);

            for (ModelHolder modelHolder : input) {
                ControlPoints newControlPoints = transformer.transform(modelHolder, ControlPoints.class);
                if (newControlPoints != null) {
                    ControlPoints oldControlPoints = repository.get(newControlPoints.getName());
                    if (oldControlPoints == null) {
                        if (!repository.add(newControlPoints)) return false;
                    } else {
                        if (option == ImportOption.REPLACE_EXISTED) {
                            if (!repository.set(oldControlPoints, newControlPoints)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean importMeasurementTransformFactors(List<ModelHolder> in, RepositoryFactory repositoryFactory) {
        List<ModelHolder> input = in.stream().filter(e -> e.getModel() == Model.MEASUREMENT).collect(Collectors.toList());
        if (input.size() > 0) {
            MeasurementFactorRepository repository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);

            for (ModelHolder modelHolder : input) {
                String from = modelHolder.getValue(MEASUREMENT_VALUE);
                Collection<MeasurementTransformFactor> oldFactors = repository.getBySource(from);

                Map<String, String> factors = jsonParser.parse(modelHolder.getValue(MEASUREMENT_FACTORS));
                for (Map.Entry<String, String> entry : factors.entrySet()) {
                    String to = entry.getKey();
                    double factor = Double.parseDouble(entry.getValue());
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
