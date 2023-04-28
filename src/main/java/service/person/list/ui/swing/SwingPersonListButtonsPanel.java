package service.person.list.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.person.list.PersonListManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingPersonListButtonsPanel extends DefaultPanel {
    private static final String CLOSE_TEXT = "Закрити";
    private static final String CHANGE_TEXT = "Змінити";
    private static final String ADD_TEXT = "Додати";

    public SwingPersonListButtonsPanel(@Nonnull PersonListManager manager) {
        super();

        DefaultButton closeButton = new DefaultButton(CLOSE_TEXT);
        DefaultButton changeButton = new DefaultButton(CHANGE_TEXT);
        DefaultButton addButton = new DefaultButton(ADD_TEXT);

        closeButton.addActionListener(e -> manager.clickClose());
        changeButton.addActionListener(e -> manager.clickChange());
        addButton.addActionListener(e -> manager.clickAdd());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(changeButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(addButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
