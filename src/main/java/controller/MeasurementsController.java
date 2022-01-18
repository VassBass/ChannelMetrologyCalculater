package controller;

import constants.MeasurementConstants;
import measurements.Consumption;
import measurements.Measurement;
import measurements.Pressure;
import measurements.Temperature;
import model.Model;
import repository.Repository;

import java.util.ArrayList;

public class MeasurementsController {
    private ArrayList<Measurement> measurements;

    public void init(){
        try {
            this.measurements = new Repository<Measurement>(null, Model.MEASUREMENT).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_MEASUREMENTS.getName() + "\" is empty");
            this.measurements = this.loadDefault();
        }
    }

    private ArrayList<Measurement> loadDefault(){
        if (this.measurements == null){
            this.measurements = new ArrayList<>();
        }else this.measurements.clear();

        this.measurements.add(new Temperature(MeasurementConstants.DEGREE_CELSIUS));

        this.measurements.add(new Pressure(MeasurementConstants.KPA));
        this.measurements.add(new Pressure(MeasurementConstants.PA));
        this.measurements.add(new Pressure(MeasurementConstants.MM_ACVA));
        this.measurements.add(new Pressure(MeasurementConstants.KG_SM2));
        this.measurements.add(new Pressure(MeasurementConstants.KG_MM2));
        this.measurements.add(new Pressure(MeasurementConstants.BAR));
        this.measurements.add(new Pressure(MeasurementConstants.ML_BAR));

        this.measurements.add(new Consumption(MeasurementConstants.M3_HOUR));

        this.save();
        return this.measurements;
    }

    public String[]getAllNames(){
        ArrayList<String>names = new ArrayList<>();
        for (Measurement measurement : this.measurements){
            String name = measurement.getName();
            boolean exist = false;
            for (String n : names){
                if (n.equals(name)) {
                    exist = true;
                    break;
                }
            }
            if (!exist){
                names.add(name);
            }
        }
        return names.toArray(new String[0]);
    }

    public String[]getAllValues(){
        String[]values = new String[this.measurements.size()];
        for (int m=0;m<this.measurements.size();m++){
            values[m] = this.measurements.get(m).getValue();
        }
        return values;
    }

    public String[]getValues(Measurement measurement){
        ArrayList<String> values  = new ArrayList<>();
        for (Measurement m : this.measurements) {
            if (m.getName().equals(measurement.getName())) {
                values.add(m.getValue());
            }
        }
        return values.toArray(new String[0]);
    }

    public String[]getValues(MeasurementConstants measurementName){
        ArrayList<String> values  = new ArrayList<>();
        for (Measurement measurement : this.measurements) {
            if (measurement.getNameConstant() == measurementName) {
                values.add(measurement.getValue());
            }
        }
        return values.toArray(new String[0]);
    }

    public String[]getValues(String measurementName){
        ArrayList<String> values  = new ArrayList<>();
        for (Measurement measurement : this.measurements) {
            if (measurement.getName().equals(measurementName)) {
                values.add(measurement.getValue());
            }
        }
        return values.toArray(new String[0]);
    }

    public ArrayList<Measurement> getAll() {
        return this.measurements;
    }

    public Measurement get(MeasurementConstants measurementValue){
        for (Measurement measurement : this.measurements){
            if (measurement.getValueConstant() == measurementValue){
                return measurement;
            }
        }
        return null;
    }

    public Measurement get(String measurementValue){
        for (Measurement measurement : this.measurements){
            if (measurement.getValue().equals(measurementValue)){
                return measurement;
            }
        }
        return null;
    }

    public Measurement get(int index) {
        if (index >= 0) {
            return this.measurements.get(index);
        }else {
            return null;
        }
    }

    public ArrayList<Measurement>getMeasurements(MeasurementConstants measurementName){
        ArrayList<Measurement>measurements = new ArrayList<>();
        for (Measurement measurement : this.measurements){
            if (measurement.getNameConstant() == measurementName){
                measurements.add(measurement);
            }
        }
        return measurements;
    }

    public ArrayList<Measurement>getMeasurements(String measurementName){
        ArrayList<Measurement>measurements = new ArrayList<>();
        for (Measurement measurement : this.measurements){
            if (measurement.getName().equals(measurementName)){
                measurements.add(measurement);
            }
        }
        return measurements;
    }

    private void save() {
        new Repository<Measurement>(null, Model.MEASUREMENT).writeList(this.measurements);
    }
}
