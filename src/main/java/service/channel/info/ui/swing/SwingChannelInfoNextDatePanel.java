package service.channel.info.ui.swing;

import model.ui.TitledLabel;
import service.channel.info.ui.ChannelInfoNextDatePanel;

import java.awt.*;

public class SwingChannelInfoNextDatePanel extends TitledLabel implements ChannelInfoNextDatePanel {
    private static final String TITLE_TEXT = " Дата наступної перевірки";

    public SwingChannelInfoNextDatePanel() {
        super(TITLE_TEXT, Color.BLACK);
    }

    @Override
    public void setNextDate(String date) {
        this.setText(date);
    }

    @Override
    public String getNextDate() {
        return this.getText();
    }
}
