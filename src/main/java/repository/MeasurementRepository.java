package repository;

import model.Measurement;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public interface MeasurementRepository extends Repository<Measurement>{
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(@Nonnull Measurement measurement);
    String[]getValues(@Nonnull String name);
    Measurement get(@Nonnull String value);
    Collection<Measurement> getMeasurements(@Nonnull String name);

    boolean changeFactors(@Nonnull String measurementValue, @Nonnull Map<String, Double> factors);

    boolean isLastInMeasurement(@Nonnull String measurementValue);
    boolean exists(@Nonnull String measurementValue);
    boolean exists(@Nonnull String oldValue, @Nonnull String newValue);
}
