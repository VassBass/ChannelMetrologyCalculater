package ui.mainScreen;

import backgroundTasks.CheckChannel;
import backgroundTasks.SearchChannels;
import constants.Sort;
import converters.VariableConverter;
import org.apache.commons.validator.DateValidator;
import repository.PathElementRepository;
import repository.impl.*;
import service.ChannelSorter;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SearchPanel extends JPanel {
    private static final String CODE = "Код каналу";
    private static final String NAME = "Назва каналу";
    private static final String MEASUREMENT_NAME = "Вид вимірювання";
    private static final String MEASUREMENT_VALUE = "Одиниці вимірювання";
    private static final String DEPARTMENT = "Цех";
    private static final String AREA = "Ділянка";
    private static final String PROCESS = "Процесс";
    private static final String INSTALLATION = "Установка";
    private static final String DATE = "Дата останньої перевірки";
    private static final String FREQUENCY = "Міжконтрольний інтервал";
    private static final String TECHNOLOGY_NUMBER = "Технологічний номер";
    private static final String SENSOR_NAME = "Назва ПВП";
    private static final String SENSOR_TYPE = "Тип ПВП";
    private static final String PROTOCOL_NUMBER = "Номер протоколу(сертифікату)";
    private static final String REFERENCE = "Номер довідки";
    private static final String SUITABILITY = "Придатність";
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
    private JComboBox<String>field;
    private JTextField valueText;
    private JComboBox<String>valueComboBox;
    private JCheckBox valueSuitability;

    private final PathElementRepository departmentRepository = DepartmentRepositorySQLite.getInstance();
    private final PathElementRepository areaRepository = AreaRepositorySQLite.getInstance();
    private final PathElementRepository processRepository = ProcessRepositorySQLite.getInstance();
    private final PathElementRepository installationRepository = InstallationRepositorySQLite.getInstance();

    public SearchPanel(){
        super(new GridBagLayout());

        this.createElements();
        this.build(TEXT);
        this.setReactions();
    }

    private void createElements() {
        this.buttonSearch = new DefaultButton(START_SEARCH);

        this.field = new JComboBox<>(new String[]{
                CODE, NAME, MEASUREMENT_NAME, MEASUREMENT_VALUE,
                DEPARTMENT,AREA, PROCESS, INSTALLATION,
                DATE, FREQUENCY, TECHNOLOGY_NUMBER,
                SENSOR_NAME, SENSOR_TYPE, PROTOCOL_NUMBER,
                REFERENCE, SUITABILITY
        });
        ((JLabel)this.field.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        this.field.setBackground(Color.BLACK);
        this.field.setForeground(Color.WHITE);
        this.field.setFocusable(false);

        this.valueText = new JTextField(10);
        this.valueText.setHorizontalAlignment(JTextField.CENTER);

        this.valueComboBox = new JComboBox<>();
        ((JLabel)this.valueComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        this.valueComboBox.setBackground(Color.WHITE);
        this.valueComboBox.setForeground(Color.BLACK);

        this.valueSuitability = new JCheckBox(SUITABLE);
        this.valueSuitability.setHorizontalAlignment(JCheckBox.CENTER);
        this.valueSuitability.setFocusPainted(false);
    }

    private void setReactions() {
        this.buttonSearch.addActionListener(this.clickSearch);

        this.field.addItemListener(this.changeField);
        this.valueText.addFocusListener(this.textFocus);

        this.field.addKeyListener(keyListener);
        this.valueText.addKeyListener(keyListener);
        this.valueComboBox.addKeyListener(keyListener);
        this.valueSuitability.addKeyListener(keyListener);
        this.buttonSearch.addKeyListener(keyListener);
    }

    private void build(int element) {
        this.removeAll();
        this.add(this.field, new Cell(0));
        switch (element){
            default:
                this.add(this.valueText, new Cell(1));
                break;
            case LIST:
                this.setModelToComboBox();
                this.add(this.valueComboBox, new Cell(1));
                break;
            case CHECK:
                this.add(this.valueSuitability, new Cell(1));
                break;
        }

        this.add(this.buttonSearch, new Cell(2));
    }

    private void setModelToComboBox(){
        if (this.field.getSelectedItem() != null){
            switch (this.field.getSelectedItem().toString()){
                case MEASUREMENT_NAME:
                    this.valueComboBox.setModel(this.model_measurementsNames());
                    this.valueComboBox.setFocusable(false);
                    this.valueComboBox.setEditable(false);
                    break;
                case MEASUREMENT_VALUE:
                    this.valueComboBox.setModel(this.model_measurementsValues());
                    this.valueComboBox.setFocusable(false);
                    this.valueComboBox.setEditable(false);
                    break;
                case DEPARTMENT:
                    this.valueComboBox.setModel(this.model_departments());
                    this.valueComboBox.setFocusable(true);
                    this.valueComboBox.setEditable(true);
                    break;
                case AREA:
                    this.valueComboBox.setModel(this.model_areas());
                    this.valueComboBox.setFocusable(true);
                    this.valueComboBox.setEditable(true);
                    break;
                case PROCESS:
                    this.valueComboBox.setModel(this.model_processes());
                    this.valueComboBox.setFocusable(true);
                    this.valueComboBox.setEditable(true);
                    break;
                case INSTALLATION:
                    this.valueComboBox.setModel(this.model_installations());
                    this.valueComboBox.setFocusable(true);
                    this.valueComboBox.setEditable(true);
                    break;
                case SENSOR_TYPE:
                    this.valueComboBox.setModel(this.model_sensorsTypes());
                    this.valueComboBox.setFocusable(false);
                    this.valueComboBox.setEditable(false);
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
        return new DefaultComboBoxModel<>(MeasurementRepositorySQLite.getInstance().getAllNames());
    }

    private ComboBoxModel<String>model_measurementsValues(){
        return new DefaultComboBoxModel<>(MeasurementRepositorySQLite.getInstance().getAllValues());
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
        return new DefaultComboBoxModel<>(SensorRepositorySQLite.getInstance().getAllTypes().toArray(new String[0]));
    }

    private final ActionListener clickSearch = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (field.getSelectedItem() != null) {
                if (buttonSearch.getText().equals(START_SEARCH)) {
                    String f = field.getSelectedItem().toString();
                    if (!f.equals(CODE)) {
                        if (field != null) field.setEnabled(false);
                        if (valueText != null) valueText.setEnabled(false);
                        if (valueComboBox != null) valueComboBox.setEnabled(false);
                        if (valueSuitability != null) valueSuitability.setEnabled(false);

                        buttonSearch.setText(FINISH_SEARCH);
                    }
                    String value = valueComboBox.getSelectedItem() == null ? null : valueComboBox.getSelectedItem().toString();
                    switch (f) {
                        case CODE:
                            new CheckChannel(MainScreen.getInstance(), valueText.getText()).start();
                            break;
                        case NAME:
                            new SearchChannels().startSearch(Sort.NAME, valueText.getText());
                            break;
                        case MEASUREMENT_NAME:
                            new SearchChannels().startSearch(Sort.MEASUREMENT_NAME, value);
                            break;
                        case MEASUREMENT_VALUE:
                            new SearchChannels().startSearch(Sort.MEASUREMENT_VALUE, value);
                            break;
                        case DEPARTMENT:
                            new SearchChannels().startSearch(Sort.DEPARTMENT, value);
                            break;
                        case AREA:
                            new SearchChannels().startSearch(Sort.AREA, value);
                            break;
                        case PROCESS:
                            new SearchChannels().startSearch(Sort.PROCESS, value);
                            break;
                        case INSTALLATION:
                            new SearchChannels().startSearch(Sort.INSTALLATION, value);
                            break;
                        case DATE:
                            new SearchChannels().startSearch(Sort.DATE, valueText.getText());
                            break;
                        case FREQUENCY:
                            new SearchChannels().startSearch(Sort.FREQUENCY, valueText.getText());
                            break;
                        case TECHNOLOGY_NUMBER:
                            new SearchChannels().startSearch(Sort.TECHNOLOGY_NUMBER, valueText.getText());
                            break;
                        case SENSOR_NAME:
                            new SearchChannels().startSearch(Sort.SENSOR_NAME, valueText.getText());
                            break;
                        case SENSOR_TYPE:
                            new SearchChannels().startSearch(Sort.SENSOR_TYPE, value);
                            break;
                        case PROTOCOL_NUMBER:
                            new SearchChannels().startSearch(Sort.PROTOCOL_NUMBER, valueText.getText());
                            break;
                        case REFERENCE:
                            new SearchChannels().startSearch(Sort.REFERENCE, valueText.getText());
                            break;
                        case SUITABILITY:
                            new SearchChannels().startSearch(Sort.SUITABILITY, valueSuitability.isSelected());
                            break;
                    }
                } else {
                    ChannelSorter.getInstance().setOff();
                    MainScreen.getInstance().setChannelsList(new ArrayList<>(ChannelRepositorySQLite.getInstance().getAll()));
                    if (field != null) field.setEnabled(true);
                    if (valueText != null) valueText.setEnabled(true);
                    if (valueComboBox != null) valueComboBox.setEnabled(true);
                    if (valueSuitability != null) valueSuitability.setEnabled(true);
                    buttonSearch.setText(START_SEARCH);
                }
            }
        }
    };

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

    private static class Cell extends GridBagConstraints{

        protected Cell(int y){
            super();

            this.fill = BOTH;
            this.weightx = 1.0;
            this.weighty = 1.0;

            this.gridx = 0;
            this.gridy = y;
        }
    }
}