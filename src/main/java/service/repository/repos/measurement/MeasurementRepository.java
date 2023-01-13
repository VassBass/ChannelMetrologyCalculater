package service.repository.repos.measurement;

import model.Measurement;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public interface MeasurementRepository {
    Collection<Measurement> getAll();
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(@Nonnull Measurement measurement);
    String[]getValues(@Nonnull String name);
    Measurement get(@Nonnull String value);
    Collection<Measurement> getMeasurements(@Nonnull String name);

    boolean add(Measurement measurement);

    boolean set(@Nonnull Measurement oldMeasurement, @Nonnull Measurement newMeasurement);
    boolean changeFactors(@Nonnull String measurementValue, @Nonnull Map<String, Double> factors);
    boolean rewrite(@Nonnull Collection<Measurement> measurements);

    boolean remove(@Nonnull Measurement measurement);

    boolean clear();

    boolean isLastInMeasurement(@Nonnull String measurementValue);
    boolean exists(@Nonnull String measurementValue);
    boolean exists(@Nonnull String oldValue, @Nonnull String newValue);
}
