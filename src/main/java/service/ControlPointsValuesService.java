package service;

import model.ControlPointsValues;

import java.util.List;

public interface ControlPointsValuesService {
    List<ControlPointsValues> getAll();
    List<ControlPointsValues>getBySensorType(String sensorType);
    ControlPointsValues getControlPointsValues(int id);
    int add(ControlPointsValues controlPointsValues);
    boolean set(ControlPointsValues cpv);
    boolean changeSensorType(String oldSensorType, String newSensorType);
    boolean remove(ControlPointsValues controlPointsValues);
    boolean removeAll(String sensorType);
    boolean clear();
    boolean resetToDefault();
}
