package service.repository.repos.calibrator;

import model.Calibrator;
import model.Measurement;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BufferedCalibratorRepositorySQLite extends CalibratorRepositorySQLite {
    private final Map<String, Calibrator> buffer;

    public BufferedCalibratorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        buffer = getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Calibrator::getName, Function.identity()));
    }

    @Override
    public Collection<Calibrator> getAll() {
        return buffer.values();
    }

    @Override
    public String[] getAllNames(@Nonnull Measurement measurement) {
        return buffer.values().stream()
                .filter(c -> c.getMeasurement().equals(measurement.getName()))
                .map(Calibrator::getName)
                .toArray(String[]::new);
    }

    @Override
    public Calibrator get(@Nonnull String name) {
        return buffer.get(name);
    }

    @Override
    public boolean add(@Nonnull Calibrator calibrator) {
        if (buffer.containsKey(calibrator.getName())) return false;

        buffer.put(calibrator.getName(), calibrator);
        return super.add(calibrator);
    }

    @Override
    public boolean remove(@Nonnull Calibrator calibrator) {
        return buffer.remove(calibrator.getName()) != null && super.remove(calibrator);
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String measurementValue) {
        Collection<Calibrator> result = buffer.values().stream()
                .filter(c -> !c.getValue().equals(measurementValue))
                .collect(Collectors.toSet());

        buffer.clear();
        buffer.putAll(result.stream().collect(Collectors.toMap(Calibrator::getName, Function.identity())));
        return super.removeByMeasurementValue(measurementValue);
    }

    @Override
    public boolean set(@Nonnull Calibrator oldCalibrator, @Nonnull Calibrator newCalibrator) {
        if (!oldCalibrator.equals(newCalibrator)) {
            if (buffer.containsKey(newCalibrator.getName())) return false;

            buffer.remove(oldCalibrator.getName());
        }

        buffer.put(newCalibrator.getName(), newCalibrator);
        return super.set(oldCalibrator, newCalibrator);
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        buffer.values().forEach(c -> {
            if (c.getValue().equals(oldValue)) c.setValue(newValue);
        });
        return super.changeMeasurementValue(oldValue, newValue);
    }

    @Override
    public boolean clear() {
        buffer.clear();
        return super.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Calibrator> calibrators) {
        buffer.clear();
        buffer.putAll(calibrators.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Calibrator::getName, Function.identity())));
        return super.rewrite(calibrators);
    }

    @Override
    public boolean importData(@Nonnull Collection<Calibrator> newCalibrators, @Nonnull Collection<Calibrator> calibratorsForChange) {
        buffer.putAll(calibratorsForChange.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Calibrator::getName, Function.identity())));
        buffer.putAll(newCalibrators.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Calibrator::getName, Function.identity())));
        return super.importData(newCalibrators, calibratorsForChange);
    }

    @Override
    public boolean isExists(@Nonnull Calibrator calibrator) {
        return buffer.containsKey(calibrator.getName());
    }
}
