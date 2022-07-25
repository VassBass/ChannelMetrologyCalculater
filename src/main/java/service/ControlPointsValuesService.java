package service;

import model.ControlPointsValues;

import java.util.List;

public interface ControlPointsValuesService extends Service<ControlPointsValues> {
    List<ControlPointsValues>getBySensorType(String sensorType);
    ControlPointsValues getControlPointsValues(int id);

    int addReturnId(ControlPointsValues controlPointsValues);

    boolean set(ControlPointsValues cpv);
    boolean changeSensorType(String oldSensorType, String newSensorType);

    boolean removeAll(String sensorType);
    boolean resetToDefault();
}
