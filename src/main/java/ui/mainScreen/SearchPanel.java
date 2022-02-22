package ui.mainScreen;

import application.Application;
import backgroundTasks.SearchChannels;
import constants.Sort;
import converters.VariableConverter;
import org.apache.commons.validator.DateValidator;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class SearchPanel extends JPanel {
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
    private static final String START_SEARCH = "Шукати";
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

    public SearchPanel(){
        super(new GridBagLayout());

        this.createElements();
        this.setReactions();
        this.build(TEXT);
    }

    private void createElements() {
        this.buttonSearch = new DefaultButton(START_SEARCH);

        this.field = new JComboBox<>(new String[]{
                NAME, MEASUREMENT_NAME, MEASUREMENT_VALUE,
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
        this.valueComboBox.setFocusable(false);
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
        switch (this.field.getSelectedIndex()){
            case 1:
                this.valueComboBox.setModel(this.model_measurementsNames());
                break;
            case 2:
                this.valueComboBox.setModel(this.model_measurementsValues());
                break;
            case 11:
                this.valueComboBox.setModel(this.model_sensorsTypes());
                break;
        }
    }

    private ComboBoxModel<String>model_measurementsNames(){
        return new DefaultComboBoxModel<>(Application.context.measurementService.getAllNames());
    }

    private ComboBoxModel<String>model_measurementsValues(){
        return new DefaultComboBoxModel<>(Application.context.measurementService.getAllValues());
    }

    private ComboBoxModel<String>model_sensorsTypes(){
        return new DefaultComboBoxModel<>(Application.context.sensorService.getAllTypes());
    }

    private final ActionListener clickSearch = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonSearch.getText().equals(START_SEARCH)){
                if (field != null) field.setEnabled(false);
                if (valueText != null) valueText.setEnabled(false);
                if (valueComboBox != null) valueComboBox.setEnabled(false);
                if (valueSuitability != null) valueSuitability.setEnabled(false);

                buttonSearch.setText(FINISH_SEARCH);
                int index = field.getSelectedIndex();
                switch (index){
                    default:
                        new SearchChannels().startSearch(index, valueText.getText());
                        break;
                    case 1:
                    case 2:
                    case 11:
                        new SearchChannels().startSearch(index, Objects.requireNonNull(valueComboBox.getSelectedItem()).toString());
                        break;
                    case 14:
                        new SearchChannels().startSearch(index, valueSuitability.isSelected());
                        break;
                }
            }else{
                Application.context.channelSorter.setOff();
                Application.context.mainScreen.setChannelsList(Application.context.channelService.getAll());
                if (field != null) field.setEnabled(true);
                if (valueText != null) valueText.setEnabled(true);
                if (valueComboBox != null) valueComboBox.setEnabled(true);
                if (valueSuitability != null) valueSuitability.setEnabled(true);
                buttonSearch.setText(START_SEARCH);
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
            if (e.getStateChange() == ItemEvent.SELECTED){
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        switch (field.getSelectedIndex()){
                            default:
                                build(TEXT);
                                valueText.setToolTipText(DEFAULT_TOOLTIP_TEXT);
                                break;
                            case Sort.MEASUREMENT_NAME:
                            case Sort.MEASUREMENT_VALUE:
                            case Sort.SENSOR_TYPE:
                                build(LIST);
                                break;
                            case Sort.DATE:
                                build(TEXT);
                                valueText.setToolTipText(DATE_TOOLTIP_TEXT);
                                break;
                            case Sort.SUITABILITY:
                                build(CHECK);
                                break;
                        }
                        Application.context.mainScreen.refresh();
                    }
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