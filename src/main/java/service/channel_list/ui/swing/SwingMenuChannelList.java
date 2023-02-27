package service.channel_list.ui.swing;

import service.channel_list.ChannelListManager;

import javax.swing.*;

public class SwingMenuChannelList extends JMenu {
    private static final String HEADER_TEXT = "Канал";
    private static final String ADD_BUTTON_TEXT = "Додати";
    private static final String CALCULATE_BUTTON_TEXT = "Розрахувати";
    private static final String INFO_BUTTON_TEXT = "Детальніше";
    private static final String REMOVE_BUTTON_TEXT = "Видалити";
    private static final String FOLDER_BUTTON_TEXT = "Сертифікати/Протоколи";

    public SwingMenuChannelList(final ChannelListManager manager){
        super(HEADER_TEXT);

        JMenuItem btnAdd = new JMenuItem(ADD_BUTTON_TEXT);
        JMenuItem btnCalculate = new JMenuItem(CALCULATE_BUTTON_TEXT);
        JMenuItem btnInfo = new JMenuItem(INFO_BUTTON_TEXT);
        JMenuItem btnRemove = new JMenuItem(REMOVE_BUTTON_TEXT);
        JMenuItem btnFolder = new JMenuItem(FOLDER_BUTTON_TEXT);

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