package service;

import model.Measurement;

import java.util.ArrayList;
import java.util.HashMap;

public interface MeasurementService {
    void init();
    void addInCurrentThread(Measurement measurement);
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

    /**
     *
     * @param value of measurement
     * @return null if Measurement with value not found or value == null
     */
    Measurement get(String value);

    /**
     * Remove measurement from list and DB
     * if measurement == null or measurement not exists in measurements list the method does nothing
     * @param measurement to delete
     */
    void delete(Measurement measurement);

    void changeFactors(String measurementValue, HashMap<String,Double>factors);
    void changeInCurrentThread(Measurement oldMeasurement, Measurement newMeasurement);

    void clear();

    /**
     *
     * @param name of measurement
     * @return measurements values with input name or null if @name == null | @name.length == 0
     */
    ArrayList<Measurement>getMeasurements(String name);

    void resetToDefaultInCurrentThread();

    boolean isLastInMeasurement(String measurementValue);
    boolean exists(String measurementValue);
    boolean exists(String oldValue, String newValue);
}
