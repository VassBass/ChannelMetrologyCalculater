package service.sensor_error.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_error.list.SensorErrorListManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorErrorListButtonsPanel extends DefaultPanel {

    public SwingSensorErrorListButtonsPanel(@Nonnull SensorErrorListManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton buttonClose = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton buttonChange = new DefaultButton(labels.get(RootLabelName.CHANGE));
        DefaultButton buttonAdd = new DefaultButton(labels.get(RootLabelName.ADD));
        DefaultButton buttonRemove = new DefaultButton(labels.get(RootLabelName.DELETE));

        buttonClose.addActionListener(e -> manager.clickClose());
        buttonChange.addActionListener(e -> manager.clickChange());
        buttonAdd.addActionListener(e -> manager.clickAdd());
        buttonRemove.addActionListener(e -> manager.clickRemove());

        this.add(buttonClose, new CellBuilder().fill(NONE).x(0).build());
        this.add(buttonRemove, new CellBuilder().fill(NONE).x(1).build());
        this.add(buttonChange, new CellBuilder().fill(NONE).x(2).build());
        this.add(buttonAdd, new CellBuilder().fill(NONE).x(3).build());
    }
}
