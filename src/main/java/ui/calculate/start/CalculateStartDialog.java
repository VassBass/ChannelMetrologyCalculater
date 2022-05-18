package ui.calculate.start;

import application.Application;
import constants.Key;
import converters.ConverterUI;
import model.Calibrator;
import model.Channel;
import model.Measurement;
import model.Sensor;
import service.SystemData;
import ui.calculate.measurement.CalculateMeasurementDialog;
import ui.calculate.start.complexElements.CalculateStartDialog_alarmPanel;
import ui.calculate.start.complexElements.CalculateStartDialog_datePanel;
import ui.calculate.start.complexElements.CalculateStartDialog_weatherPanel;
import ui.calibratorsList.calibratorInfo.CalibratorInfoDialog;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Objects;

public class CalculateStartDialog extends JDialog {
    private static String title(Channel channel){
        return "Розрахунок каналу \"" + channel.getName() + "\"";
    }
    private static final String DATE = "Дата : ";
    private static final String NUMBER = "№ ";
    private static final String CALIBRATOR = "Калібратор: ";
    private static final String DEFAULT_NUMBER_OF_PROTOCOL = "0";
    private static final String START = "Почати (Alt + Enter)";
    private static final String CANCEL = "Відміна (Alt + Esc)";
    private static final String ALARM_CHECK = "Перевірка сигналізації";
    private static final String ADD_NEW = "<-Додати новий->";

    private final MainScreen mainScreen;
    private final Channel channel;
    private HashMap<Integer, Object> values;

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

    private SystemData os;

    public CalculateStartDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values){
        super(mainScreen, title(channel), true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;

        this.createModels();
        this.createView();
        this.addControllers();
    }

    public CalculateStartDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values, SystemData os){
        super(mainScreen, title(channel), true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.os = os;

        this.createModels();
        this.createView();
        this.addControllers();
    }

   private void createModels() {
        this.labelDate = new JLabel(DATE);
        this.labelNumber = new JLabel(NUMBER);
        this.calibratorLabel = new JLabel(CALIBRATOR);

        this.datePanel = new CalculateStartDialog_datePanel();

        this.numberOfProtocol = new JTextField(DEFAULT_NUMBER_OF_PROTOCOL, 5);
        this.numberOfProtocol.setHorizontalAlignment(SwingConstants.CENTER);

        this.calibrator = new JComboBox<>();
        this.calibrator.setBackground(Color.WHITE);
        this.calibrator.setEditable(false);
        this.updateCalibrators(this.channel.getMeasurement().getName());

        this.weatherPanel = new CalculateStartDialog_weatherPanel();

        this.positiveButton = new DefaultButton(START);
        this.negativeButton = new DefaultButton(CANCEL);

        this.alarmCheck = new JCheckBox(ALARM_CHECK);
        this.alarmPanel = new CalculateStartDialog_alarmPanel(this.channel);
    }

    public void updateCalibrators(String measurement){
        if (measurement == null){
            this.calibrator.setSelectedIndex(0);
        }else {
            String[] cal = Application.context.calibratorService.getAllNames(new Measurement(measurement, ""));
            String[] toModel = new String[cal.length + 1];
            System.arraycopy(cal,0,toModel,0,cal.length);
            toModel[toModel.length - 1] = ADD_NEW;
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(toModel);
            this.calibrator.setModel(model);
        }
    }

    private void addControllers() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.numberOfProtocol.addFocusListener(this.numberFocus);

        this.positiveButton.addActionListener(this.clickPositiveButton);
        this.negativeButton.addActionListener(this.clickNegativeButton);

        this.alarmCheck.addItemListener(this.clickAlarm);
        this.calibrator.addItemListener(this.changeCalibrator);

        this.datePanel.addKeyListener(this.keyListener);
        this.numberOfProtocol.addKeyListener(this.keyListener);
        this.calibrator.addKeyListener(this.keyListener);
        this.alarmCheck.addKeyListener(this.keyListener);
        this.weatherPanel.addKeyListener(this.keyListener);
        this.positiveButton.addKeyListener(this.keyListener);
        this.negativeButton.addKeyListener(this.keyListener);
    }

    private void createView() {
        this.setValues(this.values);
    }

    private final FocusListener numberFocus = new FocusListener() {
        @Override public void focusGained(FocusEvent e) {
            numberOfProtocol.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (numberOfProtocol.getText().length()==0){
                numberOfProtocol.setText(DEFAULT_NUMBER_OF_PROTOCOL);
            }
        }
    };

    private void setValues(HashMap<Integer, Object> values){
        if (values != null){
            this.numberOfProtocol.setText((String) values.get(Key.CHANNEL_PROTOCOL_NUMBER));
            this.datePanel.update((String) values.get(Key.CHANNEL_DATE));
            String[] calibrators = Application.context.calibratorService.getAllNames(this.channel.getMeasurement());
            Calibrator calibrator = (Calibrator) values.get(Key.CALIBRATOR);
            for (int x=0;x<calibrators.length;x++){
                if (calibrator != null && calibrator.getName().equals(calibrators[x])){
                    this.calibrator.setSelectedIndex(x);
                    break;
                }
            }
            this.weatherPanel.temperature.setText((String) values.get(Key.CALCULATION_EXTERNAL_TEMPERATURE));
            this.weatherPanel.pressure.setText((String) values.get(Key.CALCULATION_EXTERNAL_PRESSURE));
            this.weatherPanel.humidity.setText((String) values.get(Key.CALCULATION_EXTERNAL_HUMIDITY));
            this.alarmPanel.setValue((String) values.get(Key.CALCULATION_ALARM_VALUE));
            boolean withAlarmPanel = (boolean) values.get(Key.CALCULATION_ALARM_PANEL);
            this.setContentPane(new MainPanel(withAlarmPanel));
        }else {
            this.values = new HashMap<>();
            if (this.channel.getSensor().getType().contains(Sensor.ROSEMOUNT)){
                this.calibrator.setSelectedItem(Calibrator.ROSEMOUNT_8714DQ4);
            }
            this.setContentPane(new MainPanel(false));
        }
        if (values != null && values.get(Key.SYS) == null) values.put(Key.SYS, this.os);
    }

    public HashMap<Integer, Object> getValues(HashMap<Integer, Object> values){
        if (values == null){
            values = new HashMap<>();
        }
        values.put(Key.CHANNEL_PROTOCOL_NUMBER, this.numberOfProtocol.getText());
        values.put(Key.CHANNEL_DATE, this.datePanel.getDate());
        String calibratorName = Objects.requireNonNull(this.calibrator.getSelectedItem()).toString();
        values.put(Key.CALIBRATOR, Application.context.calibratorService.get(calibratorName));
        values.put(Key.CALCULATION_EXTERNAL_TEMPERATURE, this.weatherPanel.temperature.getText());
        values.put(Key.CALCULATION_EXTERNAL_PRESSURE, this.weatherPanel.pressure.getText());
        values.put(Key.CALCULATION_EXTERNAL_HUMIDITY, this.weatherPanel.humidity.getText());
        values.put(Key.CALCULATION_ALARM_PANEL, this.withAlarm);
        values.put(Key.CALCULATION_ALARM_VALUE, this.alarmPanel.getValue());
        if (values.get(Key.SYS) == null) values.put(Key.SYS, this.os);

        return values;
    }

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

    private final ItemListener changeCalibrator = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && calibrator.getSelectedItem() != null){
                if (calibrator.getSelectedItem().toString().equals(ADD_NEW)){
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new CalibratorInfoDialog(CalculateStartDialog.this, channel).setVisible(true);
                        }
                    });
                }
            }
        }
    };

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_ENTER:
                    if (e.isAltDown()) positiveButton.doClick();
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (e.isAltDown()) negativeButton.doClick();
                    break;
            }
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
                CalculateStartDialog.this.setSize(820,350);
            }else {
                CalculateStartDialog.this.setSize(820,300);
            }
            CalculateStartDialog.this.setLocation(ConverterUI.POINT_CENTER(mainScreen, CalculateStartDialog.this));
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