package service.calibrator.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calibrator.list.CalibratorListManager;

import javax.annotation.Nonnull;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalibratorListButtonsPanel extends DefaultPanel {
    public SwingCalibratorListButtonsPanel(@Nonnull CalibratorListManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton buttonDelete = new DefaultButton(labels.get(RootLabelName.DELETE));
        DefaultButton buttonDetails = new DefaultButton(labels.get(RootLabelName.DETAILS));
        DefaultButton buttonAdd = new DefaultButton(labels.get(RootLabelName.ADD));

        buttonDelete.addActionListener(e -> manager.clickDelete());
        buttonDetails.addActionListener(e -> manager.clickDetails());
        buttonAdd.addActionListener(e -> manager.clickAdd());

        this.add(buttonDelete, new CellBuilder().fill(NONE).x(0).build());
        this.add(buttonDetails, new CellBuilder().fill(NONE).x(1).build());
        this.add(buttonAdd, new CellBuilder().fill(NONE).x(2).build());
    }
}
