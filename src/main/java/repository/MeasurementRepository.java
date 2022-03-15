package repository;

import constants.MeasurementConstants;
import measurements.Measurement;

import java.util.ArrayList;

public interface MeasurementRepository {
    ArrayList<Measurement>getAll();
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(Measurement measurement);
    String[]getValues(MeasurementConstants name);
    String[]getValues(String name);
    Measurement get(MeasurementConstants value);
    Measurement get(String value);
    Measurement get(int index);
    ArrayList<Measurement>getMeasurements(MeasurementConstants name);
    ArrayList<Measurement>getMeasurements(String name);
    void rewriteInCurrentThread(ArrayList<Measurement>measurements);
}
