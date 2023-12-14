package service.sensor_types.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_types.list.SensorTypesListManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorTypesListButtonsPanel extends DefaultPanel {

    public SwingSensorTypesListButtonsPanel(@Nonnull SensorTypesListManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton closeButton = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton changeButton = new DefaultButton(labels.get(RootLabelName.CHANGE));

        closeButton.addActionListener(e -> manager.clickClose());
        changeButton.addActionListener(e -> manager.clickChange());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(changeButton, new CellBuilder().fill(NONE).x(1).build());
    }
}
