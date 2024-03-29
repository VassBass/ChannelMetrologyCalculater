package repository.repos.measurement;

import model.dto.Measurement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface MeasurementRepository {
    Collection<Measurement> getAll();
    @Nullable Measurement getAnyNotEquals(Measurement measurement);
    String[]getAllNames();
    String[]getAllValues();
    String[]getValues(@Nonnull String name);
    @Nullable Measurement getByValue(@Nonnull String value);
    Collection<Measurement> getMeasurementsByName(@Nonnull String name);

    boolean add(Measurement measurement);

    boolean set(@Nonnull Measurement oldMeasurement, @Nonnull Measurement newMeasurement);
    boolean rewrite(@Nonnull Collection<Measurement> measurements);

    boolean remove(@Nonnull Measurement measurement);

    boolean clear();

    boolean isLastInMeasurement(@Nonnull String measurementValue);
    boolean exists(@Nonnull String measurementValue);
    boolean exists(@Nonnull String oldValue, @Nonnull String newValue);
}
