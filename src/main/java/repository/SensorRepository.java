package repository;

import model.Sensor;

import java.util.List;

public interface SensorRepository {
    List<Sensor> getAll();
    List<Sensor>getAll(String measurement);
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

    boolean importData(List<Sensor>newSensors, List<Sensor>sensorsForChange);
    boolean rewrite(List<Sensor>sensors);

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);
}
