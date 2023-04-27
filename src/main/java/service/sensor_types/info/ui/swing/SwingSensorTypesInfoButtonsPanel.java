package service.sensor_types.info.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_types.info.SensorTypesInfoManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorTypesInfoButtonsPanel extends DefaultPanel {
    private static final String CANCEL_TEXT = "Відміна";
    private static final String REFRESH_TEXT = "Скинути";
    private static final String SAVE_TEXT = "Зберегти";

    public SwingSensorTypesInfoButtonsPanel(@Nonnull SensorTypesInfoManager manager) {
        super();

        DefaultButton buttonCancel = new DefaultButton(CANCEL_TEXT);
        DefaultButton buttonRefresh = new DefaultButton(REFRESH_TEXT);
        DefaultButton buttonSave = new DefaultButton(SAVE_TEXT);

        buttonCancel.addActionListener(e -> manager.clickCancel());
        buttonRefresh.addActionListener(e -> manager.clickRefresh());
        buttonSave.addActionListener(e -> manager.clickSave());

        this.add(buttonCancel, new CellBuilder().fill(NONE).x(0).build());
        this.add(buttonRefresh, new CellBuilder().fill(NONE).x(1).build());
        this.add(buttonSave, new CellBuilder().fill(NONE).x(2).build());
    }
}
