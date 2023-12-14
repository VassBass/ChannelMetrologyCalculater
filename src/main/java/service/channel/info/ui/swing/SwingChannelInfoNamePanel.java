package service.channel.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.TitledTextField;
import service.channel.info.ui.ChannelInfoNamePanel;

import java.awt.*;

public class SwingChannelInfoNamePanel extends TitledTextField implements ChannelInfoNamePanel {

    public SwingChannelInfoNamePanel() {
        super(15, Labels.getRootLabels().get(RootLabelName.CHANNEL_NAME_SHORT), Color.BLACK);
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
        if (getChannelName().isEmpty()) {
            setTitleColor(Color.RED);
            return false;
        }

        this.setTitleColor(Color.BLACK);
        return true;
    }
}
