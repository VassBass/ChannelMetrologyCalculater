package service.repository.repos.measurement_factor;

import model.dto.MeasurementTransformFactor;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;

public interface MeasurementFactorRepository {
    Collection<MeasurementTransformFactor> getAll();
    MeasurementTransformFactor getById(@Nonnegative int id);
    Collection<MeasurementTransformFactor> getBySource(@Nonnull String source);
    Collection<MeasurementTransformFactor> getByResult(@Nonnull String result);

    int add(@Nonnull String source, @Nonnull String result, double factor);

    boolean set(@Nonnull MeasurementTransformFactor mtf);
    boolean rewrite(@Nonnull Collection<MeasurementTransformFactor> mtf);
    boolean changeAllSources(String oldValue, String newValue);
    boolean changeAllResults(String oldValue, String newValue);
    boolean changeFactor(int id, double factor);

    boolean removeById(@Nonnegative int id);
    boolean removeBySource(@Nonnull String source);
    boolean removeByResult(@Nonnull String result);
}
