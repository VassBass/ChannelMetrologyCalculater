package service.sensor_types.list.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_types.list.SensorTypesListManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorTypesListButtonsPanel extends DefaultPanel {
    private static final String CLOSE_TEXT = "Закрити";
    private static final String CHANGE_TEXT = "Змінити";

    public SwingSensorTypesListButtonsPanel(@Nonnull SensorTypesListManager manager) {
        super();

        DefaultButton closeButton = new DefaultButton(CLOSE_TEXT);
        DefaultButton changeButton = new DefaultButton(CHANGE_TEXT);

        closeButton.addActionListener(e -> manager.clickClose());
        changeButton.addActionListener(e -> manager.clickChange());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(changeButton, new CellBuilder().fill(NONE).x(1).build());
    }
}
