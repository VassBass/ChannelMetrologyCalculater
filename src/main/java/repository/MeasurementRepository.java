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
    void add(Measurement measurement);
    void delete(Measurement measurement);
    void clear();
    ArrayList<Measurement>getMeasurements(String name);
    void rewriteInCurrentThread(ArrayList<Measurement>measurements);
}
