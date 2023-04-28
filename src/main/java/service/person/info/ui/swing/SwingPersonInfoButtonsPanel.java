package service.person.info.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.person.info.PersonInfoManager;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.NONE;

public class SwingPersonInfoButtonsPanel extends DefaultPanel {
    private static final String CANCEL_TEXT = "Відмінити";
    private static final String REFRESH_TEXT = "Скинути";
    private static final String SAVE_TEXT = "Зберегти";

    public SwingPersonInfoButtonsPanel(@Nonnull PersonInfoManager manager) {
        super();

        DefaultButton cancelButton = new DefaultButton(CANCEL_TEXT);
        DefaultButton refreshButton = new DefaultButton(REFRESH_TEXT);
        DefaultButton saveButton = new DefaultButton(SAVE_TEXT);

        cancelButton.addActionListener(e -> manager.clickClose());
        refreshButton.addActionListener(e -> manager.clickRefresh());
        saveButton.addActionListener(e -> manager.clickSave());

        this.add(cancelButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(refreshButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(saveButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
