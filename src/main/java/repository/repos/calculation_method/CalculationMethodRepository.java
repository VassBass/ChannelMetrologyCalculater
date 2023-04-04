package repository.repos.calculation_method;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public interface CalculationMethodRepository {
    Map<String, String> getAll();
    @Nullable String getMethodNameByMeasurementName(@Nonnull String measurementName);

    boolean set(@Nonnull String measurementName, @Nonnull String methodName);

    boolean add(@Nonnull String measurementName, @Nonnull String methodName);

    boolean removeByMeasurementName(@Nonnull String measurementName);
}
