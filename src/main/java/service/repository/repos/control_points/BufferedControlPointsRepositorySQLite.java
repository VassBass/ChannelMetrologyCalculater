package service.repository.repos.control_points;

import model.ControlPoints;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BufferedControlPointsRepositorySQLite extends ControlPointsRepositorySQLite {
    private final Map<String, ControlPoints> buffer;

    public BufferedControlPointsRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        buffer = super.getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ControlPoints::getName, Function.identity()));
    }

    @Override
    public Collection<ControlPoints> getAll() {
        return buffer.values();
    }

    @Override
    public Collection<ControlPoints> getAllBySensorType(@Nonnull String sensorType) {
        return buffer.values().stream()
                .filter(cp -> cp.getSensorType().equalsIgnoreCase(sensorType))
                .collect(Collectors.toSet());
    }

    @Override
    public ControlPoints get(@Nonnull String name) {
        return buffer.get(name);
    }

    @Override
    public boolean add(@Nonnull ControlPoints controlPoints) {
        if (buffer.containsKey(controlPoints.getName())) return false;

        buffer.put(controlPoints.getName(), controlPoints);
        return super.add(controlPoints);
    }

    @Override
    public boolean set(@Nonnull ControlPoints oldControlPoints, @Nonnull ControlPoints newControlPoints) {
        if (!buffer.containsKey(oldControlPoints.getName())) return false;

        if (!oldControlPoints.equals(newControlPoints)) {
            if (buffer.containsKey(newControlPoints.getName())) return false;

            buffer.remove(oldControlPoints.getName());
        }

        buffer.put(newControlPoints.getName(), newControlPoints);
        return super.set(oldControlPoints, newControlPoints);
    }

    @Override
    public boolean changeSensorType(@Nonnull String oldSensorType, @Nonnull String newSensorType) {
        buffer.values().forEach(cp -> {
            if (cp.getSensorType().equals(oldSensorType)) cp.setSensorType(newSensorType);
        });
        return super.changeSensorType(oldSensorType, newSensorType);
    }

    @Override
    public boolean removeByName(@Nonnull String name) {
        buffer.remove(name);
        return super.removeByName(name);
    }

    @Override
    public boolean removeBySensorType(@Nonnull String sensorType) {
        Collection<ControlPoints> result = buffer.values();
        result.removeIf(cp -> cp.getSensorType().equals(sensorType));

        return super.removeBySensorType(sensorType);
    }

    @Override
    public boolean clear() {
        buffer.clear();
        return super.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<ControlPoints> list) {
        buffer.clear();
        buffer.putAll(list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ControlPoints::getName, Function.identity())));
        return super.rewrite(list);
    }
}
