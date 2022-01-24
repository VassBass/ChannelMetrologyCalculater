package ui.sensorsList.sensorInfo;

import application.Application;
import backgroundTasks.PutSensorInList;
import constants.MeasurementConstants;
import constants.SensorType;
import converters.ConverterUI;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import model.Sensor;
import ui.model.ButtonCell;
import ui.model.DefaultButton;
import ui.sensorsList.SensorsListDialog;
import ui.sensorsList.sensorInfo.complexElements.SensorRangePanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class SensorInfoDialog extends JDialog {
    private static final String SENSOR = "Первинний вимірювальний пристрій";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String TYPE = "Тип";
    private static final String NAME = "Назва";
    private static final String RANGE_OF_SENSOR = "Діапазон вимірювання ПВП";
    private static final String ERROR_FORMULA = "Формула для розрахунку похибки";
    private static final String TYPE_HINT = "Тип ПВП(Застосовується у протоколі)";
    private static final String NAME_HINT = "Назва ПВП для застосування в данній програмі(Не фігурує в документах)";
    private static final String INSERT = "Вставка";
    private static final String CANCEL = "Відміна";
    private static final String SAVE = "Зберегти";

    private final SensorsListDialog parent;
    private final SensorInfoDialog current;
    private final Sensor oldSensor;

    private ButtonCell labelMeasurement;
    private ButtonCell labelType;
    private ButtonCell labelName;
    private ButtonCell labelRange;
    private ButtonCell labelErrorFormula;
    private ButtonCell helpFormula1, helpFormula2, helpFormula3, helpFormula4, helpFormula5,
            helpFormula6, helpFormula7, helpFormula8, helpFormula9, helpFormula10, helpFormula11, helpFormula12;

    private JComboBox<String>measurementsList;
    private JTextField typeText;
    private JComboBox<String>typesList;
    private JTextField nameText;
    private SensorRangePanel rangePanel;
    private JTextField errorFormulaText;

    private JPopupMenu namePopupMenu;

    private JButton buttonCancel, buttonSave;

    public SensorInfoDialog(SensorsListDialog parent, Sensor oldSensor){
        super(parent, SENSOR, true);
        this.parent = parent;
        this.current = this;
        this.oldSensor = oldSensor;

        this.createElements();
        this.setInfo();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.labelMeasurement = new ButtonCell(true, TYPE_OF_MEASUREMENT);
        this.labelType = new ButtonCell(true, TYPE);
        this.labelName = new ButtonCell(true, NAME);
        this.labelRange = new ButtonCell(true, RANGE_OF_SENSOR);
        this.labelErrorFormula = new ButtonCell(true, ERROR_FORMULA);

        this.measurementsList = new JComboBox<>(Application.context.measurementsController.getAllNames());

        String[]consumptionTypes = new String[]{SensorType.YOKOGAWA, SensorType.ROSEMOUNT};
        this.typeText = new JTextField(10);
        this.typesList = new JComboBox<>(consumptionTypes);
        this.typeText.setToolTipText(TYPE_HINT);
        this.typesList.setToolTipText(TYPE_HINT);

        this.nameText = new JTextField(10);
        this.nameText.setToolTipText(NAME_HINT);
        this.namePopupMenu = new JPopupMenu(INSERT);
        this.nameText.setComponentPopupMenu(this.namePopupMenu);

        this.rangePanel = new SensorRangePanel();
        this.errorFormulaText = new JTextField(10);

        String toolTipText = "Приклад існує лише для ознайомлення з формою запису і не є реальною формулою.";

        String help1 = "Щоб написати формулу користуйтеся:";
        this.helpFormula1 = new ButtonCell(false, help1);
        this.helpFormula1.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula1.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula1.setBorderPainted(false);

        String help2 = "0...9, 0.1, 0,1 - Натуральні та дробні числа";
        this.helpFormula2 = new ButtonCell(false, help2);
        this.helpFormula2.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula2.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula2.setBorderPainted(false);

        String help3 = "() - Дужки, для розстановки послідовності дій";
        this.helpFormula3 = new ButtonCell(false, help3);
        this.helpFormula3.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula3.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula3.setBorderPainted(false);

        String help4 = "+, -, *, / - сума, різниця, множення, ділення";
        this.helpFormula4 = new ButtonCell(false, help4);
        this.helpFormula4.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula4.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula4.setBorderPainted(false);

        String help5 = "R - Діапазон вимірювання вимірювального каналу";
        this.helpFormula5 = new ButtonCell(false, help5);
        this.helpFormula5.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula5.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula5.setBorderPainted(false);

        String help6 = "convR - Діапазон вимірювання ПВП переконвертований під вимірювальну величину вимірювального каналу";
        this.helpFormula6 = new ButtonCell(false, help6);
        this.helpFormula6.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula6.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula6.setBorderPainted(false);

        String help7 = "r - Діапазон вимірювання ПВП";
        this.helpFormula7 = new ButtonCell(false, help7);
        this.helpFormula7.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula7.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula7.setBorderPainted(false);

        String help8 = "Приклад: ((0.005 * R) / r) + convR";
        this.helpFormula8 = new ButtonCell(false, help8);
        this.helpFormula8.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula8.setHorizontalAlignment(SwingConstants.CENTER);
        this.helpFormula8.setToolTipText(toolTipText);

        String help9 = "Дія №1 - 0.005 помножено на діапазон вимірювання вимірювального каналу(R)";
        this.helpFormula9 = new ButtonCell(false, help9);
        this.helpFormula9.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula9.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula9.setBorderPainted(false);
        this.helpFormula9.setToolTipText(toolTipText);

        String help10 = "Дія №2 - Результат першої дії поділено на діапазон вимірювання ПВП(r)";
        this.helpFormula10 = new ButtonCell(false, help10);
        this.helpFormula10.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula10.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula10.setBorderPainted(false);
        this.helpFormula10.setToolTipText(toolTipText);

        String help11 = "Дія №3 - До результату другої дії додати діапазон вимірювання ПВП переконвертований під вимірювальну";
        this.helpFormula11 = new ButtonCell(false, help11);
        this.helpFormula11.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula11.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula11.setBorderPainted(false);
        this.helpFormula11.setToolTipText(toolTipText);

        String help12 = "величину вимірювального каналу(convR)";
        this.helpFormula12 = new ButtonCell(false, help12);
        this.helpFormula12.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula12.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula12.setBorderPainted(false);
        this.helpFormula12.setToolTipText(toolTipText);

        this.buttonCancel = new DefaultButton(CANCEL);
        this.buttonSave = new DefaultButton(SAVE);
    }

    private void setInfo(){
        if (this.oldSensor != null){
            this.measurementsList.setSelectedItem(this.oldSensor.getMeasurement());
            if (this.oldSensor.getMeasurement().equals(MeasurementConstants.CONSUMPTION.getValue())){
                String type = this.oldSensor.getType();
                int spaceIndex = type.indexOf(" ");
                this.typeText.setText(type.substring(++spaceIndex));
                this.typesList.setSelectedItem(type.substring(0, --spaceIndex));
            }else {
                this.typeText.setText(this.oldSensor.getType());
            }
            this.nameText.setText(this.oldSensor.getName());
            this.rangePanel.setRange(this.oldSensor.getRangeMax(), this.oldSensor.getRangeMin());
            this.errorFormulaText.setText(this.oldSensor.getErrorFormula());

            if (!this.oldSensor.getMeasurement().equals(MeasurementConstants.TEMPERATURE.getValue())){
                this.rangePanel.setEnabled(false);
            }
            this.measurementsList.setEnabled(false);
        }
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonSave.addActionListener(this.clickSave);

        this.measurementsList.addItemListener(this.changeMeasurement);

        this.typeText.getDocument().addDocumentListener(this.typeChange);
    }

    private void build() {
        this.setSize(850,550);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private void refresh(){
        this.setVisible(false);
        this.setVisible(true);
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(current)) return;
            if (checkSensor()) {
                Sensor sensor = new Sensor();
                if (Objects.requireNonNull(measurementsList.getSelectedItem()).toString().equals(MeasurementConstants.CONSUMPTION.getValue())){
                    String type = Objects.requireNonNull(typesList.getSelectedItem()) + " " + typeText.getText();
                    sensor.setType(type);
                }else {
                    sensor.setType(typeText.getText());
                }
                sensor.setName(nameText.getText());
                sensor.setMeasurement(Objects.requireNonNull(measurementsList.getSelectedItem()).toString());
                if (rangePanel.isEnabled()) {
                    sensor.setRangeMin(rangePanel.getRangeMin());
                    sensor.setRangeMax(rangePanel.getRangeMax());
                    sensor.setValue(rangePanel.getValue());
                }else {
                    sensor.setRange(0D,0D);
                    sensor.setValue("");
                }
                sensor.setErrorFormula(errorFormulaText.getText());
                PutSensorInList putSensorInList = new PutSensorInList(parent, current, sensor);
                if (oldSensor == null){
                    putSensorInList.start();
                }else {
                    putSensorInList.start(oldSensor);
                }
            }
        }
    };

    private final ItemListener changeMeasurement = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED){
                build();
                rangePanel.setEnabled(Objects.requireNonNull(measurementsList.getSelectedItem()).toString().equals(MeasurementConstants.TEMPERATURE.getValue()));
                refresh();
            }
        }
    };

    private final DocumentListener typeChange = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            namePopupMenu.removeAll();
            JMenuItem type = new JMenuItem(typeText.getText());
            type.addActionListener(clickPaste);
            namePopupMenu.add(type);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            namePopupMenu.removeAll();
            JMenuItem type = new JMenuItem(typeText.getText());
            type.addActionListener(clickPaste);
            namePopupMenu.add(type);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {}
    };

    private final ActionListener clickPaste = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            nameText.setText(typeText.getText());
            nameText.requestFocus();
        }
    };

    private boolean checkSensor(){
        if (this.typeText.getText().length() == 0 &&
                !Objects.requireNonNull(this.measurementsList.getSelectedItem()).toString().equals(MeasurementConstants.CONSUMPTION.getValue())){
            JOptionPane.showMessageDialog(this, "Ви не ввели тип ПВП");
            return false;
        }else if (this.nameText.getText().length() == 0){
            JOptionPane.showMessageDialog(this, "Ви не ввели назву ПВП");
            return false;
        }else if (this.errorFormulaText.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Ви не ввели похибку ПВП");
            return false;
        }else if (!checkFormula(this.errorFormulaText.getText())){
            JOptionPane.showMessageDialog(this, "Для запису формули використовуйте тільки дозволені символи.");
            return false;
        }else {
            return true;
        }
    }

    private boolean checkFormula(String formula){
        String fo = VariableConverter.commasToDots(formula);
        Function f = new Function("At(R,r,convR) = " + fo);
        Argument R = new Argument("R = 1");
        Argument r = new Argument("r = 1");
        Argument convR = new Argument("convR = 1");
        Expression expression = new Expression("At(R,r,convR)", f,R,r,convR);
        return !Double.isNaN(expression.calculate());
    }

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(labelMeasurement, new Cell(0, 0,1));
            this.add(measurementsList, new Cell(1, 0,2));
            this.add(labelType, new Cell(0, 1,1));
            if (Objects.requireNonNull(measurementsList.getSelectedItem()).toString().equals(MeasurementConstants.CONSUMPTION.getValue())){
                this.add(typesList, new Cell(1,1,1));
                this.add(typeText, new Cell(2,1,1));
            }else {
                this.add(typeText, new Cell(1, 1, 2));
            }
            this.add(labelName, new Cell(0, 2,1));
            this.add(nameText, new Cell(1, 2,2));
            this.add(labelRange, new Cell(0, 3,1));
            this.add(rangePanel, new Cell(1, 3,2));
            this.add(labelErrorFormula, new Cell(0, 4,1));
            this.add(errorFormulaText, new Cell(1, 4,2));

            this.add(helpFormula1, new Cell(0,5,3));
            this.add(helpFormula2, new Cell(0,6,3));
            this.add(helpFormula3, new Cell(0,7,3));
            this.add(helpFormula4, new Cell(0,8,3));
            this.add(helpFormula5, new Cell(0,9,3));
            this.add(helpFormula6, new Cell(0,10,3));
            this.add(helpFormula7, new Cell(0,11,3));
            this.add(helpFormula8, new Cell(0,12,3));
            this.add(helpFormula9, new Cell(0,13,3));
            this.add(helpFormula10, new Cell(0,14,3));
            this.add(helpFormula11, new Cell(0,15,3));
            this.add(helpFormula12, new Cell(0,16,3));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonCancel);
            buttonsPanel.add(buttonSave);
            this.add(buttonsPanel, new Cell(0,17,3));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width){
                super();

                this.weightx = 1D;
                this.weighty = 1D;
                this.fill = BOTH;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}