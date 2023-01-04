package ui.mainScreen;

import constants.Sort;
import converters.VariableConverter;
import org.apache.commons.validator.DateValidator;
import repository.MeasurementRepository;
import repository.PathElementRepository;
import repository.SensorRepository;
import repository.impl.*;
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
    public static final String CODE = "Код каналу";
    public static final String NAME = "Назва каналу";
    public static final String MEASUREMENT_NAME = "Вид вимірювання";
    public static final String MEASUREMENT_VALUE = "Одиниці вимірювання";
    public static final String DEPARTMENT = "Цех";
    public static final String AREA = "Ділянка";
    public static final String PROCESS = "Процесс";
    public static final String INSTALLATION = "Установка";
    public static final String DATE = "Дата останньої перевірки";
    public static final String FREQUENCY = "Міжконтрольний інтервал";
    public static final String TECHNOLOGY_NUMBER = "Технологічний номер";
    public static final String SENSOR_NAME = "Назва ПВП";
    public static final String SENSOR_TYPE = "Тип ПВП";
    public static final String PROTOCOL_NUMBER = "Номер протоколу(сертифікату)";
    public static final String REFERENCE = "Номер довідки";
    public static final String SUITABILITY = "Придатність";
    private static final String SUITABLE = "Придатний";
    private static final String START_SEARCH = "Шукати (Alt + Enter)";
    private static final String FINISH_SEARCH = "Відмінити";
    private static final String DEFAULT_DATE = "01.01.2020";
    private static final String DATE_TOOLTIP_TEXT = "Введіть дату у форматі \"день.місяць.рік\"";
    private static final String DEFAULT_TOOLTIP_TEXT = "Введіть слово(а) для пошуку";

    private static final int TEXT = 0;
    private static final int LIST = 1;
    private static final int CHECK = 3;

    public JButton buttonSearch;
    private StringsList field;
    private InputField valueText;
    private StringsList valueList;
    private CheckBox valueSuitability;

    private final PathElementRepository departmentRepository = DepartmentRepositorySQLite.getInstance();
    private final PathElementRepository areaRepository = AreaRepositorySQLite.getInstance();
    private final PathElementRepository processRepository = ProcessRepositorySQLite.getInstance();
    private final PathElementRepository installationRepository = InstallationRepositorySQLite.getInstance();
    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();
    private final MeasurementRepository measurementRepository = MeasurementRepositorySQLite.getInstance();

    private final DataTransfer dataTransfer = DataTransfer.getInstance();
    private final EventManager eventManager = EventManager.getInstance();

    private final EventSource eventSource;

    public SearchPanel(EventSource eventSource){
        super(new GridBagLayout());
        this.eventSource = eventSource;

        this.createElements();
        this.build(TEXT);
        this.setReactions();
    }

    private void createElements() {
        this.buttonSearch = new DefaultButton(START_SEARCH);

        this.field = new StringsList(CODE, NAME, MEASUREMENT_NAME, MEASUREMENT_VALUE,
                DEPARTMENT, AREA, PROCESS, INSTALLATION,
                DATE, FREQUENCY, TECHNOLOGY_NUMBER,
                SENSOR_NAME, SENSOR_TYPE, PROTOCOL_NUMBER,
                REFERENCE, SUITABILITY);
        this.field.setHorizontalAlignment(StringsList.CENTER);

        this.valueText = new InputFieldText();
        this.valueText.setHorizontalAlignment(InputField.CENTER);

        this.valueList = new StringsList(false);
        this.valueList.setHorizontalAlignment(StringsList.CENTER);

        this.valueSuitability = new CheckBox(SUITABLE);
        this.valueSuitability.setHorizontalAlignment(JCheckBox.CENTER);
    }

    private void setReactions() {
        this.buttonSearch.addActionListener(this.clickSearch);

        this.field.addItemListener(this.changeField);
        this.valueText.addFocusListener(this.textFocus);

        this.field.addKeyListener(keyListener);
        this.valueText.addKeyListener(keyListener);
        this.valueList.addKeyListener(keyListener);
        this.valueSuitability.addKeyListener(keyListener);
        this.buttonSearch.addKeyListener(keyListener);
    }

    private void build(int element) {
        this.removeAll();
        this.add(this.field, new CellBuilder().y(0).create());
        switch (element){
            default:
                this.add(this.valueText, new CellBuilder().y(1).create());
                break;
            case LIST:
                this.setModelToComboBox();
                this.add(this.valueList, new CellBuilder().y(1).create());
                break;
            case CHECK:
                this.add(this.valueSuitability, new CellBuilder().y(1).create());
                break;
        }

        this.add(this.buttonSearch, new CellBuilder().y(2).create());
    }

    private void setModelToComboBox(){
        if (this.field.getSelectedItem() != null){
            switch (this.field.getSelectedItem().toString()){
                case MEASUREMENT_NAME:
                    this.valueList.setModel(this.model_measurementsNames());
                    this.valueList.setEditable(false);
                    break;
                case MEASUREMENT_VALUE:
                    this.valueList.setModel(this.model_measurementsValues());
                    this.valueList.setEditable(false);
                    break;
                case DEPARTMENT:
                    this.valueList.setModel(this.model_departments());
                    this.valueList.setEditable(true);
                    break;
                case AREA:
                    this.valueList.setModel(this.model_areas());
                    this.valueList.setEditable(true);
                    break;
                case PROCESS:
                    this.valueList.setModel(this.model_processes());
                    this.valueList.setEditable(true);
                    break;
                case INSTALLATION:
                    this.valueList.setModel(this.model_installations());
                    this.valueList.setEditable(true);
                    break;
                case SENSOR_TYPE:
                    this.valueList.setModel(this.model_sensorsTypes());
                    this.valueList.setEditable(false);
                    break;
            }
        }
    }

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_ENTER){
                buttonSearch.doClick();
            }
        }
    };

    private ComboBoxModel<String>model_measurementsNames(){
        return new DefaultComboBoxModel<>(measurementRepository.getAllNames());
    }

    private ComboBoxModel<String>model_measurementsValues(){
        return new DefaultComboBoxModel<>(measurementRepository.getAllValues());
    }

    private ComboBoxModel<String>model_departments(){
        return new DefaultComboBoxModel<>(departmentRepository.getAll().toArray(new String[0]));
    }

    private ComboBoxModel<String>model_areas(){
        return new DefaultComboBoxModel<>(areaRepository.getAll().toArray(new String[0]));
    }

    private ComboBoxModel<String>model_processes(){
        return new DefaultComboBoxModel<>(processRepository.getAll().toArray(new String[0]));
    }

    private ComboBoxModel<String>model_installations(){
        return new DefaultComboBoxModel<>(installationRepository.getAll().toArray(new String[0]));
    }

    private ComboBoxModel<String>model_sensorsTypes(){
        return new DefaultComboBoxModel<>(sensorRepository.getAllTypes().toArray(new String[0]));
    }

    private final ActionListener clickSearch = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (field.getSelectedItem() != null) {
                if (buttonSearch.getText().equals(START_SEARCH)) {
                    String f = field.getSelectedItem().toString();
                    if (!f.equals(CODE)) setEnabled(false);

                    String listValue = valueList.getSelectedItem() == null ?
                            EMPTY :
                            valueList.getSelectedItem().toString();

                    dataTransfer.put(KEY_SEARCH_FIELD, f);
                    dataTransfer.put(KEY_SEARCH_VALUE_TEXT, valueText.getText());
                    dataTransfer.put(KEY_SEARCH_VALUE_LIST_ITEM, listValue);
                    dataTransfer.put(KEY_SEARCH_VALUE_BOOLEAN, valueSuitability.isSelected());

                    eventManager.runEvent(eventSource, CLICK_SEARCH_BUTTON_START);
                } else {
                    dataTransfer.clear();
                    eventManager.runEvent(eventSource, CLICK_SEARCH_BUTTON_END);
                    setEnabled(true);
                }
            }
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        buttonSearch.setText(enabled ? START_SEARCH : FINISH_SEARCH);
        if (field != null) field.setEnabled(enabled);
        if (valueText != null) valueText.setEnabled(enabled);
        if (valueList != null) valueList.setEnabled(enabled);
        if (valueSuitability != null) valueSuitability.setEnabled(enabled);
    }

    private final FocusListener textFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            valueText.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            String str = valueText.getText();
            if (field.getSelectedIndex() == Sort.FREQUENCY){
                valueText.setText(VariableConverter.doubleString(str));
            }else if (field.getSelectedIndex() == Sort.DATE){
                String str1 = VariableConverter.commasToDots(str);
                DateValidator dateValidator = DateValidator.getInstance();
                if (dateValidator.isValid(str1,"dd.MM.yyyy", false)){
                    valueText.setText(VariableConverter.stringToDateString(str));
                }else {
                    valueText.setText(DEFAULT_DATE);
                }
            }
        }
    };

    private final ItemListener changeField = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && field.getSelectedItem() != null){
                EventQueue.invokeLater(() -> {
                    switch (field.getSelectedItem().toString()){
                        default:
                            build(TEXT);
                            valueText.setToolTipText(DEFAULT_TOOLTIP_TEXT);
                            break;
                        case MEASUREMENT_NAME:
                        case MEASUREMENT_VALUE:
                        case SENSOR_TYPE:
                        case DEPARTMENT:
                        case AREA:
                        case PROCESS:
                        case INSTALLATION:
                            build(LIST);
                            break;
                        case DATE:
                            build(TEXT);
                            valueText.setToolTipText(DATE_TOOLTIP_TEXT);
                            break;
                        case SUITABILITY:
                            build(CHECK);
                            break;
                    }
                    MainScreen.getInstance().refresh();
                });
            }
        }
    };
}