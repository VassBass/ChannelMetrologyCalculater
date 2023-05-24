package repository.repos.sensor_error;

import model.dto.SensorError;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface SensorErrorRepository {
    Collection<SensorError> getAll();
    Collection<SensorError> getBySensorType(@Nonnull String sensorType);

    boolean add(@Nonnull SensorError error);

    SensorError getById(@Nonnull String id);

    boolean set(@Nonnull String oldId, @Nonnull SensorError newError);
    boolean changeSensorType(@Nonnull String oldType, @Nonnull String newType);
    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);

    boolean removeById(@Nonnull String id);
    boolean removeBySensorType(@Nonnull String sensorType);
    boolean removeByMeasurementValue(@Nonnull String value);

    boolean isExists(@Nonnull String id);
    boolean isExists(String oldId, String newId);

    boolean rewrite(Collection<SensorError> newErrors);
}
