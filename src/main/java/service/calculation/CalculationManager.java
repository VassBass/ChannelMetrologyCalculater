package service.calculation;

import service.calculation.collect.CalculationCollectDialog;

import javax.annotation.Nonnull;

public interface CalculationManager {
    void registerConditionDialog(@Nonnull CalculationCollectDialog dialog);
    void showConditionDialog();

    void registerInputDialog(@Nonnull CalculationCollectDialog dialog);
    void showInputDialog();

    void showResultDialog();

    void disposeCalculation();
}
