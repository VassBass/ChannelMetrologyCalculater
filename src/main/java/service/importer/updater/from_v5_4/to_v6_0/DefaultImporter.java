package service.importer.updater.from_v5_4.to_v6_0;

import model.dto.Channel;
import model.dto.Measurement;
import model.dto.builder.ChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.measurement.MeasurementRepository;
import service.importer.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static service.importer.ModelField.*;

public class DefaultImporter implements Importer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultImporter.class);

    private final ImportOption option;

    public DefaultImporter(ImportOption option) {
        this.option = option;
    }

    @Override
    public boolean importing(@Nonnull List<ModelHolder> in, @Nonnull RepositoryFactory repositoryFactory) {
        if (!importMeasurements(in, repositoryFactory)) return false;
        if (!importChannels(in, repositoryFactory)) return false;

        return true;
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
                    return repository.add(new Measurement(name, value));
                } else {
                    if (option == ImportOption.REPLACE_EXISTED) {
                        return repository.set(measurement, new Measurement(name, value));
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
                Channel newChannel = new ChannelBuilder(code)
                        .setName(modelHolder.getValue(CHANNEL_NAME))
                        .setMeasurementName(measurement.getName())
                        .setMeasurementValue(measurement.getValue())
                        .setDepartment(modelHolder.getValue(CHANNEL_DEPARTMENT))
                        .setArea(modelHolder.getValue(CHANNEL_AREA))
                        .setProcess(modelHolder.getValue(CHANNEL_PROCESS))
                        .setInstallation(modelHolder.getValue(CHANNEL_INSTALLATION))
                        .setDate(modelHolder.getValue(CHANNEL_DATE))
                        .setFrequency(Double.parseDouble(modelHolder.getValue(CHANNEL_FREQUENCY)))
                        .setTechnologyNumber(modelHolder.getValue(CHANNEL_TECHNOLOGY_NUMBER))
                        .setNumberOfProtocol(modelHolder.getValue(CHANNEL_PROTOCOL_NUMBER))
                        .setRangeMin(Double.parseDouble(modelHolder.getValue(CHANNEL_RANGE_MIN)))
                        .setRangeMax(Double.parseDouble(modelHolder.getValue(CHANNEL_RANGE_MAX)))
                        .setReference(modelHolder.getValue(CHANNEL_REFERENCE))
                        .setSuitability(Boolean.parseBoolean(modelHolder.getValue(CHANNEL_SUITABILITY)))
                        .setAllowableErrorInPercent(Double.parseDouble(modelHolder.getValue(CHANNEL_ALLOWABLE_ERROR_PERCENT)))
                        .setAllowableErrorInValue(Double.parseDouble(modelHolder.getValue(CHANNEL_ALLOWABLE_ERROR_VALUE)))
                        .build();

                Channel oldChannel = channelRepository.get(code);
                if (oldChannel == null) {
                    return channelRepository.add(newChannel);
                } else {
                    if (option == ImportOption.REPLACE_EXISTED) {
                        return channelRepository.set(oldChannel, newChannel);
                    }
                }
            }
        }
        return true;
    }

    private ModelHolder parseSensorFromJson(String json) {
        ModelHolder sensor = new ModelHolder(Model.SENSOR);
        String[] fields = json
                .replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .split("\\,");
        for (String f : fields) {
            String[] vals = f.split("\\:");
            switch (vals[0]) {
                case "type":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_TYPE, vals[1]);
                    } else {
                        sensor.setField(SENSOR_TYPE, EMPTY);
                    }
                    break;
                case "name":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_NAME, vals[1]);
                    } else {
                        sensor.setField(SENSOR_NAME, EMPTY);
                    }
                    break;
                case "rangeMin":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_RANGE_MIN, vals[1]);
                    } else {
                        sensor.setField(SENSOR_RANGE_MIN, "0");
                    }
                    break;
                case "rangeMax":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_RANGE_MAX, vals[1]);
                    } else {
                        sensor.setField(SENSOR_RANGE_MAX, "100");
                    }
                    break;
                case "number":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_SERIAL_NUMBER, vals[1]);
                    } else {
                        sensor.setField(SENSOR_SERIAL_NUMBER, EMPTY);
                    }
                    break;
                case "value":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_MEASUREMENT_VALUE, vals[1]);
                    } else {
                        sensor.setField(SENSOR_MEASUREMENT_VALUE, EMPTY);
                    }
                    break;
                case "measurement":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_MEASUREMENT_NAME, vals[1]);
                    } else {
                        sensor.setField(SENSOR_MEASUREMENT_NAME, EMPTY);
                    }
                    break;
                case "errorFormula":
                    if (vals.length > 1) {
                        sensor.setField(SENSOR_ERROR_FORMULA, vals[1]);
                    } else {
                        sensor.setField(SENSOR_ERROR_FORMULA, EMPTY);
                    }
                    break;
            }
        }
    }
}
