package service;

import model.Sensor;
import model.SensorValues;

public interface SensorsValuesService {
    void init();
    SensorValues getValues(Sensor sensor);
    void setValues(Sensor sensor, SensorValues values);
}
