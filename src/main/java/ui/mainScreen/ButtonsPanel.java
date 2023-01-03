package ui.mainScreen;

import ui.event.EventManager;
import ui.event.EventSource;
import ui.model.*;

import javax.swing.*;
import java.awt.*;

import static ui.event.eventManagers.mainScreen.MainScreenEventManager.CLICK_CALCULATE_BUTTON;
import static ui.event.eventManagers.mainScreen.MainScreenEventManager.CLICK_FOLDER_BUTTON;

public class ButtonsPanel extends JPanel {
    private static final String INFO_BUTTON_TEXT = "Детальніше (D)";
    private static final String REMOVE_BUTTON_TEXT = "Видалити (R)";
    private static final String ADD_BUTTON_TEXT = "Додати (A)";
    private static final String CALCULATE_BUTTON_TEXT = "Розрахувати (C)";
    private static final String FOLDER_BUTTON_TEXT = "Сертифікати/Протоколи (F)";

    private final EventManager eventManager = EventManager.getInstance();

    public ButtonsPanel(EventSource eventSource){
        super(new GridBagLayout());

        JButton btnInfo = new InfoButton(eventSource, INFO_BUTTON_TEXT);
        JButton btnRemove = new RemoveButton(eventSource, REMOVE_BUTTON_TEXT);
        JButton btnAdd = new AddButton(eventSource, ADD_BUTTON_TEXT);
        JButton btnCalculate = new DefaultButton(CALCULATE_BUTTON_TEXT);
        JButton btnFolder = new DefaultButton(FOLDER_BUTTON_TEXT);

        btnCalculate.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_CALCULATE_BUTTON));
        btnFolder.addActionListener(e -> eventManager.runEvent(eventSource, CLICK_FOLDER_BUTTON));

        this.add(btnRemove, new CellBuilder().coordinates(0,0).width(1).create());
        this.add(btnAdd, new CellBuilder().coordinates(1,0).width(1).create());
        this.add(btnInfo, new CellBuilder().coordinates(0,1).width(2).create());
        this.add(btnFolder, new CellBuilder().coordinates(0,2).width(1).create());
        this.add(btnCalculate, new CellBuilder().coordinates(1,2).width(1).create());
    }
}