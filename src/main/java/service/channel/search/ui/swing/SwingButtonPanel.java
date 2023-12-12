package service.channel.search.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.channel.search.ChannelSearchManager;

import java.util.Map;

import static model.ui.builder.CellBuilder.NONE;

public class SwingButtonPanel extends DefaultPanel {

    public SwingButtonPanel(ChannelSearchManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton startButton = new DefaultButton(labels.get(RootLabelName.START_SEARCH));
        DefaultButton cancelButton = new DefaultButton(labels.get(RootLabelName.CANCEL_SEARCH));
        DefaultButton closeButton = new DefaultButton(labels.get(RootLabelName.CLOSE));

        startButton.addActionListener(e -> manager.clickStart());
        cancelButton.addActionListener(e -> manager.clickCancel());
        closeButton.addActionListener(e -> manager.clickClose());

        this.add(closeButton, new CellBuilder().fill(NONE).x(0).build());
        this.add(cancelButton, new CellBuilder().fill(NONE).x(1).build());
        this.add(startButton, new CellBuilder().fill(NONE).x(2).build());
    }
}
