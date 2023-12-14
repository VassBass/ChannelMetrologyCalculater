package service.measurement.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.measurement.list.MeasurementListManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingMeasurementListButtonsPanel extends DefaultPanel {

    public SwingMeasurementListButtonsPanel(@Nonnull MeasurementListManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton closeButton = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton deleteButton = new DefaultButton(labels.get(RootLabelName.DELETE));
        DefaultButton changeButton = new DefaultButton(labels.get(RootLabelName.CHANGE));
        DefaultButton addButton = new DefaultButton(labels.get(RootLabelName.ADD));

        closeButton.addActionListener(e -> manager.clickClose());
        deleteButton.addActionListener(e -> manager.clickRemove());
        changeButton.addActionListener(e -> manager.clickChange());
        addButton.addActionListener(e -> manager.clickAdd());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(deleteButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(changeButton, new CellBuilder().fill(NONE).x(2).build());
        this.add(addButton, new CellBuilder().fill(NONE).x(3).build());
    }
}
