package service.channel.info.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;

public class SwingChannelInfoButtonsPanel extends DefaultPanel {
    private static final String SAVE_TEXT = "Зберегти";
    private static final String SAVE_CALCULATE_TEXT = "Зберегти та розрахувати";
    private static final String RESET_TEXT = "Скинути";
    private static final String DELETE_TEXT = "Видалити";

    public SwingChannelInfoButtonsPanel(final ChannelInfoManager manager) {
        super();

        DefaultButton buttonSave = new DefaultButton(SAVE_TEXT);
        DefaultButton buttonSaveAndCalculate = new DefaultButton(SAVE_CALCULATE_TEXT);
        DefaultButton buttonReset = new DefaultButton(RESET_TEXT);
        DefaultButton buttonDelete = new DefaultButton(DELETE_TEXT);

        buttonSave.addActionListener(e -> manager.saveChannel());
        buttonSaveAndCalculate.addActionListener(e -> manager.saveAndCalculateChannel());
        buttonReset.addActionListener(e -> manager.resetChannelInfo());
        buttonDelete.addActionListener(e -> manager.deleteChannel());

        this.add(buttonSave, new CellBuilder().x(0).y(0).build());
        this.add(buttonSaveAndCalculate, new CellBuilder().x(1).y(0).build());
        this.add(buttonReset, new CellBuilder().x(0).y(1).build());
        this.add(buttonDelete, new CellBuilder().x(1).y(1).build());
    }
}
