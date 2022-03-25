package service;

import constants.MeasurementConstants;
import model.Measurement;

import java.util.ArrayList;

public interface MeasurementService {
    void init();
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(Measurement measurement);
    String[]getValues(String name);
    ArrayList<Measurement> getAll();
    Measurement get(String value);
    Measurement get(int index);
    ArrayList<Measurement>getMeasurements(String name);
    void resetToDefault();
}
