package repository;

import model.Sensor;

import java.util.ArrayList;

public interface SensorRepository {
    ArrayList<Sensor>getAll();
    ArrayList<Sensor>getAll(String measurement);
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    String getMeasurement(String sensorType);
    String[] getAllSensorsName(String measurementName);
    Sensor get(String sensorName);
    Sensor get(int index);

    void add(Sensor sensor);
    void addInCurrentThread(Sensor sensor);

    void removeInCurrentThread(Sensor sensor);
    void removeMeasurementValueInCurrentThread(String measurementValue);

    void setInCurrentThread(Sensor oldSensor, Sensor newSensor);
    void changeMeasurementValueInCurrentThread(String oldValue, String newValue);

    void clear();

    void importDataInCurrentThread(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange);
    void rewrite(ArrayList<Sensor>sensors);
    void rewriteInCurrentThread(ArrayList<Sensor>sensors);

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);

    boolean backgroundTaskIsRun();
}
