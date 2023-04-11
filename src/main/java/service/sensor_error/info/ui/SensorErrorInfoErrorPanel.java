package service.sensor_error.info.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface SensorErrorInfoErrorPanel {
    void setErrorFormula(@Nonnull String errorFormula);
    @Nullable String getErrorFormula();
}
