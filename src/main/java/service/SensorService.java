package service;

import model.Sensor;

import java.awt.*;
import java.util.ArrayList;

public interface SensorService {
    void init(Window window);
    String[]getAllTypes();
    ArrayList<Sensor> getAll();
    String[] getAllSensorsName(String measurementName);
    ArrayList<Sensor> add(Sensor sensor);
    ArrayList<Sensor> remove(Sensor sensor);
    ArrayList<Sensor> set(Sensor oldSensor, Sensor newSensor);
    void importData(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange);
    Sensor get(String sensorName);
    Sensor get(int index);
    void clear();
    boolean isLastInMeasurement(Sensor sensor);
    void rewriteInCurrentThread(ArrayList<Sensor>sensors);
    void save();
    boolean exportData();
}
