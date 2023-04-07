package service.calibrator.info.ui.swing;

import model.ui.TitledTextField;
import service.calibrator.info.ui.CalibratorInfoTypePanel;

public class SwingCalibratorInfoTypePanel extends TitledTextField implements CalibratorInfoTypePanel {
    private static final String TITLE_TEXT = "Тип калібратора";
    private static final String TOOLTIP_TEXT = "Використовується у протоколі про перевірку МХ ВК";

    public SwingCalibratorInfoTypePanel() {
        super(20, TITLE_TEXT);
        this.setToolTipText(TOOLTIP_TEXT);
    }

    @Override
    public String getType() {
        return this.getText();
    }

    @Override
    public void setType(String type) {
        this.setText(type);
    }
}
