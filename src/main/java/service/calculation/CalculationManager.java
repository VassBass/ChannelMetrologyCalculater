package service.calculation;

import service.calculation.condition.ui.swing.SwingCalculationControlConditionDialog;
import service.calculation.input.ui.swing.SwingCalculationInputDialog;
import service.calculation.persons.ui.swing.SwingCalculationPersonsDialog;
import service.calculation.result.ui.swing.SwingCalculationResultDialog;

import javax.annotation.Nonnull;

public interface CalculationManager {
    void registerConditionDialog(@Nonnull SwingCalculationControlConditionDialog dialog);
    void showConditionDialog();

    void registerInputDialog(@Nonnull SwingCalculationInputDialog dialog);
    void showInputDialog();

    void registerResultDialog(@Nonnull SwingCalculationResultDialog dialog);
    void showResultDialog();

    void registerPersonDialog(@Nonnull SwingCalculationPersonsDialog dialog);
    void showPersonDialog();

    void printProtocol();
    void openProtocol();
    void endCalculation();

    void disposeCalculation();
}
