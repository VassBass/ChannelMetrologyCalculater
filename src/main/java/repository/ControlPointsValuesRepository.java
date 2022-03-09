package repository;

import model.ControlPointsValues;

import java.util.ArrayList;

public interface ControlPointsValuesRepository {
    ArrayList<ControlPointsValues> getAll();
    void put(ControlPointsValues cpv);
    void remove(int id);
    void removeAllInCurrentThread(String sensorType);
    void clear(String sensorType);
    void resetToDefault();
}
