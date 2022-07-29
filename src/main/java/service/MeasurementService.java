package service;

import model.Measurement;
import repository.impl.MeasurementRepositorySQLite;

import java.util.Collection;
import java.util.Map;

public interface MeasurementService extends Service<Measurement> {
    String[]getAllNames();
    String[]getAllValues();

    /**
     * @param measurement for which you want to find the values
     * @return array of values or null if measurement == null
     *
     * @see service.impl.MeasurementServiceImpl#getValues(Measurement)
     * @see repository.MeasurementRepository#getValues(Measurement)
     * @see MeasurementRepositorySQLite#getValues(Measurement)
     */
    String[]getValues(Measurement measurement);

    /**
     * @param name of measurement for which you want to find the values
     * @return array of values or null if name == null | name.length() == 0
     *
     * @see service.impl.MeasurementServiceImpl#getValues(String)
     * @see repository.MeasurementRepository#getValues(String)
     * @see MeasurementRepositorySQLite#getValues(String)
     */
    String[]getValues(String name);

    /**
     *
     * @param value of measurement
     * @return null if Measurement with value not found or value == null
     */
    Measurement get(String value);

    boolean changeFactors(String measurementValue, Map<String,Double> factors);

    /**
     *
     * @param name of measurement
     * @return measurements values with input name or null if @name == null | @name.length == 0
     */
    Collection<Measurement> getMeasurements(String name);

    boolean resetToDefault();

    boolean isLastInMeasurement(String measurementValue);
    boolean exists(String measurementValue);
    boolean exists(String oldValue, String newValue);
}
