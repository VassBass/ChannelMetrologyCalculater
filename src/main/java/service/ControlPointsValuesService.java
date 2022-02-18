package service;

import model.ControlPointsValues;

import java.util.ArrayList;

public interface ControlPointsValuesService {
    void init();
    ArrayList<ControlPointsValues>getBySensorType(String sensorType);
    double[] getValues(String sensorType, double rangeMin, double rangeMax);
    ControlPointsValues getControlPointsValues(String sensorType, int index);
    void put(ControlPointsValues controlPointsValues);
    void remove(ControlPointsValues controlPointsValues);
    void removeAllInCurrentThread(String sensorType);
    void clear(String sensorType);
    void save();
}
