package service.calculation.collect.condition.ui.swing;

import model.ui.TitledComboBox;
import service.calculation.collect.condition.ui.CalculationControlConditionCalibratorPanel;

import java.awt.*;
import java.util.List;

public class SwingCalculationControlConditionCalibratorPanel extends TitledComboBox implements CalculationControlConditionCalibratorPanel {
    private static final String TITLE = "Калібратор";

    public SwingCalculationControlConditionCalibratorPanel() {
        super(false, TITLE, Color.BLACK);
    }

    @Override
    public void setCalibratorsNamesList(List<String> names) {
        this.setList(names);
    }

    @Override
    public void setSelectedCalibrator(String calibratorName) {
        this.setSelectedItem(calibratorName);
    }

    @Override
    public String getSelectedCalibratorName() {
        return this.getSelectedItem();
    }
}
