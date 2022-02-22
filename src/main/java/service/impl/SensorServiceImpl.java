package service.impl;

import application.Application;
import constants.SensorType;
import def.DefaultSensors;
import model.Model;
import model.Sensor;
import repository.Repository;
import service.FileBrowser;
import service.SensorService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class SensorServiceImpl implements SensorService {
    private static final Logger LOGGER = Logger.getLogger(SensorService.class.getName());

    private static final String ERROR = "Помилка";

    private Window window;
    private ArrayList<Sensor> sensors;

    private String exportFileName(Calendar date){
        return "export_sensors ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].sen";
    }

    @Override
    public void init(Window window){
        LOGGER.info("SensorService: initialization start ...");
        try {
            this.sensors = new Repository<Sensor>(null, Model.SENSOR).readList();
        }catch (Exception e){
            LOGGER.info("SensorService: file \"" + FileBrowser.FILE_SENSORS.getName() + "\" is empty");
            LOGGER.info("SensorService: set default list");
            this.sensors = DefaultSensors.get();
            this.save();
        }
        this.window = window;
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
            new Repository<Sensor>(null,Model.SENSOR).writeListInCurrentThread(this.sensors);
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
            new Repository<Sensor>(this.window, Model.SENSOR).writeListInCurrentThread(this.sensors);
        }
    }

    @Override
    public void setInCurrentThread(Sensor oldSensor, Sensor newSensor) {
        if (oldSensor != null) {
            int index = this.sensors.indexOf(oldSensor);
            if (index >= 0) {
                if (newSensor == null) {
                    this.sensors.remove(index);
                } else {
                    this.sensors.set(index, newSensor);
                }
                new Repository<Sensor>(null, Model.SENSOR).writeListInCurrentThread(this.sensors);
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
        new Repository<Sensor>(null,Model.SENSOR).writeListInCurrentThread(this.sensors);
    }

    @Override
    public Sensor get(String sensorName) {
        for (Sensor sensor : this.sensors) {
            if (sensor.getName().equals(sensorName)) {
                return sensor;
            }
        }
        this.showNotFoundMessage();
        return null;
    }

    @Override
    public Sensor get(int index) {
        if (index >= 0) {
            return this.sensors.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.sensors.clear();
        this.save();
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
        new Repository<Sensor>(null, Model.SENSOR).writeListInCurrentThread(sensors);
    }

    @Override
    public void save() {
        new Repository<Sensor>(this.window, Model.SENSOR).writeList(this.sensors);
    }

    @Override
    public boolean exportData(){
        try {
            String fileName = this.exportFileName(Calendar.getInstance());
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), this.sensors);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    private void showNotFoundMessage() {
        String message = "ПВП з данною назвою не знайдено в списку ПВП.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    private void showExistMessage() {
        String message = "ПВП з данною назвою вже існує в списку ПВП. Змініть будь ласка назву.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}