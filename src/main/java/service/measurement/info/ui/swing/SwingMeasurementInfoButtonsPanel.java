package service.measurement.info.ui.swing;

import model.dto.Measurement;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.measurement.info.MeasurementInfoManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;

import static model.ui.builder.CellBuilder.NONE;

public class SwingMeasurementInfoButtonsPanel extends DefaultPanel {
    private static final String CANCEL_TEXT = "Відміна";
    private static final String CLEAN_TEXT = "Скинути";
    private static final String SAVE_TEXT = "Зберегти";

    public SwingMeasurementInfoButtonsPanel(@Nonnull MeasurementInfoManager manager, @Nullable Measurement oldMeasurement) {
        super();

        DefaultButton cancelButton = new DefaultButton(CANCEL_TEXT);
        DefaultButton cleanButton = new DefaultButton(CLEAN_TEXT);
        DefaultButton saveButton = new DefaultButton(SAVE_TEXT);

        cancelButton.addActionListener(e -> manager.clickCancel());
        cleanButton.addActionListener(e -> manager.clickClear());
        saveButton.addActionListener(e -> manager.clickSave());

        int x = 0;
        this.add(cancelButton, new CellBuilder().fill(NONE).x(x++).build());
        if (Objects.nonNull(oldMeasurement)) this.add(cleanButton, new CellBuilder().fill(NONE).x(x++).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(x).build());
    }
}
