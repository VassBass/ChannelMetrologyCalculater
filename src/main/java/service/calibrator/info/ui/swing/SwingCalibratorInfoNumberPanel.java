package service.calibrator.info.ui.swing;

import localization.Labels;
import localization.Messages;
import model.ui.TitledTextField;
import service.calibrator.info.ui.CalibratorInfoNumberPanel;

public class SwingCalibratorInfoNumberPanel extends TitledTextField implements CalibratorInfoNumberPanel {
    private static final String SERIAL_NUMBER = "serialNumber";
    private static final String SERIAL_NUMBER_TOOLTIP = "serialNumberTooltip";

    public SwingCalibratorInfoNumberPanel() {
        super(20, Labels.getLabels(SwingCalibratorInfoNumberPanel.class).get(SERIAL_NUMBER));
        this.setToolTipText(Messages.getMessages(SwingCalibratorInfoNumberPanel.class).get(SERIAL_NUMBER_TOOLTIP));
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
