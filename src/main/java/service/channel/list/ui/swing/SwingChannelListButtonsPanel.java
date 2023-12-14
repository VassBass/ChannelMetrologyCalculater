package service.channel.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultButton;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.channel.list.ChannelListManager;

import javax.swing.*;
import java.io.File;
import java.util.Map;

public class SwingChannelListButtonsPanel extends DefaultPanel {
    private static final String INFO_BUTTON_KEY = "(D)";
    private static final String REMOVE_BUTTON_KEY = "(R)";
    private static final String ADD_BUTTON_KEY = "(A)";
    private static final String CALCULATE_BUTTON_KEY = "(C)";
    private static final String FOLDER_BUTTON_KEY = "(F)";

    public SwingChannelListButtonsPanel(final ChannelListManager manager) {
        super();
        Map<String, String> rootLabels = Labels.getRootLabels();

        JButton btnInfo = new DefaultButton(rootLabels.get(RootLabelName.DETAILS) + Labels.SPACE + INFO_BUTTON_KEY);
        JButton btnRemove = new DefaultButton(rootLabels.get(RootLabelName.DELETE) + Labels.SPACE + REMOVE_BUTTON_KEY);
        JButton btnAdd = new DefaultButton(rootLabels.get(RootLabelName.ADD) + Labels.SPACE + ADD_BUTTON_KEY);
        JButton btnCalculate = new DefaultButton(rootLabels.get(RootLabelName.CALCULATE) + Labels.SPACE + CALCULATE_BUTTON_KEY);
        JButton btnFolder = new DefaultButton(rootLabels.get(RootLabelName.CERTIFICATES) +
                        File.separator +
                        rootLabels.get(RootLabelName.PROTOCOLS) +
                        Labels.SPACE +
                        FOLDER_BUTTON_KEY);

        btnInfo.addActionListener(e -> manager.showChannelInfo());
        btnRemove.addActionListener(e -> manager.removeChannel());
        btnAdd.addActionListener(e -> manager.addChannel());
        btnCalculate.addActionListener(e -> manager.calculateChannel());
        btnFolder.addActionListener(e -> manager.openChannelCertificateFolder());

        this.add(btnRemove, new CellBuilder().coordinates(0, 0).width(1).build());
        this.add(btnAdd, new CellBuilder().coordinates(1, 0).width(1).build());
        this.add(btnInfo, new CellBuilder().coordinates(0, 1).width(2).build());
        this.add(btnFolder, new CellBuilder().coordinates(0, 2).width(1).build());
        this.add(btnCalculate, new CellBuilder().coordinates(1, 2).width(1).build());
    }
}
