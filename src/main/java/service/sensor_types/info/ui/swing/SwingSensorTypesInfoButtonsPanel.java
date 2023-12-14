package service.sensor_types.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_types.info.SensorTypesInfoManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorTypesInfoButtonsPanel extends DefaultPanel {

    public SwingSensorTypesInfoButtonsPanel(@Nonnull SensorTypesInfoManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton buttonCancel = new DefaultButton(labels.get(RootLabelName.CANCEL));
        DefaultButton buttonRefresh = new DefaultButton(labels.get(RootLabelName.RESET));
        DefaultButton buttonSave = new DefaultButton(labels.get(RootLabelName.SAVE));

        buttonCancel.addActionListener(e -> manager.clickCancel());
        buttonRefresh.addActionListener(e -> manager.clickRefresh());
        buttonSave.addActionListener(e -> manager.clickSave());

        this.add(buttonCancel, new CellBuilder().fill(NONE).x(0).build());
        this.add(buttonRefresh, new CellBuilder().fill(NONE).x(1).build());
        this.add(buttonSave, new CellBuilder().fill(NONE).x(2).build());
    }
}
