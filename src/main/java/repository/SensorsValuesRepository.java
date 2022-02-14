package repository;

import model.Sensor;
import model.SensorValues;

public interface SensorsValuesRepository {
    void init();
    SensorValues getValues(Sensor sensor);
    void setValues(Sensor sensor, SensorValues values);
}
