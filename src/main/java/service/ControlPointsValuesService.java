package service;

import model.ControlPointsValues;
import model.Sensor;

import java.util.ArrayList;

public interface ControlPointsValuesService {
    ArrayList<ControlPointsValues>getAll();
    ArrayList<ControlPointsValues>getBySensorType(String sensorType);
    ControlPointsValues getControlPointsValues(int id);
    int add(ControlPointsValues controlPointsValues);
    boolean set(ControlPointsValues cpv);
    boolean changeSensorType(String oldSensorType, String newSensorType);
    boolean remove(ControlPointsValues controlPointsValues);
    boolean removeAll(String sensorType);
    boolean clear();
    boolean resetToDefault();
}
