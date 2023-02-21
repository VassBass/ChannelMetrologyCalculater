package service.repository.repos.sensor;

import model.dto.Sensor;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class BufferedSensorRepositorySQLite extends SensorRepositorySQLite {
    private final Map<String, Sensor> buffer;

    public BufferedSensorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        buffer = super.getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Sensor::getName, Function.identity()));
    }

    @Override
    public Collection<Sensor> getAll() {
        return buffer.values();
    }

    @Override
    public Collection<Sensor> getAllByMeasurementName(@Nonnull String measurement) {
        return buffer.values().stream()
                .filter(s -> s.getMeasurement().equals(measurement))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<String> getAllTypes() {
        return buffer.values().stream()
                .map(Sensor::getType)
                .collect(Collectors.toSet());
    }

    @Override
    public String getMeasurementNameBySensorType(@Nonnull String sensorType) {
        Optional<Sensor> s = buffer.values().stream()
                .filter(sen -> sen.getType().equals(sensorType))
                .findAny();
        return s.isPresent() ?
                s.get().getMeasurement() :
                EMPTY;
    }

    @Override
    public Collection<String> getAllSensorsNameByMeasurementName(@Nonnull String measurementName) {
        return buffer.values().stream()
                .filter(s -> s.getMeasurement().equals(measurementName))
                .map(Sensor::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public Sensor get(@Nonnull String sensorName) {
        return buffer.get(sensorName);
    }

    @Override
    public boolean add(@Nonnull Sensor sensor) {
        if (buffer.containsKey(sensor.getName())) return false;

        buffer.put(sensor.getName(), sensor);
        return super.add(sensor);
    }

    @Override
    public boolean remove(@Nonnull Sensor sensor) {
        return buffer.remove(sensor.getName()) != null && super.remove(sensor);
    }

    @Override
    public boolean set(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor) {
        if (!buffer.containsKey(oldSensor.getName())) return false;

        if (!oldSensor.equals(newSensor)) {
            if (buffer.containsKey(newSensor.getName())) return false;

            buffer.remove(oldSensor.getName());
        }

        buffer.put(newSensor.getName(), newSensor);
        return super.set(oldSensor, newSensor);
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        buffer.values().stream()
                .filter(s -> s.getValue().equals(oldValue))
                .forEach(s -> s.setValue(newValue));
        return super.changeMeasurementValue(oldValue, newValue);
    }

    @Override
    public boolean removeMeasurementValue(@Nonnull String measurementValue) {
        buffer.values().forEach(s -> {
            if (s.getValue().equals(measurementValue)) s.setValue(EMPTY);
        });
        return super.removeMeasurementValue(measurementValue);
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Sensor> sensors) {
        buffer.clear();
        buffer.putAll(sensors.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Sensor::getName, Function.identity())));
        return super.rewrite(sensors);
    }

    @Override
    public boolean isLastInMeasurement(@Nonnull Sensor sensor) {
        return buffer.values().stream()
                .filter(s -> s.getMeasurement().equals(sensor.getMeasurement()))
                .count() == 1L;
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
                .collect(Collectors.toMap(Sensor::getName, Function.identity())));
        buffer.putAll(newSensors.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Sensor::getName, Function.identity())));
        return super.importData(newSensors, sensorsForChange);
    }

    @Override
    public boolean isExists(@Nonnull String sensorName) {
        return buffer.containsKey(sensorName);
    }
}
