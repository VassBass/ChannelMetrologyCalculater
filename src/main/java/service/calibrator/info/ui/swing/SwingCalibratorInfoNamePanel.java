package service.calibrator.info.ui.swing;

import localization.Labels;
import localization.Messages;
import model.ui.TitledTextField;
import service.calibrator.info.CalibratorInfoManager;
import service.calibrator.info.ui.CalibratorInfoNamePanel;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SwingCalibratorInfoNamePanel extends TitledTextField implements CalibratorInfoNamePanel {
    private static final String CALIBRATOR_NAME = "calibratorName";
    private static final String CALIBRATOR_NAME_TOOLTIP = "calibratorNameTooltip";
    private static final String COPY_CALIBRATOR_TYPE = "copyCalibratorType";

    private static final Map<String, String> labels = Labels.getLabels(SwingCalibratorInfoNamePanel.class);

    public SwingCalibratorInfoNamePanel(@Nonnull CalibratorInfoManager manager) {
        super(20, labels.get(CALIBRATOR_NAME));
        this.setToolTipText(Messages.getMessages(SwingCalibratorInfoNamePanel.class).get(CALIBRATOR_NAME_TOOLTIP));
        this.setComponentPopupMenu(popupMenu(manager));
    }

    @Override
    public String getCalibratorName() {
        String name = this.getText();
        if (name.isEmpty()) {
            this.setTitleColor(Color.RED);
        } else {
            this.setTitleColor(Color.BLACK);
        }
        return name;
    }

    @Override
    public void setCalibratorName(String name) {
        this.setText(name);
    }

    private JPopupMenu popupMenu(CalibratorInfoManager manager) {
        JPopupMenu popupMenu = new JPopupMenu(labels.get(COPY_CALIBRATOR_TYPE));
        JMenuItem check = new JMenuItem(labels.get(COPY_CALIBRATOR_TYPE));
        check.addActionListener(e -> manager.copyTypeToNameField());
        popupMenu.add(check);

        return popupMenu;
    }
}
