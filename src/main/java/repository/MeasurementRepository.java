package repository;

import model.Measurement;

import java.util.ArrayList;
import java.util.HashMap;

public interface MeasurementRepository {
    void createTable();
    ArrayList<Measurement>getAll();
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(Measurement measurement);
    String[]getValues(String name);
    Measurement get(String value);
    ArrayList<Measurement>getMeasurements(String name);

    boolean add(Measurement measurement);

    boolean rewrite(ArrayList<Measurement>measurements);
    boolean changeFactors(String measurementValue, HashMap<String, Double>factors);
    boolean change(Measurement oldMeasurement, Measurement newMeasurement);

    boolean delete(Measurement measurement);
    boolean clear();

    boolean isLastInMeasurement(String measurementValue);
    boolean exists(String measurementValue);
    boolean exists(String oldValue, String newValue);
}
