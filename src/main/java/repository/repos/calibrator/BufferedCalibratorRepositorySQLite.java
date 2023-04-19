package repository.repos.calibrator;

import model.dto.Calibrator;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

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
        buffer = super.getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Calibrator::getName, Function.identity()));
    }

    @Override
    public Collection<Calibrator> getAll() {
        return buffer.values();
    }

    @Override
    public String[] getAllNamesByMeasurementName(@Nonnull String measurementName) {
        return buffer.values().stream()
                .filter(c -> c.getMeasurementName().equals(measurementName))
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
    public boolean removeByName(@Nonnull String name) {
        return buffer.remove(name) != null && super.removeByName(name);
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String value) {
        Collection<Calibrator> result = buffer.values();
        result.removeIf(c -> c.getMeasurementValue().equals(value));

        return super.removeByMeasurementValue(value);
    }

    @Override
    public boolean set(@Nonnull Calibrator oldCalibrator, @Nonnull Calibrator newCalibrator) {
        if (!buffer.containsKey(oldCalibrator.getName())) return false;

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
            if (c.getMeasurementValue().equals(oldValue)) c.setMeasurementValue(newValue);
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
    public boolean isExists(@Nonnull String calibratorName) {
        return buffer.containsKey(calibratorName);
    }

    @Override
    public boolean isExist(@Nonnull String oldCalibratorName, @Nonnull String newCalibratorName) {
        if (oldCalibratorName.equals(newCalibratorName)) return false;
        return buffer.containsKey(newCalibratorName);
    }
}
