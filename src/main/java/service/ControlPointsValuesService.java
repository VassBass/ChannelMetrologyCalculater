package service;

import model.ControlPointsValues;

import java.util.Collection;

public interface ControlPointsValuesService extends Service<ControlPointsValues> {
    Collection<ControlPointsValues> getBySensorType(String sensorType);
    ControlPointsValues getControlPointsValues(int id);

    Integer addReturnId(ControlPointsValues controlPointsValues);

    boolean set(ControlPointsValues cpv);
    boolean changeSensorType(String oldSensorType, String newSensorType);

    boolean removeAll(String sensorType);
    boolean resetToDefault();
}
