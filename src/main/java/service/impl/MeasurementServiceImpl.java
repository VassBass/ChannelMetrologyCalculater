package service.impl;

import constants.MeasurementConstants;
import def.DefaultMeasurements;
import measurements.Measurement;
import model.Model;
import repository.Repository;
import service.FileBrowser;
import service.MeasurementService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MeasurementServiceImpl implements MeasurementService {
    private static final Logger LOGGER = Logger.getLogger(MeasurementService.class.getName());

    private ArrayList<Measurement> measurements;

    @Override
    public void init(){
        LOGGER.info("MeasurementService: initialization start ...");
        try {
            this.measurements = new Repository<Measurement>(null, Model.MEASUREMENT).readList();
        }catch (Exception e){
            LOGGER.info("MeasurementService: file \"" + FileBrowser.FILE_MEASUREMENTS.getName() + "\" is empty");
            LOGGER.info("MeasurementService: set default list");
            this.measurements = DefaultMeasurements.get();
            this.save();
        }
        LOGGER.info("MeasurementService: initialization SUCCESS");
    }

    @Override
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

    @Override
    public String[]getAllValues(){
        String[]values = new String[this.measurements.size()];
        for (int m=0;m<this.measurements.size();m++){
            values[m] = this.measurements.get(m).getValue();
        }
        return values;
    }

    @Override
    public String[]getValues(Measurement measurement){
        ArrayList<String> values  = new ArrayList<>();
        for (Measurement m : this.measurements) {
            if (m.getName().equals(measurement.getName())) {
                values.add(m.getValue());
            }
        }
        return values.toArray(new String[0]);
    }

    @Override
    public String[]getValues(MeasurementConstants name){
        ArrayList<String> values  = new ArrayList<>();
        for (Measurement measurement : this.measurements) {
            if (measurement.getNameConstant() == name) {
                values.add(measurement.getValue());
            }
        }
        return values.toArray(new String[0]);
    }

    @Override
    public String[]getValues(String name){
        ArrayList<String> values  = new ArrayList<>();
        for (Measurement measurement : this.measurements) {
            if (measurement.getName().equals(name)) {
                values.add(measurement.getValue());
            }
        }
        return values.toArray(new String[0]);
    }

    @Override
    public ArrayList<Measurement> getAll() {
        return this.measurements;
    }

    @Override
    public Measurement get(MeasurementConstants value){
        for (Measurement measurement : this.measurements){
            if (measurement.getValueConstant() == value){
                return measurement;
            }
        }
        return null;
    }

    @Override
    public Measurement get(String value){
        for (Measurement measurement : this.measurements){
            if (measurement.getValue().equals(value)){
                return measurement;
            }
        }
        return null;
    }

    @Override
    public Measurement get(int index) {
        if (index >= 0) {
            return this.measurements.get(index);
        }else {
            return null;
        }
    }

    @Override
    public ArrayList<Measurement>getMeasurements(MeasurementConstants name){
        ArrayList<Measurement>measurements = new ArrayList<>();
        for (Measurement measurement : this.measurements){
            if (measurement.getNameConstant() == name){
                measurements.add(measurement);
            }
        }
        return measurements;
    }

    @Override
    public ArrayList<Measurement>getMeasurements(String name){
        ArrayList<Measurement>measurements = new ArrayList<>();
        for (Measurement measurement : this.measurements){
            if (measurement.getName().equals(name)){
                measurements.add(measurement);
            }
        }
        return measurements;
    }

    @Override
    public void save() {
        new Repository<Measurement>(null, Model.MEASUREMENT).writeList(this.measurements);
    }
}
