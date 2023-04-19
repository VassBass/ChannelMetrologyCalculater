package service.control_points.info.ui.swing;

import model.dto.ControlPoints;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.control_points.info.ControlPointsInfoManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static model.ui.builder.CellBuilder.NONE;

public class SwingControlPointsInfoButtonsPanel extends DefaultPanel {
    private static final String CANCEL_BUTTON_TEXT = "Відміна";
    private static final String REMOVE_BUTTON_TEXT = "Видалити";
    private static final String SAVE_BUTTON_TEXT = "Зберегти";

    public SwingControlPointsInfoButtonsPanel(@Nonnull ControlPointsInfoManager manager,
                                              @Nullable ControlPoints oldCP) {
        super();

        DefaultButton cancelButton = new DefaultButton(CANCEL_BUTTON_TEXT);
        DefaultButton removeButton = null;
        if (Objects.nonNull(oldCP)) removeButton = new DefaultButton(REMOVE_BUTTON_TEXT);
        DefaultButton saveButton = new DefaultButton(SAVE_BUTTON_TEXT);

        cancelButton.addActionListener(e -> manager.closeDialog());
        if (Objects.nonNull(removeButton)) removeButton.addActionListener(e -> manager.removeControlPoints());
        saveButton.addActionListener(e -> manager.saveControlPoints());

        int x = 0;
        this.add(cancelButton, new CellBuilder().fill(NONE).x(x++).build());
        if (Objects.nonNull(removeButton)) this.add(removeButton, new CellBuilder().fill(NONE).x(x++).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(x).build());
    }
}
