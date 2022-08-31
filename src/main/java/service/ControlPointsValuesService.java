package service;

import model.ControlPointsValues;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

public interface ControlPointsValuesService extends Service<ControlPointsValues> {
    Collection<ControlPointsValues> getBySensorType(@Nonnull String sensorType);
    Optional<ControlPointsValues> getById(@Nonnegative int id);

    Optional<Integer> addReturnId(@Nonnull ControlPointsValues controlPointsValues);

    boolean set(@Nonnull ControlPointsValues cpv);
    boolean changeSensorType(@Nonnull String oldSensorType,@Nonnull String newSensorType);

    boolean removeAll(@Nonnull String sensorType);
    boolean resetToDefault();
}
