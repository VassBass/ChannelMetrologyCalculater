package service.impl;

import application.Application;
import constants.SensorType;
import def.DefaultSensors;
import model.Sensor;
import repository.SensorRepository;
import repository.impl.SensorRepositoryImpl;
import service.SensorService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SensorServiceImpl implements SensorService {
    private static final Logger LOGGER = Logger.getLogger(SensorService.class.getName());

    private static final String ERROR = "Помилка";

    private final SensorRepository repository;
    private ArrayList<Sensor> sensors;

    public SensorServiceImpl(){
        this.repository = new SensorRepositoryImpl();
    }

    public SensorServiceImpl(String dbUrl){
        this.repository = new SensorRepositoryImpl(dbUrl);
    }

    @Override
    public void init(){
        this.sensors = this.repository.getAll();
        LOGGER.info("SensorService: initialization SUCCESS");
    }

    @Override
    public String[]getAllTypes(){
        ArrayList<String>types = new ArrayList<>();
        for (Sensor sensor : this.sensors){
            String type = sensor.getType();
            boolean exist = false;
            for (String t : types){
                if (t.equals(type)){
                    exist = true;
                    break;
                }
            }
            if (!exist){
                types.add(type);
            }
        }
        return types.toArray(new String[0]);
    }

    @Override
    public String[] getAllTypesWithoutROSEMOUNT() {
        ArrayList<String>types = new ArrayList<>();
        for (Sensor sensor : this.sensors){
            String type = sensor.getType();
            if (!type.contains(SensorType.ROSEMOUNT)) {
                boolean exist = false;
                for (String t : types) {
                    if (t.equals(type)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    types.add(type);
                }
            }
        }
        return types.toArray(new String[0]);    }

    @Override
    public ArrayList<Sensor> getAll() {
        return this.sensors;
    }

    @Override
    public String getMeasurement(String sensorType) {
        for (Sensor sensor : this.sensors){
            if (sensor.getType().equals(sensorType)){
                return sensor.getMeasurement();
            }
        }
        return null;
    }

    @Override
    public String[] getAllSensorsName(String measurementName){
        ArrayList<String> names = new ArrayList<>();
        for (Sensor sensor : this.sensors) {
            if (sensor.getMeasurement().equals(measurementName)) {
                names.add(sensor.getName());
            }
        }
        return names.toArray(new String[0]);
    }

    @Override
    public ArrayList<Sensor> add(Sensor sensor) {
        if (this.sensors.contains(sensor)){
            this.showExistMessage();
        }else {
            this.sensors.add(sensor);
            this.repository.add(sensor);
        }
        return this.sensors;
    }

    @Override
    public void removeInCurrentThread(Sensor sensor) {
        int index = this.sensors.indexOf(sensor);
        if (index >= 0){
            Sensor sen = this.sensors.get(index);
            int numByType = -1;
            for (Sensor s : this.sensors){
                if (s.getType().equals(sen.getType())) ++numByType;
            }
            if (numByType <= 0) Application.context.controlPointsValuesService.removeAllInCurrentThread(sen.getType());

            this.sensors.remove(index);
            this.repository.removeInCurrentThread(sensor.getName());
        }
    }

    @Override
    public void setInCurrentThread(Sensor oldSensor, Sensor newSensor) {
        if (oldSensor != null || newSensor != null) {
            int index = this.sensors.indexOf(oldSensor);
            if (index >= 0) {
                this.sensors.set(index, newSensor);
                this.repository.setInCurrentThread(oldSensor, newSensor);
            }
        }
    }

    @Override
    public void importData(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange){
        for (Sensor sensor : sensorsForChange){
            int index = this.sensors.indexOf(sensor);
            if (index >= 0) this.sensors.set(index, sensor);
        }
        this.sensors.addAll(newSensors);
        this.repository.rewriteInCurrentThread(this.sensors);
    }

    @Override
    public Sensor get(String sensorName) {
        Sensor sensor = new Sensor();
        sensor.setName(sensorName);
        int index = this.sensors.indexOf(sensor);
        return index < 0 ? null : this.sensors.get(index);
    }

    @Override
    public Sensor get(int index) {
        return index < 0 | index >= this.sensors.size() ? null : this.sensors.get(index);
    }

    @Override
    public void clear() {
        this.sensors.clear();
        this.repository.clear();
    }

    @Override
    public boolean isLastInMeasurement(Sensor sensor){
        String measurement = sensor.getMeasurement();
        int numberOfSensors = 0;
        for (Sensor s : this.sensors){
            if (s.getMeasurement().equals(measurement)){
                numberOfSensors++;
            }
        }
        return numberOfSensors <= 1;
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Sensor>sensors){
        this.sensors = sensors;
        this.repository.rewriteInCurrentThread(sensors);
    }

    @Override
    public void exportData(){
        this.repository.export(this.sensors);
    }

    @Override
    public void resetToDefault() {
        this.sensors = DefaultSensors.get();
        this.repository.rewrite(this.sensors);
    }

    private void showExistMessage() {
        if (Application.context != null) {
            String message = "ПВП з данною назвою вже існує в списку ПВП. Змініть будь ласка назву.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}