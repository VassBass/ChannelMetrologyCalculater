package repository;

import model.Measurement;

import java.util.Collection;
import java.util.Map;

public interface MeasurementRepository extends Repository<Measurement>{
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(Measurement measurement);
    String[]getValues(String name);
    Measurement get(String value);
    Collection<Measurement> getMeasurements(String name);

    boolean changeFactors(String measurementValue, Map<String, Double> factors);

    boolean isLastInMeasurement(String measurementValue);
    boolean exists(String measurementValue);
    boolean exists(String oldValue, String newValue);
}
