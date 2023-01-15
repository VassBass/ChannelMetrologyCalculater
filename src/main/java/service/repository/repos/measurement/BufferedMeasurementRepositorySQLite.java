package service.repository.repos.measurement;

import model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class BufferedMeasurementRepositorySQLite extends MeasurementRepositorySQLite {
    private static final Logger logger = LoggerFactory.getLogger(BufferedMeasurementRepositorySQLite.class);

    private final Map<String, Measurement> buffer;

    public BufferedMeasurementRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        this.buffer = new HashMap<>();
        super.getAll().forEach(m -> buffer.put(m.getValue(), m));
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
    public Measurement get(@Nonnull String value) {
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
    public boolean changeFactors(@Nonnull String measurementValue, @Nonnull Map<String, Double> factors) {
        buffer.get(measurementValue).setFactors(factors);
        return super.changeFactors(measurementValue, factors);
    }

    @Override
    public boolean set(@Nonnull Measurement oldMeasurement, @Nonnull Measurement newMeasurement) {
        if (buffer.containsKey(oldMeasurement.getValue()) && buffer.containsKey()) {
            buffer.put(oldMeasurement.getValue(), newMeasurement);
            return super.set(oldMeasurement, newMeasurement);
        }
    }

    @Override
    public boolean remove(@Nonnull Measurement measurement) {
        return super.remove(measurement);
    }

    @Override
    public boolean clear() {
        return super.clear();
    }

    @Override
    public Collection<Measurement> getMeasurements(@Nonnull String name) {
        return super.getMeasurements(name);
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Measurement> measurements) {
        return super.rewrite(measurements);
    }

    @Override
    public boolean isLastInMeasurement(@Nonnull String measurementValue) {
        return super.isLastInMeasurement(measurementValue);
    }

    @Override
    public boolean exists(@Nonnull String measurementValue) {
        return super.exists(measurementValue);
    }

    @Override
    public boolean exists(@Nonnull String oldValue, @Nonnull String newValue) {
        return super.exists(oldValue, newValue);
    }
}
