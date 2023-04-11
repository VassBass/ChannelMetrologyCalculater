package service.sensor_error.info.ui.swing;

import model.dto.SensorError;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_error.info.SensorErrorInfoManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorErrorInfoButtonsPanel extends DefaultPanel {
    private static final String CLOSE_BUTTON_TEXT = "Відмінити";
    private static final String REMOVE_BUTTON_TEXT = "Видалити";
    private static final String SAVE_BUTTON_TEXT = "Зберегти";

    public SwingSensorErrorInfoButtonsPanel(@Nonnull SensorErrorInfoManager manager,
                                            @Nullable SensorError oldError) {
        super();

        DefaultButton closeButton = new DefaultButton(CLOSE_BUTTON_TEXT);
        DefaultButton removeButton = new DefaultButton(REMOVE_BUTTON_TEXT);
        DefaultButton saveButton = new DefaultButton(SAVE_BUTTON_TEXT);

        closeButton.addActionListener(e -> manager.clickClose());
        removeButton.addActionListener(e -> manager.clickRemove());
        saveButton.addActionListener(e -> manager.clickSave());

        int x = 0;
        this.add(closeButton, new CellBuilder().fill(NONE).x(x++).build());
        if (Objects.nonNull(oldError)) this.add(removeButton, new CellBuilder().fill(NONE).x(x++).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(x).build());
    }
}
