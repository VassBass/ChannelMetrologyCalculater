package ui.calculate.start;

import constants.Value;
import support.Calibrator;
import converters.ConverterUI;
import measurements.Measurement;
import support.Channel;
import support.Lists;
import constants.Strings;
import support.Values;
import ui.UI_Container;
import ui.calculate.measurement.CalculateMeasurementDialog;
import ui.calculate.start.complexElements.CalculateStartDialog_alarmPanel;
import ui.calculate.start.complexElements.CalculateStartDialog_datePanel;
import ui.calculate.start.complexElements.CalculateStartDialog_weatherPanel;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CalculateStartDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final Channel channel;
    private Values values;
    private final JDialog current;

    private boolean withAlarm;

    private JLabel labelDate;
    private JLabel labelNumber;
    private JLabel calibratorLabel;

    private CalculateStartDialog_datePanel datePanel;
    private CalculateStartDialog_weatherPanel weatherPanel;

    private JTextField numberOfProtocol;

    private JComboBox<String>calibrator;

    private JButton positiveButton, negativeButton;

    private JCheckBox alarmCheck;

    private CalculateStartDialog_alarmPanel alarmPanel;

    public CalculateStartDialog(MainScreen mainScreen, Channel channel, Values values){
        super(mainScreen, "Розрахунок каналу \"" + channel.getName() + "\"", true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.labelDate = new JLabel("Дата : ");
        this.labelNumber = new JLabel("№ ");
        this.calibratorLabel = new JLabel(Strings.CALIBRATOR + ": ");

        this.datePanel = new CalculateStartDialog_datePanel();

        this.numberOfProtocol = new JTextField("0", 5);
        this.numberOfProtocol.setHorizontalAlignment(SwingConstants.CENTER);

        this.calibrator = new JComboBox<>(calibratorsArray(this.channel.getMeasurement()));
        this.calibrator.setBackground(Color.white);
        this.calibrator.setEditable(false);

        this.weatherPanel = new CalculateStartDialog_weatherPanel();

        this.positiveButton = new JButton(Strings.START);
        this.positiveButton.setBackground(Color.white);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);

        this.negativeButton = new JButton(Strings.CANCEL);
        this.negativeButton.setBackground(Color.white);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);

        this.alarmCheck = new JCheckBox(Strings.ALARM_CHECK);

        this.alarmPanel = new CalculateStartDialog_alarmPanel(this.channel);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.numberOfProtocol.addFocusListener(this.numberFocus);

        this.positiveButton.addChangeListener(this.pushButton);
        this.negativeButton.addChangeListener(this.pushButton);

        this.positiveButton.addActionListener(this.clickPositiveButton);
        this.negativeButton.addActionListener(this.clickNegativeButton);

        alarmCheck.addItemListener(clickAlarm);
    }

    @Override
    public void build() {
        this.setValues(this.values);
    }

    private final FocusListener numberFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            numberOfProtocol.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (numberOfProtocol.getText().length()==0){
                numberOfProtocol.setText("0");
            }
        }
    };

    private void setValues(Values values){
        if (values != null){
            this.numberOfProtocol.setText(values.getStringValue(Value.CHANNEL_PROTOCOL_NUMBER));
            this.datePanel.update((Calendar) values.getValue(Value.CHANNEL_DATE));
            String[] calibrators = this.calibratorsArray(this.channel.getMeasurement());
            Calibrator calibrator = (Calibrator) values.getValue(Value.CALIBRATOR);
            for (int x=0;x<calibrators.length;x++){
                if (calibrator.getName().equals(calibrators[x])){
                    this.calibrator.setSelectedIndex(x);
                    break;
                }
            }
            this.weatherPanel.temperature.setText(values.getStringValue(Value.CALCULATION_EXTERNAL_TEMPERATURE));
            this.weatherPanel.pressure.setText(values.getStringValue(Value.CALCULATION_EXTERNAL_PRESSURE));
            this.weatherPanel.humidity.setText(values.getStringValue(Value.CALCULATION_EXTERNAL_HUMIDITY));
            this.alarmPanel.setValue(values.getStringValue(Value.CALCULATION_ALARM_VALUE));
            boolean withAlarmPanel = values.getBooleanValue(Value.CALCULATION_ALARM_PANEL);
            this.setContentPane(new MainPanel(withAlarmPanel));
        }else {
            this.values = new Values();
            this.setContentPane(new MainPanel(false));
        }

    }

    public Values getValues(Values values){
        if (values == null){
            values = new Values();
        }

        values.putValue(Value.CHANNEL_PROTOCOL_NUMBER, this.numberOfProtocol.getText());
        values.putValue(Value.CHANNEL_DATE, this.datePanel.getDate());
        for (Calibrator calibrator : Objects.requireNonNull(Lists.calibrators())){
            if (calibrator.getName().equals(Objects.requireNonNull(this.calibrator.getSelectedItem()).toString())){
                values.putValue(Value.CALIBRATOR, calibrator);
                break;
            }
        }
        values.putValue(Value.CALCULATION_EXTERNAL_TEMPERATURE, this.weatherPanel.temperature.getText());
        values.putValue(Value.CALCULATION_EXTERNAL_PRESSURE, this.weatherPanel.pressure.getText());
        values.putValue(Value.CALCULATION_EXTERNAL_HUMIDITY, this.weatherPanel.humidity.getText());
        values.putValue(Value.CALCULATION_ALARM_PANEL, this.withAlarm);
        values.putValue(Value.CALCULATION_ALARM_VALUE, this.alarmPanel.getValue());

        return values;
    }

    private String[]calibratorsArray(Measurement measurement){
        ArrayList<String> c = new ArrayList<>();
        ArrayList<Calibrator>calibrators = Lists.calibrators();
        for (Calibrator calibrator : Objects.requireNonNull(calibrators)) {
            if (calibrator.getMeasurement().equals(measurement.getName())){
                c.add(calibrator.getName());
            }
        }
        return c.toArray(new String[0]);
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                button.setBackground(Color.white.darker());
            }else {
                button.setBackground(Color.white);
            }
        }
    };

    private final ActionListener clickPositiveButton = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    CalculateMeasurementDialog measurementDialog = new CalculateMeasurementDialog(mainScreen, channel, getValues(values));
                    dispose();
                    measurementDialog.setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ItemListener clickAlarm = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (alarmCheck.isSelected()) {
                        setContentPane(new MainPanel(true));
                    } else {
                        setContentPane(new MainPanel(false));
                    }
                }
            });
        }
    };

    private class MainPanel extends JPanel {

        protected MainPanel(boolean withAlarmPanel){
            super(new GridBagLayout());
            withAlarm = withAlarmPanel;

            JPanel date = new JPanel();
            date.add(labelDate);
            date.add(datePanel);
            this.add(date, new Cell(0,0));

            JPanel numberPanel = new JPanel();
            numberPanel.add(labelNumber);
            numberPanel.add(numberOfProtocol);
            this.add(numberPanel, new Cell(1,0));

            JPanel calibratorPanel = new JPanel();
            calibratorPanel.add(calibratorLabel);
            calibratorPanel.add(calibrator);
            this.add(calibratorPanel, new Cell(2,0));

            this.add(alarmCheck, new Cell(1,1,1));
            alarmCheck.setSelected(withAlarmPanel);

            this.add(weatherPanel, new Cell(0,2,3));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(positiveButton);
            buttonsPanel.add(negativeButton);
            this.add(buttonsPanel, new Cell(0,3, 3));

            if (withAlarmPanel){
                this.add(alarmPanel, new Cell(0,4,3));
                current.setSize(820,350);
            }else {
                current.setSize(820,300);
            }
            current.setLocation(ConverterUI.POINT_CENTER(mainScreen, current));
        }

        private class Cell extends GridBagConstraints {

            protected Cell(int x, int y){
                this.fill = HORIZONTAL;
                this.insets = new Insets(5,5,5,5);

                this.gridx = x;
                this.gridy = y;
            }

            protected Cell(int x, int y, int width){
                this.fill = HORIZONTAL;
                this.insets = new Insets(5,5,5,5);

                this.gridwidth = width;
                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}
