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

    boolean add(Sensor sensor);

    boolean remove(Sensor sensor);
    boolean removeMeasurementValue(String measurementValue);

    boolean set(Sensor oldSensor, Sensor newSensor);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean clear();

    boolean importData(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange);
    boolean rewrite(ArrayList<Sensor>sensors);

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);
}
