package service.impl;

import def.DefaultSensors;
import model.Sensor;
import repository.SensorRepository;
import repository.impl.SensorRepositorySQLite;
import service.SensorService;

import java.util.List;

public class SensorServiceImpl implements SensorService {
    private final SensorRepository repository;

    public SensorServiceImpl(){
        this.repository = new SensorRepositorySQLite();
    }

    public SensorServiceImpl(SensorRepository repository){
        this.repository = repository;
    }

    @Override
    public String[]getAllTypes(){
        return this.repository.getAllTypes();
    }

    @Override
    public String[] getAllTypesWithoutROSEMOUNT() {
        return this.repository.getAllTypesWithoutROSEMOUNT();
    }

    @Override
    public List<Sensor> getAll() {
        return this.repository.getAll();
    }

    @Override
    public List<Sensor> getAll(String measurement) {
        return this.repository.getAll(measurement);
    }

    @Override
    public String getMeasurement(String sensorType) {
        return this.repository.getMeasurement(sensorType);
    }

    @Override
    public String[] getAllSensorsName(String measurementName){
        return this.repository.getAllSensorsName(measurementName);
    }

    @Override
    public boolean add(Sensor sensor) {
        return this.repository.add(sensor);
    }

    @Override
    public boolean remove(Sensor sensor) {
        return this.repository.remove(sensor);
    }

    @Override
    public boolean removeMeasurementValue(String measurementValue) {
        return this.repository.removeMeasurementValue(measurementValue);
    }

    @Override
    public boolean set(Sensor oldSensor, Sensor newSensor) {
        return this.repository.set(oldSensor, newSensor);
    }

    @Override
    public boolean changeMeasurementValue(String oldValue, String newValue) {
        return this.repository.changeMeasurementValue(oldValue, newValue);
    }

    @Override
    public boolean importData(List<Sensor>newSensors, List<Sensor>sensorsForChange){
        return this.repository.importData(newSensors, sensorsForChange);
    }

    @Override
    public Sensor get(String sensorName) {
        return this.repository.get(sensorName);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean isLastInMeasurement(Sensor sensor){
        return this.repository.isLastInMeasurement(sensor);
    }

    @Override
    public boolean isExists(String sensorName) {
        return this.repository.isExists(sensorName);
    }

    @Override
    public boolean rewrite(List<Sensor>sensors){
        return this.repository.rewrite(sensors);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultSensors.get());
    }

}