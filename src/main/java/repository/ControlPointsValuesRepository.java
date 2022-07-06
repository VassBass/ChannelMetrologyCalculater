package repository;

import model.ControlPointsValues;
import model.Sensor;

import java.util.ArrayList;

public interface ControlPointsValuesRepository {
    ArrayList<ControlPointsValues> getAll();
    ArrayList<ControlPointsValues>getBySensorType(String sensorType);
    ControlPointsValues getControlPointsValues(int id);
    int add(ControlPointsValues cpv);
    boolean set(ControlPointsValues cpv);
    boolean changeSensorType(String oldSensorType, String newSensorType);
    boolean remove(ControlPointsValues cpv);
    boolean removeAll(String sensorType);
    boolean clear();
    boolean resetToDefault();
}
