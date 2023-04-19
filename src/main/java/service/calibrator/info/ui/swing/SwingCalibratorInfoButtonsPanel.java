package service.calibrator.info.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calibrator.info.CalibratorInfoManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalibratorInfoButtonsPanel extends DefaultPanel {
    private static final String SAVE_BUTTON_TEXT = "Зберегти";
    private static final String CLOSE_BUTTON_TEXT = "Закрити";
    private static final String REMOVE_BUTTON_TEXT = "Видалити";

    public SwingCalibratorInfoButtonsPanel(@Nonnull CalibratorInfoManager manager) {
        super();

        DefaultButton saveButton = new DefaultButton(SAVE_BUTTON_TEXT);
        DefaultButton closeButton = new DefaultButton(CLOSE_BUTTON_TEXT);
        DefaultButton removeButton = new DefaultButton(REMOVE_BUTTON_TEXT);

        saveButton.addActionListener(e -> manager.saveCalibrator());
        closeButton.addActionListener(e -> manager.clickCloseDialog());
        removeButton.addActionListener(e -> manager.clickRemove());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(removeButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
