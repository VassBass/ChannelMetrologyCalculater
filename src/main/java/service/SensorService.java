package service;

import model.Sensor;

import java.util.ArrayList;

public interface SensorService {
    void init();
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    ArrayList<Sensor> getAll();
    String getMeasurement(String sensorType);
    String[] getAllSensorsName(String measurementName);

    ArrayList<Sensor> add(Sensor sensor);
    void addInCurrentThread(Sensor sensor);

    void removeInCurrentThread(Sensor sensor);

    void setInCurrentThread(Sensor oldSensor, Sensor newSensor);

    void rewriteInCurrentThread(ArrayList<Sensor>sensors);
    void importDataInCurrentThread(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange);

    Sensor get(String sensorName);
    Sensor get(int index);

    void clear();

    boolean isLastInMeasurement(Sensor sensor);

    void resetToDefaultInCurrentThread();

    boolean backgroundTaskIsRun();
}
