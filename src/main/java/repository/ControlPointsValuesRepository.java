package repository;

import model.ControlPointsValues;
import model.Sensor;

import java.util.ArrayList;

public interface ControlPointsValuesRepository {
    ArrayList<ControlPointsValues> getAll();
    ArrayList<ControlPointsValues>getBySensorType(String sensorType);
    double[] getValues(String sensorType, double rangeMin, double rangeMax);

    /**
     *
     * @param sensorType type of Sensor {@link Sensor#getType()}
     * @param index sequence number among control points with {@link Sensor#getType()}
     * @return null if ControlPointsValues not finds or if sensorType equals null
     */
    ControlPointsValues getControlPointsValues(String sensorType, int index);
    void put(ControlPointsValues cpv);
    void putInCurrentThread(ControlPointsValues cpv);
    void remove(ControlPointsValues cpv);
    void removeAllInCurrentThread(String sensorType);
    void clear(String sensorType);
    void resetToDefaultInCurrentThread();
    boolean backgroundTaskIsRun();
}
