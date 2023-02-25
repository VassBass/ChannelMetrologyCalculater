package service.channel.ui.swing;

import model.ui.DefaultButton;
import model.ui.builder.CellBuilder;
import service.channel.ChannelManager;

import javax.swing.*;
import java.awt.*;

public class SwingChannelButtonsPanel extends JPanel {
    private static final String INFO_BUTTON_TEXT = "Детальніше (D)";
    private static final String REMOVE_BUTTON_TEXT = "Видалити (R)";
    private static final String ADD_BUTTON_TEXT = "Додати (A)";
    private static final String CALCULATE_BUTTON_TEXT = "Розрахувати (C)";
    private static final String FOLDER_BUTTON_TEXT = "Сертифікати/Протоколи (F)";

    public SwingChannelButtonsPanel(final ChannelManager manager) {
        super(new GridBagLayout());

        JButton btnInfo = new DefaultButton(INFO_BUTTON_TEXT);
        JButton btnRemove = new DefaultButton(REMOVE_BUTTON_TEXT);
        JButton btnAdd = new DefaultButton(ADD_BUTTON_TEXT);
        JButton btnCalculate = new DefaultButton(CALCULATE_BUTTON_TEXT);
        JButton btnFolder = new DefaultButton(FOLDER_BUTTON_TEXT);

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
