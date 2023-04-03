package repository.repos.calculation_method;


import javax.annotation.Nullable;
import java.util.Map;

public interface CalculationMethodRepository {
    Map<String, String> getAll();
    @Nullable String getMethodNameByMeasurementName(String measurementName);

    boolean set(String measurementName, String methodName);

    boolean add(String measurementName, String methodName);

    boolean removeByMeasurementName(String measurementName);
}
