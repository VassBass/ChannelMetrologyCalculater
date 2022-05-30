package repository;

import model.Measurement;

import java.util.ArrayList;
import java.util.HashMap;

public interface MeasurementRepository {
    ArrayList<Measurement>getAll();
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(Measurement measurement);
    String[]getValues(String name);
    Measurement get(String value);
    ArrayList<Measurement>getMeasurements(String name);

    ArrayList<Measurement> addInCurrentThread(Measurement measurement);

    void rewriteInCurrentThread(ArrayList<Measurement>measurements);
    void changeFactors(String measurementValue, HashMap<String, Double>factors);
    void changeInCurrentThread(Measurement oldMeasurement, Measurement newMeasurement);

    void delete(Measurement measurement);
    void clear();

    boolean backgroundTaskIsRun();
    boolean isLastInMeasurement(String measurementValue);
    boolean exists(String measurementValue);
    boolean exists(String oldValue, String newValue);
}
