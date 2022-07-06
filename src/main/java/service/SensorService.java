package service;

import model.Sensor;

import java.util.ArrayList;

public interface SensorService {
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    ArrayList<Sensor> getAll();
    ArrayList<Sensor>getAll(String measurement);
    String getMeasurement(String sensorType);
    String[] getAllSensorsName(String measurementName);

    boolean add(Sensor sensor);

    boolean remove(Sensor sensor);
    boolean removeMeasurementValue(String measurementValue);

    boolean set(Sensor oldSensor, Sensor newSensor);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean rewrite(ArrayList<Sensor>sensors);
    boolean importData(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange);

    Sensor get(String sensorName);

    boolean clear();

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);

    boolean resetToDefault();
}
