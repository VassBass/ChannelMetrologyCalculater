package service.channel.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import service.channel.list.ChannelListManager;

import javax.swing.*;
import java.io.File;
import java.util.Map;

public class SwingMenuChannelList extends JMenu {
    private static final Map<String, String> labels = Labels.getRootLabels();

    public SwingMenuChannelList(final ChannelListManager manager){
        super(labels.get(RootLabelName.CHANNEL));

        JMenuItem btnAdd = new JMenuItem(labels.get(RootLabelName.ADD));
        JMenuItem btnCalculate = new JMenuItem(labels.get(RootLabelName.CALCULATE));
        JMenuItem btnInfo = new JMenuItem(labels.get(RootLabelName.DETAILS));
        JMenuItem btnRemove = new JMenuItem(labels.get(RootLabelName.DELETE));
        JMenuItem btnFolder = new JMenuItem(
                labels.get(RootLabelName.CERTIFICATES) +
                        File.separator +
                        labels.get(RootLabelName.PROTOCOLS)
                );

        btnInfo.addActionListener(e -> manager.showChannelInfo());
        btnAdd.addActionListener(e -> manager.addChannel());
        btnRemove.addActionListener(e -> manager.removeChannel());
        btnCalculate.addActionListener(e -> manager.calculateChannel());
        btnFolder.addActionListener(e -> manager.openChannelCertificateFolder());

        this.add(btnAdd);
        this.add(btnRemove);
        this.addSeparator();
        this.add(btnInfo);
        this.add(btnCalculate);
        this.addSeparator();
        this.add(btnFolder);
    }
}