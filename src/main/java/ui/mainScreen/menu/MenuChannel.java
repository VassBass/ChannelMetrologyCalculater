package ui.mainScreen.menu;

import service.MainScreenEventListener;
import ui.event.EventDataSource;
import ui.event.SingleEventDataSource;
import ui.mainScreen.channelTable.ChannelTable;

import javax.annotation.Nonnull;
import javax.swing.*;

import static ui.mainScreen.MainScreen.KEY_CHANNEL;

public class MenuChannel extends JMenu {
    private static final String HEADER_TEXT = "Канал";
    private static final String ADD_BUTTON_TEXT = "Додати";
    private static final String CALCULATE_BUTTON_TEXT = "Розрахувати";
    private static final String INFO_BUTTON_TEXT = "Детальніше";
    private static final String REMOVE_BUTTON_TEXT = "Видалити";
    private static final String FOLDER_BUTTON_TEXT = "Сертифікати/Протоколи";

    public MenuChannel(@Nonnull final MainScreenEventListener eventService,
                       @Nonnull final ChannelTable channelTable){
        super(HEADER_TEXT);

        JMenuItem btnAdd = new JMenuItem(ADD_BUTTON_TEXT);
        JMenuItem btnCalculate = new JMenuItem(CALCULATE_BUTTON_TEXT);
        JMenuItem btnInfo = new JMenuItem(INFO_BUTTON_TEXT);
        JMenuItem btnRemove = new JMenuItem(REMOVE_BUTTON_TEXT);
        JMenuItem btnFolder = new JMenuItem(FOLDER_BUTTON_TEXT);

        btnInfo.addActionListener(eventService
                .clickInfoButton(new SingleEventDataSource<>(KEY_CHANNEL, channelTable.getSelectedChannel())));
        btnAdd.addActionListener(eventService
                .clickAddButton(EventDataSource.empty));
        btnRemove.addActionListener(eventService
                .clickRemoveButton(new SingleEventDataSource<>(KEY_CHANNEL, channelTable.getSelectedChannel())));
        btnCalculate.addActionListener(eventService
                .clickCalculateButton(new SingleEventDataSource<>(KEY_CHANNEL, channelTable.getSelectedChannel())));
        btnFolder.addActionListener(eventService
                .clickOpenFolderButton(EventDataSource.empty));

        this.add(btnAdd);
        this.add(btnRemove);
        this.addSeparator();
        this.add(btnInfo);
        this.add(btnCalculate);
        this.addSeparator();
        this.add(btnFolder);
    }
}