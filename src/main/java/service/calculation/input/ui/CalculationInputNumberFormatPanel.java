package service.calculation.input.ui;

import javax.annotation.Nonnegative;

public interface CalculationInputNumberFormatPanel {
    @Nonnegative int getValueDecimalPoint();
    @Nonnegative int getPercentDecimalPoint();
}
