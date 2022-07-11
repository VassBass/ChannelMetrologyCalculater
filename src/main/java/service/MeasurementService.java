package service;

import model.Measurement;

import java.util.List;
import java.util.Map;

public interface MeasurementService {
    boolean add(Measurement measurement);
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

    List<Measurement> getAll();

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
    boolean delete(Measurement measurement);

    boolean changeFactors(String measurementValue, Map<String,Double> factors);
    boolean change(Measurement oldMeasurement, Measurement newMeasurement);

    boolean clear();

    /**
     *
     * @param name of measurement
     * @return measurements values with input name or null if @name == null | @name.length == 0
     */
    List<Measurement>getMeasurements(String name);

    boolean resetToDefault();

    boolean isLastInMeasurement(String measurementValue);
    boolean exists(String measurementValue);
    boolean exists(String oldValue, String newValue);
}
