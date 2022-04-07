package service;

import model.Measurement;

import java.util.ArrayList;

public interface MeasurementService {
    void init();
    void add(Measurement measurement);
    String[]getAllNames();
    String[]getAllValues();

    /**
     * @param measurement for which you want to find the values
     * @return array of values or null if measurement == null
     *
     * @see service.impl.MeasurementServiceImpl#getValues(Measurement)
     * @see repository.MeasurementRepository#getValues(Measurement)
     * @see repository.impl.MeasurementRepositoryImpl#getValues(Measurement)
     */
    String[]getValues(Measurement measurement);

    /**
     * @param name of measurement for which you want to find the values
     * @return array of values or null if name == null | name.length() == 0
     *
     * @see service.impl.MeasurementServiceImpl#getValues(String)
     * @see repository.MeasurementRepository#getValues(String)
     * @see repository.impl.MeasurementRepositoryImpl#getValues(String)
     */
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
