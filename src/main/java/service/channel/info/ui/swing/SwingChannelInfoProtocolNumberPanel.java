package service.channel.info.ui.swing;

import model.ui.TitledTextField;
import service.channel.info.ui.ChannelInfoProtocolNumberPanel;

import java.awt.*;

public class SwingChannelInfoProtocolNumberPanel extends TitledTextField implements ChannelInfoProtocolNumberPanel {
    private static final String TITLE_TEXT = "Номер протоколу";
    private static final String TOOLTIP_TEXT = "Номер протоколу результатів останньої перевірки";

    public SwingChannelInfoProtocolNumberPanel() {
        super(15, TITLE_TEXT, Color.BLACK);
        this.setToolTipText(TOOLTIP_TEXT);
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
