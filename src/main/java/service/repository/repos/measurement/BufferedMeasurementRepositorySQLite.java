package service.repository.repos.measurement;

import model.Measurement;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class BufferedMeasurementRepositorySQLite extends MeasurementRepositorySQLite {
    private final Map<String, Measurement> buffer;

    public BufferedMeasurementRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        this.buffer = super.getAll().stream()
                .filter(Objects::nonNull)
                .collect(toMap(Measurement::getValue, Function.identity()));
    }

    @Override
    public Collection<Measurement> getAll() {
        return buffer.values();
    }

    @Override
    public String[] getAllNames() {
        return buffer.values().stream()
                .map(Measurement::getName)
                .distinct()
                .toArray(String[]::new);
    }

    @Override
    public String[] getAllValues() {
        return buffer.keySet().toArray(new String[0]);
    }

    @Override
    public String[] getValues(@Nonnull String name) {
        return buffer.values().stream()
                .filter(m -> m.getName().equals(name))
                .map(Measurement::getValue)
                .toArray(String[]::new);
    }

    @Override
    public Measurement getByValue(@Nonnull String value) {
        return buffer.get(value);
    }

    @Override
    public boolean add(@Nonnull Measurement measurement) {
        if (buffer.containsKey(measurement.getValue())) {
            return false;
        } else {
            buffer.put(measurement.getValue(), measurement);
            return super.add(measurement);
        }
    }

    @Override
    public boolean set(@Nonnull Measurement oldMeasurement, @Nonnull Measurement newMeasurement) {
        if (!buffer.containsKey(oldMeasurement.getValue())) return false;

        if (!oldMeasurement.equals(newMeasurement)) {
            if (buffer.containsKey(newMeasurement.getValue())) return false;

            buffer.remove(oldMeasurement.getValue());
        }

        buffer.put(newMeasurement.getValue(), newMeasurement);
        return super.set(oldMeasurement, newMeasurement);
    }

    @Override
    public boolean remove(@Nonnull Measurement measurement) {
        return buffer.remove(measurement.getValue()) != null && super.remove(measurement);
    }

    @Override
    public boolean clear() {
        buffer.clear();
        return super.clear();
    }

    @Override
    public Collection<Measurement> getMeasurementsByName(@Nonnull String name) {
        return buffer.values().stream()
                .filter(m -> m.getName().equals(name))
                .collect(toSet());
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Measurement> measurements) {
        buffer.clear();
        buffer.putAll(measurements.stream()
                .filter(Objects::nonNull)
                .collect(toMap(Measurement::getValue, Function.identity())));
        return super.rewrite(measurements);
    }

    @Override
    public boolean isLastInMeasurement(@Nonnull String measurementValue) {
        final String name = buffer.get(measurementValue).getName();
        return buffer.values().stream()
                .filter(m -> m.getName().equals(name))
                .count() == 1L;
    }

    @Override
    public boolean exists(@Nonnull String measurementValue) {
        return buffer.containsKey(measurementValue);
    }

    @Override
    public boolean exists(@Nonnull String oldValue, @Nonnull String newValue) {
        if (!buffer.containsKey(oldValue)) return true;
        if (oldValue.equals(newValue)) return false;
        return buffer.containsKey(newValue);
    }
}
