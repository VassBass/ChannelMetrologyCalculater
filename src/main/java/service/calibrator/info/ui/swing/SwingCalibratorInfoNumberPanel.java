package service.calibrator.info.ui.swing;

import model.ui.TitledTextField;
import service.calibrator.info.ui.CalibratorInfoNumberPanel;

public class SwingCalibratorInfoNumberPanel extends TitledTextField implements CalibratorInfoNumberPanel {
    private static final String TITLE_TEXT = "Серійний номер";
    private static final String TOOLTIP_TEXT = "Використовується у протоколі про перевірку МХ ВК";

    public SwingCalibratorInfoNumberPanel() {
        super(20, TITLE_TEXT);
        this.setToolTipText(TOOLTIP_TEXT);
    }

    @Override
    public String getNumber() {
        return this.getText();
    }

    @Override
    public void setNumber(String number) {
        this.setText(number);
    }
}
