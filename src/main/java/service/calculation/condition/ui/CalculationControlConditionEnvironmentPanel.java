package service.calculation.condition.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CalculationControlConditionEnvironmentPanel {
    void setTemperature(@Nonnull String temperature);
    @Nullable String getTemperature();

    void setPressure(@Nonnull String pressure);
    @Nullable String getPressure();

    void setHumidity(@Nonnull String humidity);
    @Nullable String getHumidity();
}
