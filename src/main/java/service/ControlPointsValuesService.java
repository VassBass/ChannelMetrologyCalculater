package service;

import model.ControlPointsValues;
import model.Sensor;

import java.util.ArrayList;

public interface ControlPointsValuesService {
    void init();
    ArrayList<ControlPointsValues>getAll();
    ArrayList<ControlPointsValues>getBySensorType(String sensorType);
    double[] getValues(String sensorType, double rangeMin, double rangeMax);

    /**
     *
     * @param sensorType type of Sensor {@link Sensor#getType()}
     * @param index sequence number among control points with {@link Sensor#getType()}
     * @return null if ControlPointsValues not finds or if sensorType equals null
     */
    ControlPointsValues getControlPointsValues(String sensorType, int index);
    void put(ControlPointsValues controlPointsValues);
    void putInCurrentThread(ControlPointsValues cpv);
    void changeSensorTypeInCurrentThread(String oldSensorType, String newSensorType);
    void remove(ControlPointsValues controlPointsValues);
    void removeAllInCurrentThread(String sensorType);
    void clear(String sensorType);
    void resetToDefaultInCurrentThread();
    boolean backgroundTaskIsRun();
}
