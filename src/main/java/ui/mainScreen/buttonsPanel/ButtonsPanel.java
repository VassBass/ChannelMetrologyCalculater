package ui.mainScreen.buttonsPanel;

import service.MainScreenEventListener;
import ui.event.EventDataSource;
import ui.event.SingleEventDataSource;
import ui.mainScreen.channelTable.ChannelTable;
import ui.model.CellBuilder;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;

import static ui.mainScreen.MainScreen.KEY_CHANNEL;

public class ButtonsPanel extends JPanel {
    private static final String INFO_BUTTON_TEXT = "Детальніше (D)";
    private static final String REMOVE_BUTTON_TEXT = "Видалити (R)";
    private static final String ADD_BUTTON_TEXT = "Додати (A)";
    private static final String CALCULATE_BUTTON_TEXT = "Розрахувати (C)";
    private static final String FOLDER_BUTTON_TEXT = "Сертифікати/Протоколи (F)";

    public ButtonsPanel(MainScreenEventListener eventListener,
                        ChannelTable channelTable){
        super(new GridBagLayout());

        JButton btnInfo = new DefaultButton(INFO_BUTTON_TEXT);
        JButton btnRemove = new DefaultButton(REMOVE_BUTTON_TEXT);
        JButton btnAdd = new DefaultButton(ADD_BUTTON_TEXT);
        JButton btnCalculate = new DefaultButton(CALCULATE_BUTTON_TEXT);
        JButton btnFolder = new DefaultButton(FOLDER_BUTTON_TEXT);

        btnInfo.addActionListener(eventListener.clickInfoButton(
                new SingleEventDataSource<>(KEY_CHANNEL, channelTable.getSelectedChannel())));
        btnRemove.addActionListener(eventListener.clickRemoveButton(
                new SingleEventDataSource<>(KEY_CHANNEL, channelTable.getSelectedChannel())));
        btnAdd.addActionListener(eventListener.clickAddButton(EventDataSource.empty));
        btnCalculate.addActionListener(eventListener.clickCalculateButton(
                new SingleEventDataSource<>(KEY_CHANNEL, channelTable.getSelectedChannel())));
        btnFolder.addActionListener(eventListener.clickOpenFolderButton(EventDataSource.empty));

        this.add(btnRemove, new CellBuilder().coordinates(0,0).width(1).create());
        this.add(btnAdd, new CellBuilder().coordinates(1,0).width(1).create());
        this.add(btnInfo, new CellBuilder().coordinates(0,1).width(2).create());
        this.add(btnFolder, new CellBuilder().coordinates(0,2).width(1).create());
        this.add(btnCalculate, new CellBuilder().coordinates(1,2).width(1).create());
    }
}