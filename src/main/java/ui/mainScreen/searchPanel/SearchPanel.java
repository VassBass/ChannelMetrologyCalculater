package ui.mainScreen.searchPanel;

import service.DataTransfer;
import ui.event.EventManager;
import ui.event.EventSource;
import ui.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ui.event.eventManagers.mainScreen.MainScreenEventManager.*;

public class SearchPanel extends JPanel {
    public static final String TEXT_CODE = "Код каналу";
    public static final String TEXT_NAME = "Назва каналу";
    public static final String TEXT_MEASUREMENT_NAME = "Вид вимірювання";
    public static final String TEXT_MEASUREMENT_VALUE = "Одиниці вимірювання";
    public static final String TEXT_DEPARTMENT = "Цех";
    public static final String TEXT_AREA = "Ділянка";
    public static final String TEXT_PROCESS = "Процесс";
    public static final String TEXT_INSTALLATION = "Установка";
    public static final String TEXT_DATE = "Дата останньої перевірки";
    public static final String TEXT_FREQUENCY = "Міжконтрольний інтервал";
    public static final String TEXT_TECHNOLOGY_NUMBER = "Технологічний номер";
    public static final String TEXT_SENSOR_NAME = "Назва ПВП";
    public static final String TEXT_SENSOR_TYPE = "Тип ПВП";
    public static final String TEXT_PROTOCOL_NUMBER = "Номер протоколу(сертифікату)";
    public static final String TEXT_REFERENCE = "Номер довідки";
    public static final String TEXT_SUITABILITY = "Придатність";
    private static final String TEXT_SUITABLE = "Придатний";
    protected static final String TEXT_START_SEARCH = "Шукати (Alt + Enter)";
    private static final String TEXT_FINISH_SEARCH = "Відмінити";
    protected static final String TEXT_DEFAULT_DATE = "01.01.2020";
    protected static final String TEXT_DATE_TOOLTIP = "Введіть дату у форматі \"день.місяць.рік\"";
    protected static final String TEXT_DEFAULT_TOOLTIP = "Введіть слово(а) для пошуку";

    private static final String[] SEARCH_FIELDS = {
            TEXT_CODE, TEXT_NAME, TEXT_MEASUREMENT_NAME, TEXT_MEASUREMENT_VALUE, TEXT_DEPARTMENT, TEXT_AREA, TEXT_PROCESS,
            TEXT_INSTALLATION, TEXT_DATE, TEXT_FREQUENCY, TEXT_TECHNOLOGY_NUMBER, TEXT_SENSOR_NAME, TEXT_SENSOR_TYPE,
            TEXT_PROTOCOL_NUMBER, TEXT_REFERENCE, TEXT_SUITABILITY
    };

    protected static final int TEXT_MODE = 0;
    protected static final int LIST_MODE = 1;
    protected static final int CHECK_MODE = 3;

    public DefaultButton btnSearch;
    protected StringsList listSearchFields;
    protected InputField inputValue;
    protected StringsList listValues;
    protected CheckBox checkSuitability;

    private final DataTransfer dataTransfer = DataTransfer.getInstance();
    private final EventManager eventManager = EventManager.getInstance();

    private final EventSource eventSource;
    private final SearchPanelVT searchPanelVT;

    public SearchPanel(EventSource eventSource){
        super(new GridBagLayout());
        this.eventSource = eventSource;
        this.searchPanelVT = new SearchPanelVT(this);

        this.btnSearch = new DefaultButton(TEXT_START_SEARCH);
        this.listSearchFields = new StringsList(StringsList.CENTER, SEARCH_FIELDS);
        this.inputValue = new InputFieldText(InputField.CENTER);
        this.listValues = new StringsList(false, StringsList.CENTER);
        this.checkSuitability = new CheckBox(TEXT_SUITABLE, CheckBox.CENTER);

        this.add(this.listSearchFields, new CellBuilder().y(0).create());
        this.add(this.inputValue, new CellBuilder().y(1).create());
        this.add(this.btnSearch, new CellBuilder().y(2).create());

        this.btnSearch.addActionListener(clickButtonSearch);
        this.listSearchFields.addItemListener(e -> searchPanelVT.changeField());
        this.inputValue.addFocusListener(inputValueFocusListener);
        this.listSearchFields.addKeyListener(keyListener);
        this.inputValue.addKeyListener(keyListener);
        this.listValues.addKeyListener(keyListener);
        this.checkSuitability.addKeyListener(keyListener);
        this.btnSearch.addKeyListener(keyListener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        btnSearch.setText(enabled ? TEXT_START_SEARCH : TEXT_FINISH_SEARCH);
        if (listSearchFields != null) listSearchFields.setEnabled(enabled);
        if (inputValue != null) inputValue.setEnabled(enabled);
        if (listValues != null) listValues.setEnabled(enabled);
        if (checkSuitability != null) checkSuitability.setEnabled(enabled);
    }

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_ENTER){
                btnSearch.doClick();
            }
        }
    };

    private final ActionListener clickButtonSearch = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (listSearchFields.getSelectedItem() != null) {
                boolean start = btnSearch.getText().equals(TEXT_START_SEARCH);
                searchPanelVT.clickSearch(start);
                if (start) {
                    String value = listValues.getSelectedItem() == null ?
                            EMPTY :
                            listValues.getSelectedItem().toString();

                    dataTransfer.put(KEY_SEARCH_FIELD, listSearchFields.getSelectedItem().toString());
                    dataTransfer.put(KEY_SEARCH_VALUE_TEXT, inputValue.getText());
                    dataTransfer.put(KEY_SEARCH_VALUE_LIST_ITEM, value);
                    dataTransfer.put(KEY_SEARCH_VALUE_BOOLEAN, checkSuitability.isSelected());

                    eventManager.runEvent(eventSource, CLICK_SEARCH_BUTTON_START);
                } else {
                    dataTransfer.clear();
                    eventManager.runEvent(eventSource, CLICK_SEARCH_BUTTON_END);
                }
            }
        }
    };

    private final FocusListener inputValueFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            inputValue.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            searchPanelVT.valueLostFocus();
        }
    };
}