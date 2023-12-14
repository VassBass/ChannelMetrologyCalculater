package service.channel.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.TitledLabel;
import service.channel.info.ui.ChannelInfoNextDatePanel;

import java.awt.*;

public class SwingChannelInfoNextDatePanel extends TitledLabel implements ChannelInfoNextDatePanel {

    public SwingChannelInfoNextDatePanel() {
        super(Labels.getRootLabels().get(RootLabelName.NEXT_CHECK_DATE), Color.BLACK);
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
