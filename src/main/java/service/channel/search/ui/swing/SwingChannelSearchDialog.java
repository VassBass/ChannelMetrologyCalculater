package service.channel.search.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.channel.search.ChannelSearchConfigHolder;
import service.channel.search.ui.ChannelSearchContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

public class SwingChannelSearchDialog extends JDialog implements UI {
    private static final String TITLE = "Розширений пошук ВК";

    public SwingChannelSearchDialog(@Nonnull ApplicationScreen applicationScreen,
                                    @Nonnull ChannelSearchConfigHolder configHolder,
                                    @Nonnull ChannelSearchContext context) {
        super(applicationScreen, TITLE, true);

        SwingDatePanel datePanel = context.getElement(SwingDatePanel.class);
        SwingLocationPanel locationPanel = context.getElement(SwingLocationPanel.class);
        SwingButtonPanel buttonPanel = context.getElement(SwingButtonPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(datePanel, new CellBuilder().width(1).weightY(0.9).x(0).y(0).build());
        panel.add(locationPanel, new CellBuilder().width(1).weightY(0.9).x(1).y(0).build());
        panel.add(buttonPanel, new CellBuilder().width(2).weightY(0.1).x(0).y(1).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }

    @Override
    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }

    @Override
    public void shutdown() {
        this.dispose();
    }

    @Override
    public Object getSource() {
        return this;
    }
}
