package service.calibrator.info.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calibrator.info.CalibratorInfoManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalibratorInfoButtonsPanel extends DefaultPanel {
    private static final String CANCEL_BUTTON_TEXT = "Закрити";
    private static final String SAVE_BUTTON_TEXT = "Зберегти";

    public SwingCalibratorInfoButtonsPanel(@Nonnull CalibratorInfoManager manager) {
        super();

        DefaultButton cancelButton = new DefaultButton(CANCEL_BUTTON_TEXT);
        DefaultButton saveButton = new DefaultButton(SAVE_BUTTON_TEXT);

        this.add(cancelButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(1).build());
    }
}
