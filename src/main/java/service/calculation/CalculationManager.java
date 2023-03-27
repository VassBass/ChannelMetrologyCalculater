package service.calculation;

import javax.annotation.Nonnull;

public interface CalculationManager {
    void registerConditionDialog(@Nonnull CalculationCollectDialog dialog);
    void showConditionDialog();

    void registerInputDialog(@Nonnull CalculationCollectDialog dialog);
    void showInputDialog();

    void registerResultDialog(@Nonnull CalculationCollectDialog dialog);
    void showResultDialog();

    void disposeCalculation();
}
