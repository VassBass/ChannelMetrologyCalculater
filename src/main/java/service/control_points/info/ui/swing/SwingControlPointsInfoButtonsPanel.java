package service.control_points.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.dto.ControlPoints;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.control_points.info.ControlPointsInfoManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

import static model.ui.builder.CellBuilder.NONE;

public class SwingControlPointsInfoButtonsPanel extends DefaultPanel {

    public SwingControlPointsInfoButtonsPanel(@Nonnull ControlPointsInfoManager manager,
                                              @Nullable ControlPoints oldCP) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton cancelButton = new DefaultButton(labels.get(RootLabelName.CANCEL));
        DefaultButton removeButton = null;
        if (Objects.nonNull(oldCP)) removeButton = new DefaultButton(labels.get(RootLabelName.DELETE));
        DefaultButton saveButton = new DefaultButton(labels.get(RootLabelName.SAVE));

        cancelButton.addActionListener(e -> manager.closeDialog());
        if (Objects.nonNull(removeButton)) removeButton.addActionListener(e -> manager.removeControlPoints());
        saveButton.addActionListener(e -> manager.saveControlPoints());

        int x = 0;
        this.add(cancelButton, new CellBuilder().fill(NONE).x(x++).build());
        if (Objects.nonNull(removeButton)) this.add(removeButton, new CellBuilder().fill(NONE).x(x++).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(x).build());
    }
}
