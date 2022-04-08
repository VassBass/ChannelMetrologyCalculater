package repository;

import model.Sensor;

import java.util.ArrayList;

public interface SensorRepository {
    ArrayList<Sensor>getAll();
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    String getMeasurement(String sensorType);
    String[] getAllSensorsName(String measurementName);
    Sensor get(String sensorName);
    Sensor get(int index);

    void add(Sensor sensor);
    void addInCurrentThread(Sensor sensor);

    void removeInCurrentThread(Sensor sensor);

    void setInCurrentThread(Sensor oldSensor, Sensor newSensor);

    void clear();

    void importDataInCurrentThread(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange);
    void rewrite(ArrayList<Sensor>sensors);
    void rewriteInCurrentThread(ArrayList<Sensor>sensors);

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);

    boolean backgroundTaskIsRun();
}
