package ui.sensorsList.sensorInfo;

import backgroundTasks.PutSensorInList;
import constants.Strings;
import converters.ConverterUI;
import measurements.Measurement;
import support.Lists;
import support.Sensor;
import ui.ButtonCell;
import ui.UI_Container;
import ui.sensorsList.SensorsListDialog;
import ui.sensorsList.sensorInfo.complexElements.SensorRangePanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SensorInfoDialog extends JDialog implements UI_Container {
    private final SensorsListDialog parent;
    private final SensorInfoDialog current;
    private final Sensor oldSensor;

    private ButtonCell labelMeasurement;
    private ButtonCell labelType;
    private ButtonCell labelName;
    private ButtonCell labelRange;
    private ButtonCell labelErrorFormula;
    private ButtonCell helpFormula;

    private JComboBox<String>measurementsList;
    private JTextField typeText;
    private JTextField nameText;
    private SensorRangePanel rangePanel;
    private JTextField errorFormulaText;

    private JButton buttonCancel, buttonSave;

    private final Color buttonsColor = new Color(51,51,51);

    public SensorInfoDialog(SensorsListDialog parent, Sensor oldSensor){
        super(parent, Strings.SENSOR, true);
        this.parent = parent;
        this.current = this;
        this.oldSensor = oldSensor;

        this.createElements();
        this.setInfo();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.labelMeasurement = new ButtonCell(true, Strings.TYPE_OF_MEASUREMENT);
        this.labelType = new ButtonCell(true, Strings.TYPE);
        this.labelName = new ButtonCell(true, Strings._NAME);
        this.labelRange = new ButtonCell(true, Strings.RANGE_OF_SENSOR);
        this.labelErrorFormula = new ButtonCell(true, Strings.ERROR_FORMULA);

        String[]measurements = new String[Objects.requireNonNull(Lists.measurements()).size()];
        int x = 0;
        for (Measurement measurement : Objects.requireNonNull(Lists.measurements())){
            measurements[x] = measurement.getName();
            x++;
        }
        this.measurementsList = new JComboBox<>(measurements);

        String typeHint = "Тип ПВП(Застосовується у протоколі)";
        this.typeText = new JTextField(typeHint, 10);

        String nameHint = "Назва ПВП для застосування в данній програмі(Не фігурує в документах)";
        this.nameText = new JTextField(nameHint, 10);

        this.rangePanel = new SensorRangePanel();
        this.errorFormulaText = new JTextField(10);

        String help = "Щоб написати формулу користуйтеся:" +
                "\n0...9, 0.1, 0,1 - Натуральні та дробні числа" +
                "\n() - Дужки, для розстановки послідовності дій" +
                "\n+, -, *, / - сума, різниця, множення, ділення" +
                "\nR - Діапазон вимірювання вимірювального каналу" +
                "\nconvR - Діапазон вимірювання ПВП переконвертований під вимірювальну величину вимірювального каналу" +
                "\nr - Діапазон вимірювання ПВП";
        this.helpFormula = new ButtonCell(false, help);

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(buttonsColor);
        this.buttonCancel.setForeground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);

        this.buttonSave = new JButton(Strings.SAVE);
        this.buttonSave.setBackground(buttonsColor);
        this.buttonSave.setForeground(Color.white);
        this.buttonSave.setFocusPainted(false);
        this.buttonSave.setContentAreaFilled(false);
        this.buttonSave.setOpaque(true);
    }

    private void setInfo(){
        if (this.oldSensor != null){
            this.measurementsList.setSelectedItem(this.oldSensor.getMeasurement());
            this.typeText.setText(this.oldSensor.getType());
            this.nameText.setText(this.oldSensor.getName());
            this.rangePanel.setRange(this.oldSensor.getRangeMax(), this.oldSensor.getRangeMin());
            this.errorFormulaText.setText(this.oldSensor.getErrorFormula());
        }
    }

    @Override
    public void setReactions() {
        this.buttonCancel.addChangeListener(pushButton);
        this.buttonSave.addChangeListener(pushButton);

        this.buttonCancel.addActionListener(clickCancel);
        this.buttonSave.addActionListener(clickSave);
    }

    @Override
    public void build() {
        this.setSize(500,500);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()){
                button.setBackground(buttonsColor.darker());
            }else {
                button.setBackground(buttonsColor);
            }
        }
    };

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Sensor sensor = new Sensor();
            sensor.setType(typeText.getText());
            sensor.setName(nameText.getText());
            sensor.setMeasurement(Objects.requireNonNull(measurementsList.getSelectedItem()).toString());
            sensor.setRangeMin(rangePanel.getRangeMin());
            sensor.setRangeMax(rangePanel.getRangeMax());
            sensor.setValue(rangePanel.getValue());
            sensor.setErrorFormula(errorFormulaText.getText());
            new PutSensorInList(parent, current, sensor, oldSensor).execute();
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(labelMeasurement, new Cell(0, 0,1,1));
            this.add(labelType, new Cell(0, 1,1,1));
            this.add(labelName, new Cell(0, 2,1,1));
            this.add(labelRange, new Cell(0, 3,1,1));
            this.add(labelErrorFormula, new Cell(0, 4,1,1));

            this.add(measurementsList, new Cell(1, 0,1,1));
            this.add(typeText, new Cell(1, 1,1,1));
            this.add(nameText, new Cell(1, 2,1,1));
            this.add(rangePanel, new Cell(1, 3,1,1));
            this.add(errorFormulaText, new Cell(1, 4,1,1));

            this.add(helpFormula, new Cell(0,5,2,7));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width, int height){
                super();

                this.fill = BOTH;
                this.weightx = 1D;
                this.weighty = 1D;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
                this.gridheight = height;
            }
        }
    }
}
