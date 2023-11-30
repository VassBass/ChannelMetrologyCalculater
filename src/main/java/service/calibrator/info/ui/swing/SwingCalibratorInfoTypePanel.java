package service.calibrator.info.ui.swing;

import localization.Labels;
import localization.Messages;
import model.ui.TitledTextField;
import service.calibrator.info.ui.CalibratorInfoTypePanel;

import java.awt.*;

public class SwingCalibratorInfoTypePanel extends TitledTextField implements CalibratorInfoTypePanel {
    private static final String CALIBRATOR_TYPE = "calibratorType";
    private static final String CALIBRATOR_TYPE_TOOLTIP = "calibratorTypeTooltip";

    public SwingCalibratorInfoTypePanel() {
        super(20, Labels.getLabels(SwingCalibratorInfoTypePanel.class).get(CALIBRATOR_TYPE), Color.BLACK);
        this.setToolTipText(Messages.getMessages(SwingCalibratorInfoTypePanel.class).get(CALIBRATOR_TYPE_TOOLTIP));
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
