package service.channel.search.ui.swing;

import application.ApplicationScreen;
import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.channel.search.ChannelSearchConfigHolder;
import service.channel.search.ui.ChannelSearchContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import java.util.Map;

public class SwingChannelSearchDialog extends DefaultDialog {

    private static final Map<String, String> labels = Labels.getRootLabels();

    public SwingChannelSearchDialog(@Nonnull ApplicationScreen applicationScreen,
                                    @Nonnull ChannelSearchConfigHolder configHolder,
                                    @Nonnull ChannelSearchContext context) {
        super(
                applicationScreen,
                labels.get(RootLabelName.ADVANCED_SEARCH) +
                        Labels.SPACE +
                        labels.get(RootLabelName.CHANNEL_SHORT)
        );

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
}
