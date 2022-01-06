package controller;

import constants.MeasurementConstants;
import measurements.Consumption;
import measurements.Measurement;
import measurements.Pressure;
import measurements.Temperature;
import model.Model;
import repository.Repository;
import support.Comparator;

import java.util.ArrayList;

public class MeasurementsController {
    private ArrayList<Measurement> measurements;

    public void init(){
        try {
            this.measurements = new Repository<Measurement>(null, Model.MEASUREMENT).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_CHANNELS.getName() + "\" is empty");
            this.measurements = this.loadDefault();
        }
    }

    private ArrayList<Measurement> loadDefault(){
        ArrayList<Measurement>measurements = new ArrayList<>();

        measurements.add(new Temperature(MeasurementConstants.DEGREE_CELSIUS));

        measurements.add(new Pressure(MeasurementConstants.KPA));
        measurements.add(new Pressure(MeasurementConstants.PA));
        measurements.add(new Pressure(MeasurementConstants.MM_ACVA));
        measurements.add(new Pressure(MeasurementConstants.KG_SM2));
        measurements.add(new Pressure(MeasurementConstants.KG_MM2));
        measurements.add(new Pressure(MeasurementConstants.BAR));
        measurements.add(new Pressure(MeasurementConstants.ML_BAR));

        measurements.add(new Consumption(MeasurementConstants.M3_HOUR));

        this.save();
        return measurements;
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

    public ArrayList<Measurement> getAll() {
        return this.measurements;
    }

    public int getIndex(Measurement measurement) {
        for (int index=0;index<this.measurements.size();index++) {
            Measurement m = this.measurements.get(index);
            if (Comparator.measurementsMatch(measurement, m)) {
                return index;
            }
        }
        return -1;
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
