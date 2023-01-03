package ui.mainScreen.menu;

import ui.event.EventManager;
import ui.event.EventSource;

import javax.swing.*;

import static ui.event.EventManager.*;
import static ui.event.eventManagers.mainScreen.MainScreenEventManager.CLICK_CALCULATE_BUTTON;
import static ui.event.eventManagers.mainScreen.MainScreenEventManager.CLICK_FOLDER_BUTTON;

public class MenuChannel extends JMenu {
    private static final String HEADER_TEXT = "Канал";
    private static final String ADD_BUTTON_TEXT = "Додати";
    private static final String CALCULATE_BUTTON_TEXT = "Розрахувати";
    private static final String INFO_BUTTON_TEXT = "Детальніше";
    private static final String REMOVE_BUTTON_TEXT = "Видалити";
    private static final String FOLDER_BUTTON_TEXT = "Сертифікати/Протоколи";

    public MenuChannel(final EventSource eventSource){
        super(HEADER_TEXT);
        final EventManager eventManager = EventManager.getInstance();

        JMenuItem btnAdd = new JMenuItem(ADD_BUTTON_TEXT);
        JMenuItem btnCalculate = new JMenuItem(CALCULATE_BUTTON_TEXT);
        JMenuItem btnInfo = new JMenuItem(INFO_BUTTON_TEXT);
        JMenuItem btnRemove = new JMenuItem(REMOVE_BUTTON_TEXT);
        JMenuItem btnFolder = new JMenuItem(FOLDER_BUTTON_TEXT);

        btnInfo.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_INFO_BUTTON));
        btnAdd.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_ADD_BUTTON));
        btnRemove.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_REMOVE_BUTTON));
        btnCalculate.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_CALCULATE_BUTTON));
        btnFolder.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_FOLDER_BUTTON));

        this.add(btnAdd);
        this.add(btnRemove);
        this.addSeparator();
        this.add(btnInfo);
        this.add(btnCalculate);
        this.addSeparator();
        this.add(btnFolder);
    }
}