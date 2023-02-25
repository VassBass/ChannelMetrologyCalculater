package service.channel.ui.swing;

import model.ui.builder.CellBuilder;

import javax.swing.*;
import java.awt.*;

public class SwingPanel extends JPanel {
    public SwingPanel(SwingChannelInfoTable infoTable,
                      SwingSearchPanel searchPanel,
                      SwingChannelButtonsPanel buttonsPanel,
                      SwingChannelsTable channelsTable) {
        super(new GridBagLayout());

        this.add(infoTable, new CellBuilder().x(0).y(0).width(2).weightY(0.05).build());
        this.add(searchPanel, new CellBuilder().x(0).y(1).width(1).weightY(0.05).build());
        this.add(buttonsPanel, new CellBuilder().x(1).y(1).width(1).weightY(0.05).build());
        this.add(new JScrollPane(channelsTable), new CellBuilder().x(0).y(2).width(2).weightY(0.9).build());
    }
}