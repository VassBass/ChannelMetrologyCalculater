package service.channel.info.ui.swing;

import model.ui.TitledTextField;
import service.channel.info.ui.ChannelInfoTechnologyNumberPanel;

import java.awt.*;

public class SwingChannelInfoTechnologyNumberPanel extends TitledTextField implements ChannelInfoTechnologyNumberPanel {
    private static final String TITLE_TEXT = "Технологічний номер";

    public SwingChannelInfoTechnologyNumberPanel() {
        super(15, TITLE_TEXT, Color.BLACK);
    }

    @Override
    public void setTechnologyNumber(String technologyNumber) {
        this.setText(technologyNumber);
    }

    @Override
    public String getTechnologyNumber() {
        return this.getText();
    }

    @Override
    public boolean isTechnologyNumberValid() {
        if (getTechnologyNumber().isEmpty()) {
            setTitleColor(Color.RED);
            return false;
        }

        this.setTitleColor(Color.BLACK);
        return true;
    }
}
