package repository;

import model.Sensor;

import java.util.ArrayList;

public interface SensorRepository {
    ArrayList<Sensor>getAll();
    void add(Sensor sensor);
    void removeInCurrentThread(String sensorName);
    void setInCurrentThread(Sensor oldSensor, Sensor newSensor);
    void rewriteInCurrentThread(ArrayList<Sensor>sensors);
    void clear();
    void export(ArrayList<Sensor>sensors);
    void rewrite(ArrayList<Sensor>sensors);
}
