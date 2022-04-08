package ui.sensorsList.sensorInfo;

import application.Application;
import backgroundTasks.PutSensorInList;
import converters.ConverterUI;
import converters.VariableConverter;
import model.Measurement;
import model.Sensor;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import ui.channelInfo.DialogChannel;
import ui.model.ButtonCell;
import ui.model.DefaultButton;
import ui.sensorsList.SensorsListDialog;
import ui.sensorsList.sensorInfo.complexElements.SensorRangePanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;
import java.util.logging.Logger;

public class SensorInfoDialog extends JDialog {
    private static final Logger LOGGER = Logger.getLogger(SensorInfoDialog.class.getName());

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

    private String from_R(double percent){
        return percent + "% від діапазону вимірювального каналу";
    }
    private String from_r(double percent){
        return percent + "% від діапазону вимірювання ПВП";
    }
    private String from_convR(double percent){
        return percent + "% від діапазону вимірювання ПВП, переконвертованого під вимірювальну величину вимірювального каналу";
    }

    private final SensorsListDialog parent;
    private final DialogChannel dialogChannel;
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

    private JPopupMenu namePopupMenu, errorPopupMenu;

    private JButton buttonCancel, buttonSave;

    private String fullType(){
        StringBuilder builder = new StringBuilder();
        if (measurementsList != null && typeText != null) {
            if (measurementsList.getSelectedItem() != null
                    && measurementsList.getSelectedItem().toString().equals(Measurement.CONSUMPTION)) {
                if (typesList.getSelectedItem() != null){
                    String pre = typesList.getSelectedItem().toString();
                    builder.append(pre);
                    if (pre.length() > 0) builder.append(" ");
                }
            }
            builder.append(typeText.getText());
        }
        return builder.toString();
    }

    public SensorInfoDialog(SensorsListDialog parent, Sensor oldSensor){
        super(parent, SENSOR, true);
        this.parent = parent;
        this.dialogChannel = null;
        this.oldSensor = oldSensor;

        this.createElements();
        this.setInfo();
        this.setReactions();
        this.build();
        LOGGER.info("SensorInfoDialog: creation SUCCESS");
    }

    public SensorInfoDialog(DialogChannel dialogChannel){
        super(dialogChannel, SENSOR, true);
        this.dialogChannel = dialogChannel;
        this.parent = null;
        this.oldSensor = null;

        this.createElements();
        Measurement measurement = dialogChannel.measurementPanel.getMeasurement();
        this.measurementsList.setSelectedItem(measurement.getName());
        this.measurementsList.setEnabled(false);
        this.rangePanel.setValues(measurement.getName());
        this.rangePanel.setValue(measurement.getValue());
        this.setReactions();
        this.build();
    }

    private void createElements() {
        LOGGER.fine("SensorInfoDialog: create elements ...");
        this.labelMeasurement = new ButtonCell(true, TYPE_OF_MEASUREMENT);
        this.labelType = new ButtonCell(true, TYPE);
        this.labelName = new ButtonCell(true, NAME);
        this.labelRange = new ButtonCell(true, RANGE_OF_SENSOR);
        this.labelErrorFormula = new ButtonCell(true, ERROR_FORMULA);

        this.measurementsList = new JComboBox<>(Application.context.measurementService.getAllNames());

        String[]consumptionTypes = new String[]{"",Sensor.YOKOGAWA, Sensor.ROSEMOUNT};
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
        this.errorPopupMenu = new JPopupMenu(INSERT);
        this.errorFormulaText.setComponentPopupMenu(this.errorPopupMenu);

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

        LOGGER.fine("SensorInfoDialog: create elements SUCCESS");
    }

    private void setInfo(){
        if (this.oldSensor != null){
            LOGGER.fine("SensorInfoDialog: set info from sensor [" + this.oldSensor + "]");

            this.measurementsList.setSelectedItem(this.oldSensor.getMeasurement());
            if (this.oldSensor.getMeasurement().equals(Measurement.CONSUMPTION)){
                String type = this.oldSensor.getType();
                int spaceIndex = type.indexOf(" ");
                this.typeText.setText(type.substring(++spaceIndex));
                this.typesList.setSelectedItem(type.substring(0, --spaceIndex));
                this.rangePanel.setValues(null);
            }else {
                this.typeText.setText(this.oldSensor.getType());
                this.rangePanel.setRange(this.oldSensor.getRangeMax(), this.oldSensor.getRangeMin());
                this.rangePanel.setValues(this.oldSensor.getMeasurement());
                this.rangePanel.setValue(this.oldSensor.getValue());
            }
            this.nameText.setText(this.oldSensor.getName());
            this.errorFormulaText.setText(this.oldSensor.getErrorFormula());
            this.showErrorHintsIfNeed();

            this.measurementsList.setEnabled(false);

            LOGGER.fine("SensorInfoDialog: set info SUCCESS");
        }else {
            this.rangePanel.setValues(Measurement.TEMPERATURE);
        }
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonSave.addActionListener(this.clickSave);

        this.measurementsList.addItemListener(this.changeMeasurement);
        this.typesList.addItemListener(this.changeSensorTypePre);

        this.typeText.getDocument().addDocumentListener(this.typeChange);
        this.errorFormulaText.getDocument().addDocumentListener(this.errorUpdate);
    }

    private void build() {
        this.setSize(850,550);
        if (this.dialogChannel == null) {
            this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
        }else {
            this.setLocation(ConverterUI.POINT_CENTER(this.dialogChannel, this));
        }

        this.setContentPane(new MainPanel());
    }

    private void refresh(){
        this.setVisible(false);
        this.setVisible(true);
    }

    private void showErrorHintsIfNeed(){
        errorPopupMenu.removeAll();
        Double d = VariableConverter.parseToDouble(errorFormulaText.getText());
        if (d != null){
            JMenuItem from_R_item = new JMenuItem(from_R(d));
            JMenuItem from_r_item = new JMenuItem(from_r(d));
            JMenuItem from_convR_item = new JMenuItem(from_convR(d));

            from_R_item.addActionListener(clickFrom_R);
            from_r_item.addActionListener(clickFrom_r);
            from_convR_item.addActionListener(clickFrom_convR);

            errorPopupMenu.add(from_R_item);
            errorPopupMenu.add(from_r_item);
            errorPopupMenu.add(from_convR_item);
        }
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dialogChannel != null) dialogChannel.sensorPanel.setSelectedSensor(null);
            dispose();
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(SensorInfoDialog.this)) return;
            if (checkSensor()) {
                Sensor sensor = new Sensor();
                if (Objects.requireNonNull(measurementsList.getSelectedItem()).toString().equals(Measurement.CONSUMPTION)){
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
                if (dialogChannel == null) {
                    PutSensorInList putSensorInList = new PutSensorInList(parent, SensorInfoDialog.this, sensor);
                    putSensorInList.start(oldSensor);
                }else if (Application.context.sensorService.isExists(nameText.getText())) {
                    JOptionPane.showMessageDialog(SensorInfoDialog.this, "ПВП с такою назвою вже існує в списку");
                }else {
                    Application.context.sensorService.add(sensor);
                    dialogChannel.sensorPanel.update(dialogChannel.measurementPanel.getMeasurement().getName());
                    dialogChannel.sensorPanel.setSelectedSensor(sensor.getName());
                    dispose();
                }
            }
        }
    };

    private final ItemListener changeMeasurement = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && measurementsList.getSelectedItem() != null){
                build();
                String selected = measurementsList.getSelectedItem().toString();
                if (selected.equals(Measurement.CONSUMPTION)){
                    rangePanel.setValues(null);
                }else {
                    rangePanel.setValues(selected);
                }
                refresh();
            }
        }
    };

    private final ItemListener changeSensorTypePre = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && typesList.getSelectedItem() != null){
                namePopupMenu.removeAll();
                JMenuItem type = new JMenuItem(fullType());
                type.addActionListener(clickPasteName);
                namePopupMenu.add(type);
            }
        }
    };

    private final DocumentListener typeChange = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            namePopupMenu.removeAll();
            JMenuItem type = new JMenuItem(fullType());
            type.addActionListener(clickPasteName);
            namePopupMenu.add(type);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            namePopupMenu.removeAll();
            JMenuItem type = new JMenuItem(fullType());
            type.addActionListener(clickPasteName);
            namePopupMenu.add(type);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {}
    };

    private final ActionListener clickPasteName = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            nameText.setText(fullType());
            nameText.requestFocus();
        }
    };

    private final DocumentListener errorUpdate = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            showErrorHintsIfNeed();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            showErrorHintsIfNeed();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {}
    };

    private final ActionListener clickFrom_R = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Double d = VariableConverter.parseToDouble(errorFormulaText.getText());
            if (d != null) errorFormulaText.setText("(R/100) * " + d);
        }
    };

    private final ActionListener clickFrom_r = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Double d = VariableConverter.parseToDouble(errorFormulaText.getText());
            if (d != null) errorFormulaText.setText("(r/100) * " + d);
        }
    };

    private final ActionListener clickFrom_convR = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Double d = VariableConverter.parseToDouble(errorFormulaText.getText());
            if (d != null) errorFormulaText.setText("(convR/100) * " + d);
        }
    };

    private boolean checkSensor(){
        if (this.typeText.getText().length() == 0 &&
                !Objects.requireNonNull(this.measurementsList.getSelectedItem()).toString().equals(Measurement.CONSUMPTION)){
            JOptionPane.showMessageDialog(this, "Ви не ввели тип ПВП");
            return false;
        }else if (this.nameText.getText().length() == 0) {
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
            if (Objects.requireNonNull(measurementsList.getSelectedItem()).toString().equals(Measurement.CONSUMPTION)){
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