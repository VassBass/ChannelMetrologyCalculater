package service.sensor_error.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.dto.SensorError;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_error.info.SensorErrorInfoManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorErrorInfoButtonsPanel extends DefaultPanel {

    public SwingSensorErrorInfoButtonsPanel(@Nonnull SensorErrorInfoManager manager,
                                            @Nullable SensorError oldError) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton closeButton = new DefaultButton(labels.get(RootLabelName.CLOSE));
        DefaultButton removeButton = new DefaultButton(labels.get(RootLabelName.DELETE));
        DefaultButton saveButton = new DefaultButton(labels.get(RootLabelName.SAVE));

        closeButton.addActionListener(e -> manager.clickClose());
        removeButton.addActionListener(e -> manager.clickRemove());
        saveButton.addActionListener(e -> manager.clickSave());

        int x = 0;
        this.add(closeButton, new CellBuilder().fill(NONE).x(x++).build());
        if (Objects.nonNull(oldError)) this.add(removeButton, new CellBuilder().fill(NONE).x(x++).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(x).build());
    }
}
