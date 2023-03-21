package service.calculation.collect.condition.ui;

import model.ui.UI;
import service.calculation.CalculationValue;

import javax.annotation.Nullable;
import java.util.Map;

public interface CalculationControlConditionDialog extends UI {
    @Nullable Map<CalculationValue, String> getControlConditionValues();
}
