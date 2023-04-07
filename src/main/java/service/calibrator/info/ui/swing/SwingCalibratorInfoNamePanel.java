package service.calibrator.info.ui.swing;

import model.ui.TitledTextField;
import service.calibrator.info.CalibratorInfoManager;
import service.calibrator.info.ui.CalibratorInfoNamePanel;

import javax.annotation.Nonnull;
import javax.swing.*;

public class SwingCalibratorInfoNamePanel extends TitledTextField implements CalibratorInfoNamePanel {
    private static final String TITLE_TEXT = "Назва калібратора";
    private static final String TOOLTIP_TEXT = "<html>" +
            "Має бути унікальним." +
            "<br>Використовується лише всередині системи для ідентифікування калібратора." +
            "<br>НЕ використовується у протоколі перевірки МХ ВК" +
            "</html>";
    private static final String COPY_TYPE_TEXT = "Скопіювати тип калібратора";


    public SwingCalibratorInfoNamePanel(@Nonnull CalibratorInfoManager manager) {
        super(20, TITLE_TEXT);
        this.setToolTipText(TOOLTIP_TEXT);
        this.setComponentPopupMenu(popupMenu(manager));
    }

    @Override
    public String getCalibratorName() {
        return this.getText();
    }

    @Override
    public void setCalibratorName(String name) {
        this.setText(name);
    }

    private JPopupMenu popupMenu(CalibratorInfoManager manager) {
        JPopupMenu popupMenu = new JPopupMenu(COPY_TYPE_TEXT);
        JMenuItem check = new JMenuItem(COPY_TYPE_TEXT);
        check.addActionListener(e -> manager.copyTypeToNameField());
        popupMenu.add(check);

        return popupMenu;
    }
}
