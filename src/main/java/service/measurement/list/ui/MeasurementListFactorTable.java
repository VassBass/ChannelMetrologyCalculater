package service.measurement.list.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public interface MeasurementListFactorTable {
    void setFactorList(@Nullable String sourceValue, @Nullable Map<String, Double> factorList);
}
