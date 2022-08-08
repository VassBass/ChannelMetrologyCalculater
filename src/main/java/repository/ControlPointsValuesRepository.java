package repository;

import model.ControlPointsValues;

import javax.annotation.Nonnull;
import java.util.List;

public interface ControlPointsValuesRepository extends Repository<ControlPointsValues>{
    List<ControlPointsValues>getBySensorType(@Nonnull String sensorType);
    ControlPointsValues getControlPointsValues(int id);
    Integer addReturnId(@Nonnull ControlPointsValues cpv);
    boolean set(@Nonnull ControlPointsValues cpv);
    boolean changeSensorType(@Nonnull String oldSensorType, @Nonnull String newSensorType);
    boolean removeAll(@Nonnull String sensorType);
    boolean resetToDefault();
}
