package service.measurement.list.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.measurement.list.MeasurementListManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingMeasurementListButtonsPanel extends DefaultPanel {
    private static final String CLOSE_TEXT = "Закрити";
    private static final String DELETE_TEXT = "Видалити";
    private static final String CHANGE_TEXT = "Змінити";
    private static final String ADD_TEXT = "Додати";

    public SwingMeasurementListButtonsPanel(@Nonnull MeasurementListManager manager) {
        super();

        DefaultButton closeButton = new DefaultButton(CLOSE_TEXT);
        DefaultButton deleteButton = new DefaultButton(DELETE_TEXT);
        DefaultButton changeButton = new DefaultButton(CHANGE_TEXT);
        DefaultButton addButton = new DefaultButton(ADD_TEXT);

        closeButton.addActionListener(e -> manager.clickClose());
        deleteButton.addActionListener(e -> manager.clickRemove());
        changeButton.addActionListener(e -> manager.clickChange());
        addButton.addActionListener(e -> manager.clickAdd());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(deleteButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(changeButton, new CellBuilder().fill(NONE).x(2).build());
        this.add(addButton, new CellBuilder().fill(NONE).x(3).build());
    }
}
