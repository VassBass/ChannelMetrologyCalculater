package repository;

import model.Sensor;

import java.util.List;

public interface SensorRepository extends Repository<Sensor>{
    List<Sensor>getAll(String measurement);
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    String getMeasurement(String sensorType);
    String[] getAllSensorsName(String measurementName);
    Sensor get(String sensorName);

    boolean removeMeasurementValue(String measurementValue);

    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean importData(List<Sensor>newSensors, List<Sensor>sensorsForChange);

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);
}
