package service.calibrator.info.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CalibratorInfoErrorFormulaPanel {
    @Nullable String getErrorFormula();
    void setErrorFormula(@Nonnull String value);
}
