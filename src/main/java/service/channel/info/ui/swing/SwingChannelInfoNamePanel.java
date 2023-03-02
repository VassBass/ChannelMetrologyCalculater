package service.channel.info.ui.swing;

import model.ui.TitledTextField;
import service.channel.info.ui.ChannelInfoNamePanel;

public class SwingChannelInfoNamePanel extends TitledTextField implements ChannelInfoNamePanel {
    private static final String TITLE_TEXT = "Назва ВК";

    public SwingChannelInfoNamePanel() {
        super(TITLE_TEXT, 15);
    }

    @Override
    public String getName() {
        return this.getText();
    }

    @Override
    public void setName(String name) {
        this.setText(name);
    }
}
