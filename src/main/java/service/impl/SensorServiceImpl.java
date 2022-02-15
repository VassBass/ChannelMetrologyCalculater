package service.impl;

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

public class SensorServiceImpl implements SensorService {
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
        try {
            this.sensors = new Repository<Sensor>(null, Model.SENSOR).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_SENSORS.getName() + "\" is empty");
            this.sensors = DefaultSensors.get();
            this.save();
        }
        this.window = window;
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
    public ArrayList<Sensor> getAll() {
        return this.sensors;
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
        boolean exist = false;
        for (Sensor sen : this.sensors){
            if (sen.getName().equals(sensor.getName())){
                exist = true;
                break;
            }
        }
        if (exist){
            this.showExistMessage();
        }else {
            this.sensors.add(sensor);
            new Repository<Sensor>(null,Model.SENSOR).writeListInCurrentThread(this.sensors);
        }
        return this.sensors;
    }

    @Override
    public ArrayList<Sensor> remove(Sensor sensor) {
        boolean removed = false;

        for (Sensor sen : this.sensors){
            if (sen.getName().equals(sensor.getName())){
                this.sensors.remove(sen);
                removed = true;
                break;
            }
        }

        if (removed){
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.sensors;
    }

    @Override
    public ArrayList<Sensor> set(Sensor oldSensor, Sensor newSensor) {
        if (oldSensor != null){
            if (newSensor == null){
                this.remove(oldSensor);
            }else {
                for (int s=0;s<this.sensors.size();s++){
                    String sensorName = this.sensors.get(s).getName();
                    if (sensorName.equals(oldSensor.getName())){
                        this.sensors.set(s, newSensor);
                        break;
                    }
                }
            }
            new Repository<Sensor>(null,Model.SENSOR).writeListInCurrentThread(this.sensors);
        }
        return this.sensors;
    }

    @Override
    public void importData(ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange){
        for (Sensor sensor : sensorsForChange){
            for (int index=0;index<this.sensors.size();index++){
                if (sensor.getName().equals(this.sensors.get(index).getName())){
                    this.sensors.set(index, sensor);
                    break;
                }
            }
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
