package repository;

import model.ControlPointsValues;

import java.util.ArrayList;

public interface ControlPointsValuesRepository {
    ArrayList<ControlPointsValues> getAll();
    ArrayList<ControlPointsValues>getBySensorType(String sensorType);
    double[] getValues(String sensorType, double rangeMin, double rangeMax);
    ControlPointsValues getControlPointsValues(String sensorType, int index);
    void put(ControlPointsValues cpv);
    void remove(ControlPointsValues cpv);
    void removeAllInCurrentThread(String sensorType);
    void clear(String sensorType);
    void resetToDefault();
}
