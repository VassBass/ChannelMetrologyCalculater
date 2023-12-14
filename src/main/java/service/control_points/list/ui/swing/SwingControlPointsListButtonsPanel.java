package service.control_points.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.control_points.list.ControlPointsListManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingControlPointsListButtonsPanel extends DefaultPanel {

    public SwingControlPointsListButtonsPanel(@Nonnull ControlPointsListManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton closeButton = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton removeButton = new DefaultButton(labels.get(RootLabelName.DELETE));
        DefaultButton detailsButton = new DefaultButton(labels.get(RootLabelName.WATCH));
        DefaultButton addButton = new DefaultButton(labels.get(RootLabelName.ADD));

        closeButton.addActionListener(e -> manager.shutdownService());
        removeButton.addActionListener(e -> manager.removeControlPoints());
        detailsButton.addActionListener(e -> manager.showControlPointsDetails());
        addButton.addActionListener(e -> manager.addControlPoints());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(removeButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(detailsButton, new CellBuilder().fill(NONE).x(2).build());
        this.add(addButton, new CellBuilder().fill(NONE).x(3).build());
    }
}
