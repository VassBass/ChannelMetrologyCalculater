package service.sensor_error.list.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_error.list.SensorErrorListManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingSensorErrorListButtonsPanel extends DefaultPanel {
    private static final String CLOSE_BUTTON_TEXT = "Закрити";
    private static final String CHANGE_BUTTON_TEXT = "Змінити";
    private static final String ADD_BUTTON_TEXT = "Додати";

    public SwingSensorErrorListButtonsPanel(@Nonnull SensorErrorListManager manager) {
        super();

        DefaultButton buttonClose = new DefaultButton(CLOSE_BUTTON_TEXT);
        DefaultButton buttonChange = new DefaultButton(CHANGE_BUTTON_TEXT);
        DefaultButton buttonAdd = new DefaultButton(ADD_BUTTON_TEXT);

        buttonClose.addActionListener(e -> manager.clickClose());
        buttonChange.addActionListener(e -> manager.clickChange());
        buttonAdd.addActionListener(e -> manager.clickAdd());

        this.add(buttonClose, new CellBuilder().fill(NONE).x(0).build());
        this.add(buttonChange, new CellBuilder().fill(NONE).x(1).build());
        this.add(buttonAdd, new CellBuilder().fill(NONE).x(2).build());
    }
}
