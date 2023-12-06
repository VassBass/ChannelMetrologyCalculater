package service.channel.info.ui.swing;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.ui.TitledTextField;
import service.channel.info.ui.ChannelInfoProtocolNumberPanel;

import java.awt.*;

public class SwingChannelInfoProtocolNumberPanel extends TitledTextField implements ChannelInfoProtocolNumberPanel {
    private static final String PROTOCOL_TOOLTIP = "protocolTooltip";

    public SwingChannelInfoProtocolNumberPanel() {
        super(15, Labels.getRootLabels().get(RootLabelName.PROTOCOL_NUMBER), Color.BLACK);
        this.setToolTipText(Messages.getMessages(SwingChannelInfoProtocolNumberPanel.class).get(PROTOCOL_TOOLTIP));
    }

    @Override
    public void setProtocolNumber(String protocolNumber) {
        this.setText(protocolNumber);
    }

    @Override
    public String getProtocolNumber() {
        return this.getText();
    }
}
