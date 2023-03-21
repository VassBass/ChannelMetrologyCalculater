package service.calculation.collect.condition.ui;

import java.util.List;

public interface CalculationControlConditionCalibratorPanel {
    void setCalibratorsNamesList(List<String> names);
    void setSelectedCalibrator(String calibratorName);
    String getSelectedCalibratorName();
}
