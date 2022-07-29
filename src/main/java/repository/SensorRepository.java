package repository;

import model.Sensor;

import java.util.Collection;

public interface SensorRepository extends Repository<Sensor>{
    Collection<Sensor> getAll(String measurement);
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    String getMeasurement(String sensorType);
    String[] getAllSensorsName(String measurementName);
    Sensor get(String sensorName);

    boolean removeMeasurementValue(String measurementValue);

    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean importData(Collection<Sensor>newSensors, Collection<Sensor>sensorsForChange);

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);
}
