package service.channel.search.ui.swing;

import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.channel.search.ChannelSearchManager;

import static model.ui.builder.CellBuilder.NONE;

public class SwingButtonPanel extends DefaultPanel {
    private static final String START_BUTTON_TEXT = "Шукати";
    private static final String CANCEL_BUTTON_TEXT = "Відмінити пошук";
    private static final String CLOSE_BUTTON_TEXT = "Закрити";

    public SwingButtonPanel(ChannelSearchManager manager) {
        super();

        DefaultButton startButton = new DefaultButton(START_BUTTON_TEXT);
        DefaultButton cancelButton = new DefaultButton(CANCEL_BUTTON_TEXT);
        DefaultButton closeButton = new DefaultButton(CLOSE_BUTTON_TEXT);

        startButton.addActionListener(e -> manager.clickStart());
        cancelButton.addActionListener(e -> manager.clickCancel());
        closeButton.addActionListener(e -> manager.clickClose());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(cancelButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(startButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
