package repository;

import model.Measurement;

import java.util.ArrayList;

public interface MeasurementRepository {
    ArrayList<Measurement>getAll();
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(Measurement measurement);
    String[]getValues(String name);
    Measurement get(String value);
    Measurement get(int index);
    ArrayList<Measurement>getMeasurements(String name);
    void rewriteInCurrentThread(ArrayList<Measurement>measurements);
}
