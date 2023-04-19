package service.control_points.list.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.control_points.list.ControlPointsListManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingControlPointsListButtonsPanel extends DefaultPanel {
    private static final String CLOSE_BUTTON_TEXT = "Закрити";
    private static final String REMOVE_BUTTON_TEXT = "Видалити";
    private static final String DETAILS_BUTTON_TEXT = "Дивитись";
    private static final String ADD_BUTTON_TEXT = "Додати";

    public SwingControlPointsListButtonsPanel(@Nonnull ControlPointsListManager manager) {
        super();

        DefaultButton closeButton = new DefaultButton(CLOSE_BUTTON_TEXT);
        DefaultButton removeButton = new DefaultButton(REMOVE_BUTTON_TEXT);
        DefaultButton detailsButton = new DefaultButton(DETAILS_BUTTON_TEXT);
        DefaultButton addButton = new DefaultButton(ADD_BUTTON_TEXT);

        closeButton.addActionListener(e -> manager.shutdownService());
        removeButton.addActionListener(e -> manager.removeControlPoints());
        detailsButton.addActionListener(e -> manager.showControlPointsDetails());
        addButton.addActionListener(e -> manager.addControlPoints());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(removeButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(detailsButton, new CellBuilder().fill(NONE).x(2).build());
        this.add(addButton, new CellBuilder().fill(NONE).x(3).build());
    }
}
