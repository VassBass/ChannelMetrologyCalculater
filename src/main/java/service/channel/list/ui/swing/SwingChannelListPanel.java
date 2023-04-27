package service.channel.list.ui.swing;

import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.channel.list.ui.ChannelListSwingContext;

import javax.annotation.Nonnull;
import javax.swing.*;

public class SwingChannelListPanel extends DefaultPanel {
    public SwingChannelListPanel(@Nonnull ChannelListSwingContext context) {
        super();

        SwingChannelListInfoTable infoTable = context.getElement(SwingChannelListInfoTable.class);
        SwingChannelListSearchPanel searchPanel = context.getElement(SwingChannelListSearchPanel.class);
        SwingChannelListButtonsPanel buttonsPanel = context.getElement(SwingChannelListButtonsPanel.class);
        SwingChannelListTable channelsTable = context.getElement(SwingChannelListTable.class);

        this.add(infoTable, new CellBuilder().x(0).y(0).width(2).weightY(0.05).build());
        this.add(searchPanel, new CellBuilder().x(0).y(1).width(1).weightY(0.05).build());
        this.add(buttonsPanel, new CellBuilder().x(1).y(1).width(1).weightY(0.05).build());
        this.add(new JScrollPane(channelsTable), new CellBuilder().x(0).y(2).width(2).weightY(0.9).build());
    }
}
