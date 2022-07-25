package repository;

import model.ControlPointsValues;

import java.util.List;

public interface ControlPointsValuesRepository extends Repository<ControlPointsValues>{
    List<ControlPointsValues>getBySensorType(String sensorType);
    ControlPointsValues getControlPointsValues(int id);
    int addReturnId(ControlPointsValues cpv);
    boolean set(ControlPointsValues cpv);
    boolean changeSensorType(String oldSensorType, String newSensorType);
    boolean removeAll(String sensorType);
    boolean resetToDefault();
}
