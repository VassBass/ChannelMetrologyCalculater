package repository;

import model.ControlPointsValues;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface ControlPointsValuesRepository extends Repository<ControlPointsValues>{
    List<ControlPointsValues>getBySensorType(@Nonnull String sensorType);
    Optional<ControlPointsValues> getById(@Nonnegative int id);
    Optional<Integer> addReturnId(@Nonnull ControlPointsValues cpv);

    @Override
    boolean set(@Nonnull ControlPointsValues cpv, @Nullable ControlPointsValues ignored);

    boolean set(@Nonnull ControlPointsValues cpv);
    boolean changeSensorType(@Nonnull String oldSensorType, @Nonnull String newSensorType);
    boolean removeAll(@Nonnull String sensorType);
}
