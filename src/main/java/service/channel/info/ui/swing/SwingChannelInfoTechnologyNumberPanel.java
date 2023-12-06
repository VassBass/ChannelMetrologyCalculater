package service.channel.info.ui.swing;

import localization.Labels;
import model.ui.TitledTextField;
import service.channel.info.ui.ChannelInfoTechnologyNumberPanel;

import java.awt.*;

public class SwingChannelInfoTechnologyNumberPanel extends TitledTextField implements ChannelInfoTechnologyNumberPanel {
    private static final String TECHNOLOGICAL_NUMBER = "technologicalNumber";

    public SwingChannelInfoTechnologyNumberPanel() {
        super(15, Labels.getLabels(SwingChannelInfoTechnologyNumberPanel.class).get(TECHNOLOGICAL_NUMBER), Color.BLACK);
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
