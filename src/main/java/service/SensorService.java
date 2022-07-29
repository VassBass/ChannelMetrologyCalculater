package service;

import model.Sensor;

import java.util.Collection;

public interface SensorService extends Service<Sensor> {
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    Collection<Sensor>getAll(String measurement);
    String getMeasurement(String sensorType);
    String[] getAllSensorsName(String measurementName);

    boolean removeMeasurementValue(String measurementValue);

    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean importData(Collection<Sensor> newSensors, Collection<Sensor>sensorsForChange);

    Sensor get(String sensorName);

    boolean isLastInMeasurement(Sensor sensor);
    boolean isExists(String sensorName);

    boolean resetToDefault();
}
