package service;

import model.ControlPointsValues;

import java.util.ArrayList;

public interface ControlPointsValuesService {
    void init();
    ArrayList<ControlPointsValues>getBySensorType(String sensorType);
    double[] getValues(String sensorType, double rangeMin, double rangeMax);
    void put(ControlPointsValues controlPointsValues);
    void remove(ControlPointsValues controlPointsValues);
    void removeAllInCurrentThread(String sensorType);
    void save();
}
