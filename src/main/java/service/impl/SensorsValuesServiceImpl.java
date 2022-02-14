package service.impl;

import model.Sensor;
import model.SensorValues;
import repository.SensorsValuesRepository;
import repository.impl.SensorsValuesRepositoryImpl;
import service.SensorsValuesService;

public class SensorsValuesServiceImpl implements SensorsValuesService {
    private final SensorsValuesRepository repository = new SensorsValuesRepositoryImpl();

    @Override
    public void init() {
        this.repository.init();
    }

    @Override
    public SensorValues getValues(Sensor sensor) {
        return this.repository.getValues(sensor);
    }

    @Override
    public void setValues(Sensor sensor, SensorValues values) {
        this.repository.setValues(sensor,values);
    }
}
