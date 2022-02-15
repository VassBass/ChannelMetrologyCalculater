package service;

import constants.MeasurementConstants;
import measurements.Measurement;

import java.util.ArrayList;

public interface MeasurementService {
    void init();
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(Measurement measurement);
    String[]getValues(MeasurementConstants name);
    String[]getValues(String name);
    ArrayList<Measurement> getAll();
    Measurement get(MeasurementConstants value);
    Measurement get(String value);
    Measurement get(int index);
    ArrayList<Measurement>getMeasurements(MeasurementConstants name);
    ArrayList<Measurement>getMeasurements(String name);
    void save();
}
