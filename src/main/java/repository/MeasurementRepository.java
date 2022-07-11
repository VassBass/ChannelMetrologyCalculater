package repository;

import model.Measurement;

import java.util.List;
import java.util.Map;

public interface MeasurementRepository {
    List<Measurement>getAll();
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(Measurement measurement);
    String[]getValues(String name);
    Measurement get(String value);
    List<Measurement>getMeasurements(String name);

    boolean add(Measurement measurement);

    boolean rewrite(List<Measurement>measurements);
    boolean changeFactors(String measurementValue, Map<String, Double> factors);
    boolean change(Measurement oldMeasurement, Measurement newMeasurement);

    boolean delete(Measurement measurement);
    boolean clear();

    boolean isLastInMeasurement(String measurementValue);
    boolean exists(String measurementValue);
    boolean exists(String oldValue, String newValue);
}
