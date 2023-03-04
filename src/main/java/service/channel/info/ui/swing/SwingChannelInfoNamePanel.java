package service.channel.info.ui.swing;

import model.ui.TitledTextField;
import service.channel.info.ui.ChannelInfoNamePanel;

import java.awt.*;

public class SwingChannelInfoNamePanel extends TitledTextField implements ChannelInfoNamePanel {
    private static final String TITLE_TEXT = "Назва ВК";

    public SwingChannelInfoNamePanel() {
        super(15, TITLE_TEXT, Color.BLACK);
    }

    @Override
    public String getChannelName() {
        return this.getText();
    }

    @Override
    public void setChannelName(String name) {
        this.setText(name);
    }

    @Override
    public boolean isNameValid() {
        if (getName().isEmpty()) {
            setTitleColor(Color.RED);
            return false;
        }

        this.setTitleColor(Color.BLACK);
        return true;
    }
}
