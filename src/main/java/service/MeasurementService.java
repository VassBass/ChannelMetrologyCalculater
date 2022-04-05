package service;

import model.Measurement;

import java.util.ArrayList;

public interface MeasurementService {
    void init();
    void add(Measurement measurement);
    String[]getAllNames();
    String[]getAllValues();

    //return @null if @measurement == null
    String[]getValues(Measurement measurement);

    //return @null if @name == null | @name.length() == 0
    String[]getValues(String name);

    ArrayList<Measurement> getAll();

    // return @null if Measurement with @value not found or @value == null
    Measurement get(String value);

    //return @null if @index < 0 | @index >= measurements count
    Measurement get(int index);

    //if @measurement == null or @measurement not exists in measurements list the method does nothing
    void delete(Measurement measurement);

    void clear();

    //return null if @name == null | @name.length == 0
    ArrayList<Measurement>getMeasurements(String name);

    void resetToDefaultInCurrentThread();
}
