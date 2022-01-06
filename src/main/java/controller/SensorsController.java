package controller;

import constants.MeasurementConstants;
import constants.Strings;
import model.Model;
import model.Sensor;
import repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SensorsController {
    private Window window;
    private ArrayList<Sensor> sensors;

    public void init(Window window){
        this.window = window;
        try {
            this.sensors = new Repository<Sensor>(null, Model.SENSOR).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_SENSORS.getName() + "\" is empty");
            this.sensors = this.resetToDefault();
        }
    }

    public ArrayList<Sensor> resetToDefault() {
        if (this.sensors == null){
            this.sensors = new ArrayList<>();
        }else this.sensors.clear();

        Sensor tcm_50m = new Sensor();
        tcm_50m.setType("ТСМ-50М");
        tcm_50m.setName("ТСМ-50М");
        tcm_50m.setRange(-50D,180D);
        tcm_50m.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        tcm_50m.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tcm_50m.setErrorFormula("(0.005 * R) + 0.3");
        this.sensors.add(tcm_50m);

        Sensor tcp_100 = new Sensor();
        tcp_100.setType("ТОП  Pt 100");
        tcp_100.setName("ТОП  Pt 100");
        tcp_100.setRange(-50D,500D);
        tcp_100.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        tcp_100.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tcp_100.setErrorFormula("(0.005 * R) + 0.3");
        this.sensors.add(tcp_100);

        Sensor txa_2388_typeK = new Sensor();
        txa_2388_typeK.setType("Термопара TXA-2388 (тип К)");
        txa_2388_typeK.setName("Термопара TXA-2388 (тип К) < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK.setRange(-50D,1250D);
        txa_2388_typeK.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_2388_typeK.setErrorFormula("2.5");
        this.sensors.add(txa_2388_typeK);

        Sensor txa_2388_typeK_big = new Sensor();
        txa_2388_typeK_big.setType("Термопара TXA-2388 (тип К)");
        txa_2388_typeK_big.setName("Термопара TXA-2388 (тип К) > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK_big.setRange(-50D,1250D);
        txa_2388_typeK_big.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_2388_typeK_big.setErrorFormula("0.0075 * R");
        this.sensors.add(txa_2388_typeK_big);

        Sensor txa_0395_typeK = new Sensor();
        txa_0395_typeK.setType("Термопара TXA-0395 (тип К)");
        txa_0395_typeK.setName("Термопара TXA-0395 (тип К) < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK.setRange(-50D,1250D);
        txa_0395_typeK.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_0395_typeK.setErrorFormula("2.5");
        this.sensors.add(txa_0395_typeK);

        Sensor txa_0395_typeK_big = new Sensor();
        txa_0395_typeK_big.setType("Термопара TXA-0395 (тип К)");
        txa_0395_typeK_big.setName("Термопара TXA-0395 (тип К) > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK_big.setRange(-50D,1250D);
        txa_0395_typeK_big.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_0395_typeK_big.setErrorFormula("0.0075 * R");
        this.sensors.add(txa_0395_typeK_big);

        Sensor tp0198_2 = new Sensor();
        tp0198_2.setType("ТП 0198/2");
        tp0198_2.setName("ТП 0198/2 < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2.setRange(-40D,1100D);
        tp0198_2.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tp0198_2.setErrorFormula("2.5");
        this.sensors.add(tp0198_2);

        Sensor tp0198_2_big = new Sensor();
        tp0198_2_big.setType("ТП 0198/2");
        tp0198_2_big.setName("ТП 0198/2 > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2_big.setRange(-40D,1100D);
        tp0198_2_big.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tp0198_2_big.setErrorFormula("0.0075 * R");
        this.sensors.add(tp0198_2_big);

        Sensor deltabarS = new Sensor();
        deltabarS.setType("Deltabar S");
        deltabarS.setName("Deltabar S");
        deltabarS.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        deltabarS.setErrorFormula("(convR / 100) * 0.075");
        this.sensors.add(deltabarS);

        Sensor fisherRosemount3051s = new Sensor();
        fisherRosemount3051s.setType("Fisher-Rosemount 3051S");
        fisherRosemount3051s.setName("Fisher-Rosemount 3051S");
        fisherRosemount3051s.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fisherRosemount3051s.setErrorFormula("(convR / 100) * 0.055");
        this.sensors.add(fisherRosemount3051s);

        Sensor yokogawa = new Sensor();
        yokogawa.setType("Yokogawa");
        yokogawa.setName("Yokogawa");
        yokogawa.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        yokogawa.setErrorFormula("(convR / 100) * 0.2");
        this.sensors.add(yokogawa);

        Sensor jumoDTransP02 = new Sensor();
        jumoDTransP02.setType("JUMO dTRANS p02");
        jumoDTransP02.setName("JUMO dTRANS p02");
        jumoDTransP02.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        jumoDTransP02.setErrorFormula("(convR / 100) * 0.1");
        this.sensors.add(jumoDTransP02);

        Sensor yokogawa_axf050g = new Sensor();
        yokogawa_axf050g.setType("YOKOGAWA AXF050G");
        yokogawa_axf050g.setName("YOKOGAWA AXF050G");
        yokogawa_axf050g.setMeasurement(MeasurementConstants.CONSUMPTION.getValue());
        yokogawa_axf050g.setErrorFormula("(R / 100) * 0.35");
        this.sensors.add(yokogawa_axf050g);

        Sensor rosemount_8750 = new Sensor();
        rosemount_8750.setType("ROSEMOUNT 8750");
        rosemount_8750.setName("ROSEMOUNT 8750");
        rosemount_8750.setMeasurement(MeasurementConstants.CONSUMPTION.getValue());
        rosemount_8750.setErrorFormula("(R / 100) * 0.5");
        this.sensors.add(rosemount_8750);

        this.save();
        return this.sensors;
    }

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

    public ArrayList<Sensor> getAll() {
        return this.sensors;
    }

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
            this.save();
        }
        return this.sensors;
    }

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
            this.save();
        }
        return this.sensors;
    }

    public int getIndex(String sensorName) {
        for (int index=0;index<this.sensors.size();index++) {
            Sensor sensor = this.sensors.get(index);
            if (sensor.getName().equals(sensorName)) {
                return index;
            }
        }
        this.showNotFoundMessage();
        return -1;
    }

    public Sensor get(String sensorName) {
        for (Sensor sensor : this.sensors) {
            if (sensor.getName().equals(sensorName)) {
                return sensor;
            }
        }
        this.showNotFoundMessage();
        return null;
    }

    public Sensor get(int index) {
        if (index >= 0) {
            return this.sensors.get(index);
        }else {
            return null;
        }
    }

    public void clear() {
        this.sensors.clear();
        this.save();
    }

    private void save() {
        new Repository<Sensor>(this.window, Model.SENSOR).writeList(this.sensors);
    }

    private void showNotFoundMessage() {
        String message = "ПВП з данною назвою не знайдено в списку ПВП.";
        JOptionPane.showMessageDialog(this.window, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }

    private void showExistMessage() {
        String message = "ПВП з данною назвою вже існує в списку ПВП. Змініть будь ласка назву.";
        JOptionPane.showMessageDialog(this.window, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
