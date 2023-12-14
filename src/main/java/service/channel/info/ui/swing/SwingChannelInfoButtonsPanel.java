package service.channel.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoManager;

import java.util.Map;

public class SwingChannelInfoButtonsPanel extends DefaultPanel {

    public SwingChannelInfoButtonsPanel(final ChannelInfoManager manager) {
        super();
        Map<String, String> labels = Labels.getRootLabels();

        DefaultButton buttonSave = new DefaultButton(labels.get(RootLabelName.SAVE));
        DefaultButton buttonSaveAndCalculate = new DefaultButton(labels.get(RootLabelName.SAVE_CALCULATE));
        DefaultButton buttonReset = new DefaultButton(labels.get(RootLabelName.RESET));
        DefaultButton buttonDelete = new DefaultButton(labels.get(RootLabelName.DELETE));

        buttonSave.addActionListener(e -> manager.saveChannel());
        buttonSaveAndCalculate.addActionListener(e -> manager.saveAndCalculateChannel());
        buttonReset.addActionListener(e -> manager.resetChannelInfo());
        buttonDelete.addActionListener(e -> manager.deleteChannel());

        this.add(buttonSave, new CellBuilder().x(0).y(0).build());
        this.add(buttonSaveAndCalculate, new CellBuilder().x(1).y(0).build());
        this.add(buttonReset, new CellBuilder().x(0).y(1).build());
        this.add(buttonDelete, new CellBuilder().x(1).y(1).build());
    }
}
