package repository.repos.sensor;

import model.dto.Sensor;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BufferedSensorRepositorySQLite extends SensorRepositorySQLite {
    private final Map<String, Sensor> buffer;

    public BufferedSensorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        buffer = super.getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Sensor::getChannelCode, Function.identity()));
    }

    @Override
    public Collection<Sensor> getAll() {
        return buffer.values();
    }

    @Override
    public Collection<Sensor> getAllByMeasurementName(@Nonnull final String measurement) {
        return buffer.values().stream()
                .filter(s -> s.getMeasurementName().equals(measurement))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<String> getAllTypes() {
        return buffer.values().stream()
                .map(Sensor::getType)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<String> getAllSensorsTypesByMeasurementName(@Nonnull final String measurementName) {
        return buffer.values().stream()
                .filter(s -> s.getMeasurementName().equals(measurementName))
                .map(Sensor::getType)
                .collect(Collectors.toSet());
    }

    @Override
    public Sensor get(@Nonnull String channelCode) {
        return buffer.get(channelCode);
    }

    @Override
    public boolean add(@Nonnull Sensor sensor) {
        if (buffer.containsKey(sensor.getChannelCode())) return false;

        buffer.put(sensor.getChannelCode(), sensor);
        return super.add(sensor);
    }

    @Override
    public boolean remove(@Nonnull Sensor sensor) {
        return buffer.remove(sensor.getChannelCode()) != null && super.remove(sensor);
    }

    @Override
    public boolean set(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor) {
        if (!buffer.containsKey(oldSensor.getChannelCode())) return false;

        if (!oldSensor.equals(newSensor)) {
            if (buffer.containsKey(newSensor.getChannelCode())) return false;

            buffer.remove(oldSensor.getChannelCode());
        }

        buffer.put(newSensor.getChannelCode(), newSensor);
        return super.set(oldSensor, newSensor);
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull final String oldValue, @Nonnull final String newValue) {
        buffer.values().stream()
                .filter(s -> s.getMeasurementValue().equals(oldValue))
                .forEach(s -> s.setMeasurementValue(newValue));
        return super.changeMeasurementValue(oldValue, newValue);
    }

    @Override
    public boolean changeSensorType(@Nonnull String oldType, @Nonnull String newType) {
        buffer.values().stream()
                .filter(s -> s.getType().equals(oldType))
                .forEach(s -> s.setType(newType));
        return super.changeSensorType(oldType, newType);
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Sensor> sensors) {
        buffer.clear();
        buffer.putAll(sensors.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Sensor::getChannelCode, Function.identity())));
        return super.rewrite(sensors);
    }

    @Override
    public boolean clear() {
        buffer.clear();
        return super.clear();
    }

    @Override
    public boolean importData(@Nonnull Collection<Sensor> newSensors, @Nonnull Collection<Sensor> sensorsForChange) {
        buffer.putAll(sensorsForChange.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Sensor::getChannelCode, Function.identity())));
        buffer.putAll(newSensors.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Sensor::getChannelCode, Function.identity())));
        return super.importData(newSensors, sensorsForChange);
    }

    @Override
    public boolean isExists(@Nonnull String channelCode) {
        return buffer.containsKey(channelCode);
    }

    @Override
    public boolean removeByChannelCode(@Nonnull String channelCode) {
        return buffer.remove(channelCode) != null && super.removeByChannelCode(channelCode);
    }
}
