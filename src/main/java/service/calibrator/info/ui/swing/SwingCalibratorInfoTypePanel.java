package service.calibrator.info.ui.swing;

import model.ui.TitledTextField;
import service.calibrator.info.ui.CalibratorInfoTypePanel;

import java.awt.*;

public class SwingCalibratorInfoTypePanel extends TitledTextField implements CalibratorInfoTypePanel {
    private static final String TITLE_TEXT = "Тип калібратора";
    private static final String TOOLTIP_TEXT = "Використовується у протоколі про перевірку МХ ВК";

    public SwingCalibratorInfoTypePanel() {
        super(20, TITLE_TEXT, Color.BLACK);
        this.setToolTipText(TOOLTIP_TEXT);
    }

    @Override
    public String getType() {
        String type = this.getText();
        if (type.isEmpty()) {
            this.setTitleColor(Color.RED);
        } else {
            this.setTitleColor(Color.BLACK);
        }
        return type;
    }

    @Override
    public void setType(String type) {
        this.setText(type);
    }
}
