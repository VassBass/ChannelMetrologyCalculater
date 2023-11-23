package service.calculation.condition.ui.swing;

import localization.label.Labels;
import model.ui.TitledComboBox;
import service.calculation.condition.ui.CalculationControlConditionCalibratorPanel;

import java.awt.*;
import java.util.List;

public class SwingCalculationControlConditionCalibratorPanel extends TitledComboBox implements CalculationControlConditionCalibratorPanel {
    public SwingCalculationControlConditionCalibratorPanel() {
        super(false, Labels.getInstance().calibrator, Color.BLACK);
    }

    @Override
    public void setCalibratorsNamesList(List<String> names) {
        this.setList(names);
    }

    @Override
    public String getSelectedCalibratorName() {
        return this.getSelectedString();
    }
}
