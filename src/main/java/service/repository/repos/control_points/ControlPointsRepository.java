package service.repository.repos.control_points;

import model.dto.ControlPoints;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface ControlPointsRepository {
    Collection<ControlPoints> getAll();
    Collection<ControlPoints> getAllBySensorType(@Nonnull String sensorType);

    ControlPoints get(@Nonnull String name);

    boolean add(@Nonnull ControlPoints controlPoints);

    boolean set(@Nonnull ControlPoints oldControlPoints, @Nonnull ControlPoints newControlPoints);
    boolean changeSensorType(String oldSensorType, String newSensorType);

    boolean removeByName(@Nonnull String name);
    boolean removeBySensorType(@Nonnull String sensorType);
    boolean clear();

    boolean rewrite(@Nonnull Collection<ControlPoints> controlPoints);
}
