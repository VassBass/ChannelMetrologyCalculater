package service.calibrator.list.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calibrator.list.CalibratorListManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingCalibratorListButtonsPanel extends DefaultPanel {
    private static final String DELETE_TEXT = "Видалити";
    private static final String DETAILS_TEXT = "Детальніше";
    private static final String ADD_TEXT = "Додати";

    public SwingCalibratorListButtonsPanel(@Nonnull CalibratorListManager manager) {
        super();

        DefaultButton buttonDelete = new DefaultButton(DELETE_TEXT);
        DefaultButton buttonDetails = new DefaultButton(DETAILS_TEXT);
        DefaultButton buttonAdd = new DefaultButton(ADD_TEXT);

        buttonDelete.addActionListener(e -> manager.clickDelete());
        buttonDetails.addActionListener(e -> manager.clickDetails());
        buttonAdd.addActionListener(e -> manager.clickAdd());

        this.add(buttonDelete, new CellBuilder().fill(NONE).x(0).build());
        this.add(buttonDetails, new CellBuilder().fill(NONE).x(1).build());
        this.add(buttonAdd, new CellBuilder().fill(NONE).x(2).build());
    }
}
