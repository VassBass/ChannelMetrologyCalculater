package service.impl;

import def.DefaultSensors;
import model.Sensor;
import repository.SensorRepository;
import repository.impl.SensorRepositoryImpl;
import service.SensorService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SensorServiceImpl implements SensorService {
    private static final Logger LOGGER = Logger.getLogger(SensorService.class.getName());

    private final String dbUrl;
    private SensorRepository repository;

    public SensorServiceImpl(){
        this.dbUrl = null;
    }

    public SensorServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = this.dbUrl == null ? new SensorRepositoryImpl() : new SensorRepositoryImpl(this.dbUrl);
        LOGGER.info("Initialization SUCCESS");
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
    public ArrayList<Sensor> getAll() {
        return this.repository.getAll();
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
    public ArrayList<Sensor> add(Sensor sensor) {
        this.repository.add(sensor);
        return this.repository.getAll();
    }

    @Override
    public void addInCurrentThread(Sensor sensor) {
        this.repository.addInCurrentThread(sensor);
    }

    @Override
    public void removeInCurrentThread(Sensor sensor) {
        this.repository.removeInCurrentThread(sensor);
    }

    @Override
    public void setInCurrentThread(Sensor oldSensor, Sensor newSensor) {
        this.repository.setInCurrentThread(oldSensor, newSensor);
    }

    @Override
    public void importDataInCurrentThread(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange){
        this.repository.importDataInCurrentThread(newSensors, sensorsForChange);
    }

    @Override
    public Sensor get(String sensorName) {
        return this.repository.get(sensorName);
    }

    @Override
    public Sensor get(int index) {
        return this.repository.get(index);
    }

    @Override
    public void clear() {
        this.repository.clear();
    }

    @Override
    public boolean isLastInMeasurement(Sensor sensor){
        return this.repository.isLastInMeasurement(sensor);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Sensor>sensors){
        this.repository.rewriteInCurrentThread(sensors);
    }

    @Override
    public void resetToDefaultInCurrentThread() {
        this.repository.rewriteInCurrentThread(DefaultSensors.get());
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.repository.backgroundTaskIsRun();
    }
}