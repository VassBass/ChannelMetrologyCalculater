package ui.channelInfo;

import backgroundTasks.controllers.PutChannelInList;
import constants.MeasurementConstants;
import converters.ConverterUI;
import measurements.Measurement;
import model.Sensor;
import model.Channel;
import constants.Strings;
import ui.UI_Container;
import ui.channelInfo.complexElements.*;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogChannel extends JDialog implements UI_Container {
    private final MainScreen parent;
    private final DialogChannel current;

    private JLabel codeLabel;
    private JLabel nameLabel;
    private JLabel measurementLabel;
    private JLabel technologyNumberLabel;
    private JLabel dateLabel;
    private JLabel frequencyLabel;
    private JLabel pathLabel;
    private JLabel sensorLabel;
    private JLabel rangeLabel;
    private JLabel allowableErrorLabel;
    private JLabel protocolNumberLabel;

    private JTextField userCode;
    private JTextField userName;
    private JTextField userTechnologyNumber;
    private JTextField userProtocolNumber;

    public DialogChannel_measurementPanel measurementPanel;
    public DialogChannel_datePanel datePanel;
    public DialogChannel_frequencyPanel frequencyPanel;
    public DialogChannel_pathPanel pathPanel;
    public DialogChannel_sensorPanel sensorPanel;
    public DialogChannel_rangePanel rangePanel;
    public DialogChannel_allowableErrorPanel allowableErrorPanel;
    public DialogChannel_sensorRangePanel sensorRangePanel;

    private JButton negativeButton;
    private JButton positiveButton;

    public final Channel oldChannel;
    private final Color backgroundColor_buttons = Color.white;
    private Color defaultTextColor;

    public DialogChannel(MainScreen parent, Channel oldChannel){
        super(parent, Strings.INFORMATION_ABOUT_CHANNEL, true);
        this.parent = parent;
        this.current = this;
        this.oldChannel = oldChannel;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements(){
        this.codeLabel = new JLabel("*".concat(Strings.CODE).concat(": "));
        this.userCode = new JTextField(10);

        this.nameLabel = new JLabel("*".concat(Strings._NAME).concat(": "));
        this.userName = new JTextField(10);

        this.measurementLabel = new JLabel(Strings.TYPE_OF_MEASUREMENT.concat(": "));
        this.measurementPanel = new DialogChannel_measurementPanel(this);

        this.technologyNumberLabel = new JLabel(Strings.TECHNOLOGY_NUMBER.concat(": "));
        this.userTechnologyNumber = new JTextField(10);

        this.dateLabel = new JLabel(Strings.THIS_DATE.concat(": "));
        this.datePanel = new DialogChannel_datePanel(this);

        this.frequencyLabel = new JLabel(Strings.FREQUENCY_CONTROL.concat(": "));
        this.frequencyPanel = new DialogChannel_frequencyPanel(this);

        this.pathLabel = new JLabel(Strings.PATH.concat(": "));
        this.pathPanel = new DialogChannel_pathPanel();

        this.sensorLabel = new JLabel(Strings.SENSOR.concat(": "));
        this.sensorPanel = new DialogChannel_sensorPanel(this);

        this.rangeLabel = new JLabel(Strings.RANGE_OF_CHANNEL.concat(": "));
        this.rangePanel = new DialogChannel_rangePanel(this);

        this.allowableErrorLabel = new JLabel(Strings.ALLOWABLE_ERROR_OF_CHANNEL.concat(": "));
        this.allowableErrorPanel = new DialogChannel_allowableErrorPanel(this);

        this.protocolNumberLabel = new JLabel(Strings.PROTOCOL_NUMBER.concat(": "));
        this.userProtocolNumber = new JTextField(10);

        this.negativeButton = new JButton(Strings.CANCEL);
        this.negativeButton.setBackground(backgroundColor_buttons);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);

        this.positiveButton = new JButton(Strings.SAVE);
        this.positiveButton.setBackground(backgroundColor_buttons);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);

        this.defaultTextColor = this.codeLabel.getForeground();
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.negativeButton.addChangeListener(pushButton);
        this.positiveButton.addChangeListener(pushButton);

        this.negativeButton.addActionListener(clickNegativeButton);
        this.positiveButton.addActionListener(clickPositiveButton);
    }

    @Override
    public void build() {
        if (this.oldChannel == null){
            this.setMeasurement(null);
        }else {
            this.setMeasurement(this.oldChannel.getMeasurement());
            this.userCode.setText(this.oldChannel.getCode());
            this.userName.setText(this.oldChannel.getName());
            this.measurementPanel.update(this.oldChannel.getMeasurement().getName());
            this.userTechnologyNumber.setText(this.oldChannel.getTechnologyNumber());
            this.datePanel.update(this.oldChannel.getDate());
            this.frequencyPanel.update(this.oldChannel.getFrequency(), this.oldChannel.getDate());
            this.pathPanel.update(this.oldChannel.getDepartment(), this.oldChannel.getArea(), this.oldChannel.getProcess(), this.oldChannel.getInstallation());
            this.sensorPanel.update(this.oldChannel.getMeasurement().getNameConstant());
            this.sensorPanel.update(this.oldChannel.getSensor());
            this.rangePanel.update(this.oldChannel.getMeasurement().getValue());
            this.rangePanel.update(this.oldChannel.getRangeMin(), this.oldChannel.getRangeMax());
            double range = this.oldChannel.getRangeMax() - this.oldChannel.getRangeMin();
            this.allowableErrorPanel.update(this.oldChannel.getAllowableError(), false, range);
            this.userProtocolNumber.setText(this.oldChannel.getNumberOfProtocol());
            if (this.sensorRangePanel != null){
                this.sensorRangePanel.update(this.oldChannel.getSensor());
            }

        }

        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
        this.setContentPane(new MainPanel());
    }

    public void update(Measurement measurement) {
        if (measurement != null){
            this.setMeasurement(measurement);
        }
    }

    public boolean allFieldsAreFilled(){
        if (this.userCode.getText().length()==0) {
            this.codeLabel.setForeground(Color.red);
            return false;
        }else {
            this.codeLabel.setForeground(this.defaultTextColor);
        }

        if (this.userName.getText().length()==0) {
            this.nameLabel.setForeground(Color.red);
            return false;
        }else {
            this.nameLabel.setForeground(this.defaultTextColor);
        }

        if (this.userTechnologyNumber.getText().length()==0) {
            this.technologyNumberLabel.setForeground(Color.red);
            return false;
        }else {
            this.technologyNumberLabel.setForeground(this.defaultTextColor);
        }
        return true;
    }

    public Channel getChannel(){
        Channel channel = new Channel();
        channel.setCode(this.userCode.getText());
        channel.setName(this.userName.getText());
        channel.setMeasurement(this.measurementPanel.getMeasurement());
        channel.setDepartment(this.pathPanel.getDepartment());
        channel.setArea(this.pathPanel.getArea());
        channel.setProcess(this.pathPanel.getProcess());
        channel.setInstallation(this.pathPanel.getInstallation());
        channel.setDate(this.datePanel.getDate());
        channel.setFrequency(this.frequencyPanel.getFrequency());
        channel.setTechnologyNumber(this.userTechnologyNumber.getText());
        Sensor sensor = this.sensorPanel.getSensor();
        if (this.sensorRangePanel != null){
            sensor.setRange(this.sensorRangePanel.getRangeMin(), this.sensorRangePanel.getRangeMax());
            sensor.setValue(this.sensorRangePanel.getValue());
        }
        if (channel.getMeasurement().getNameConstant() == MeasurementConstants.CONSUMPTION){
            sensor.setNumber(this.sensorPanel.getSerialNumber());
            sensor.setRangeMin(this.rangePanel.getRangeMin());
            sensor.setRangeMax(this.rangePanel.getRangeMax());
            sensor.setValue(this.measurementPanel.getMeasurement().getValue());
        }
        channel.setSensor(sensor);
        channel.setNumberOfProtocol(this.userProtocolNumber.getText());
        channel.setRange(this.rangePanel.getRangeMin(), this.rangePanel.getRangeMax());
        channel.setAllowableError(this.allowableErrorPanel.getAllowableErrorPercent(), this.allowableErrorPanel.getAllowableErrorValue());

        if (this.oldChannel != null){
            channel.setReference(this.oldChannel.getReference());
            channel.isGood = this.oldChannel.isGood;
        }

        return channel;
    }

    private void setMeasurement(Measurement measurement){
        MeasurementConstants measurementName;
        if (measurement != null){
            measurementName = measurement.getNameConstant();
        }else {
            measurementName = MeasurementConstants.TEMPERATURE;
        }
        this.measurementPanel.update(measurementName.getValue());
        this.sensorPanel.update(measurementName);
        switch (measurementName){
            case TEMPERATURE:
                this.setSize(800, 650);
                this.sensorRangePanel = null;
                this.rangeLabel.setText(Strings.RANGE_OF_CHANNEL);
                this.allowableErrorPanel.setEnabled(true);
                break;
            case PRESSURE:
                this.setSize(1000, 650);
                this.sensorRangePanel = new DialogChannel_sensorRangePanel(measurement);
                this.rangeLabel.setText(Strings.RANGE_OF_CHANNEL);
                this.allowableErrorPanel.setEnabled(true);
                break;
            case CONSUMPTION:
                this.setSize(800,650);
                this.sensorRangePanel = null;
                this.rangeLabel.setText(Strings.RANGE_OF_SENSOR);
                Channel channel = new Channel();
                channel.setMeasurement(this.measurementPanel.getMeasurement());
                channel.setRangeMin(this.rangePanel.getRangeMin());
                channel.setRangeMax(this.rangePanel.getRangeMax());
                Sensor sensor = this.sensorPanel.getSensor();
                sensor.setValue(channel.getMeasurement().getValue());
                double errorSensor = sensor.getError(channel);
                this.allowableErrorPanel.update(errorSensor, false, channel.getRange());
                this.allowableErrorPanel.setEnabled(false);
                break;
        }
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            current.dispose();
        }
    };

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!allFieldsAreFilled()) {return;}

            new PutChannelInList(current, parent).execute();
        }
    };

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                setBackground(backgroundColor_buttons.darker());
            }else {
                setBackground(backgroundColor_buttons);
            }
        }
    };

    private class MainPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        protected MainPanel() {
            super(new GridBagLayout());

            this.add(codeLabel, new Cell(0,0));
            this.add(userCode, new Cell(1,0));

            this.add(nameLabel, new Cell(0,1));
            this.add(userName, new Cell(1,1));

            this.add(measurementLabel, new Cell(0,2));
            this.add(measurementPanel, new Cell(1,2));

            this.add(technologyNumberLabel, new Cell(0,3));
            this.add(userTechnologyNumber, new Cell(1,3));

            this.add(dateLabel, new Cell(0,4));
            this.add(datePanel, new Cell(1,4));

            this.add(protocolNumberLabel, new Cell(0,5));
            this.add(userProtocolNumber, new Cell(1,5));

            this.add(frequencyLabel, new Cell(0,6));
            this.add(frequencyPanel, new Cell(1,6));

            this.add(pathLabel, new Cell(0,7));
            this.add(pathPanel, new Cell(1,7));

            this.add(sensorLabel, new Cell(0,8));
            this.add(sensorPanel, new Cell(1,8));

            if (sensorRangePanel != null){
                this.add(sensorRangePanel, new Cell(2,8));
            }

            this.add(rangeLabel, new Cell(0,9));
            this.add(rangePanel, new Cell(1,9));

            this.add(allowableErrorLabel, new Cell(0,10));
            this.add(allowableErrorPanel, new Cell(1,10));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(positiveButton);
            buttonsPanel.add(negativeButton);
            this.add(buttonsPanel, new Cell(0,11, new Insets(40,0,20,0), 1));
        }

        private class Cell extends GridBagConstraints {

            private static final long serialVersionUID = 1L;

            protected Cell (int x, int y) {
                super();

                this.weightx = 1.0;
                this.fill = HORIZONTAL;
                this.insets = new Insets(5,5,5,5);

                this.gridx = x;
                this.gridy = y;
            }

            protected Cell (int x, int y, Insets insets, int width) {
                this.weightx = 1.0;
                this.fill = HORIZONTAL;
                this.insets = insets;

                this.gridwidth = width;
                this.gridx = x;
                this.gridy = y;
            }
        }
    }

}
