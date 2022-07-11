package service;

import model.Sensor;

import java.util.List;

public interface SensorService {
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    List<Sensor> getAll();
    List<Sensor>getAll(String measurement);
    String getMeasurement(String sensorType);
    String[] getAllSensorsName(String measurementName);

    boolean add(Sensor sensor);

    boolean remove(Sensor sensor);
    boolean removeMeasurementValue(String measurementValue);

    boolean set(Sensor oldSensor, Sensor newSensor);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean rewrite(List<Sensor>sensors);
    boolean importData(List<Sensor>newSensors, List<Sensor>sensorsForChange);

    Sensor get(String sensorName);

    boolean clear();

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);

    boolean resetToDefault();
}
