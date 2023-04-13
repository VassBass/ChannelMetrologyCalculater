package repository.repos.sensor_error;

import model.dto.SensorError;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import util.StringHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BufferedSensorErrorRepositorySQLite extends SensorErrorRepositorySQLite {
    private final Map<String, SensorError> buffer;

    public BufferedSensorErrorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        this.buffer = super.getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SensorError::getId, Function.identity()));
    }

    @Override
    public Collection<SensorError> getAll() {
        return buffer.values();
    }

    @Override
    public Collection<SensorError> getBySensorType(@Nonnull String sensorType) {
        if (sensorType.isEmpty()) return new ArrayList<>(0);
        return buffer.values().stream()
                .filter(se -> StringHelper.containsEachOtherIgnoreCase(se.getType(), sensorType))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean add(@Nonnull SensorError error) {
        String id = error.getId();
        if (buffer.containsKey(id)) return false;

        buffer.put(id, error);
        return super.add(error);
    }

    @Override
    public SensorError getById(@Nonnull String id) {
        return buffer.get(id);
    }

    @Override
    public boolean set(@Nonnull String oldId, @Nonnull SensorError newError) {
        if (!buffer.containsKey(oldId)) return false;

        if (!oldId.equals(newError.getId())) {
            if (buffer.containsKey(newError.getId())) return false;

            buffer.remove(oldId);
        }

        buffer.put(newError.getId(), newError);
        return super.set(oldId, newError);
    }

    @Override
    public boolean removeById(@Nonnull String id) {
        buffer.remove(id);
        return super.removeById(id);
    }

    @Override
    public boolean removeBySensorType(@Nonnull String sensorType) {
        buffer.values().removeIf(se -> se.getType().equals(sensorType));
        return super.removeBySensorType(sensorType);
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String value) {
        buffer.values().removeIf(se -> se.getMeasurementValue().equals(value));
        return super.removeByMeasurementValue(value);
    }

    @Override
    public boolean isExists(@Nonnull String id) {
        return buffer.containsKey(id);
    }

    @Override
    public boolean isExists(String oldId, String newId) {
        if (oldId == null || newId == null || !buffer.containsKey(oldId)) return true;
        if (oldId.equals(newId)) return false;
        return isExists(newId);
    }
}
