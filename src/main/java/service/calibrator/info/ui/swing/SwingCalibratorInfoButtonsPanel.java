package service.calibrator.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calibrator.info.CalibratorInfoManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalibratorInfoButtonsPanel extends DefaultPanel {

    public SwingCalibratorInfoButtonsPanel(@Nonnull CalibratorInfoManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton saveButton = new DefaultButton(labels.get(RootLabelName.SAVE));
        DefaultButton closeButton = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton deleteButton = new DefaultButton(labels.get(RootLabelName.DELETE));

        saveButton.addActionListener(e -> manager.saveCalibrator());
        closeButton.addActionListener(e -> manager.clickCloseDialog());
        deleteButton.addActionListener(e -> manager.clickRemove());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(deleteButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
