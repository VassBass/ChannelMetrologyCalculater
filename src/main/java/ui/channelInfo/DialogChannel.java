package ui.channelInfo;

import application.Application;
import constants.MeasurementConstants;
import converters.ConverterUI;
import measurements.Measurement;
import model.Sensor;
import model.Channel;
import ui.channelInfo.complexElements.*;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DialogChannel extends JDialog {
    private static final String INFORMATION_ABOUT_CHANNEL = "Інформація вимірювального каналу";
    private static final String CODE = "*Код: ";
    private static final String NAME = "*Назва: ";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання: ";
    private static final String TECHNOLOGY_NUMBER = "Технологічний номер: ";
    private static final String THIS_DATE = "Дата останньої перевірки: ";
    private static final String FREQUENCY_CONTROL = "Міжконтрольний інтервал: ";
    private static final String PATH = "Розташування: ";
    private static final String SENSOR = "Первинний вимірювальний пристрій: ";
    private static final String RANGE_OF_CHANNEL_ = "Діапазон вимірювального каналу: ";
    private static final String ALLOWABLE_ERROR_OF_CHANNEL = "Допустима похибка вимірювального каналу: ";
    private static final String PROTOCOL_NUMBER = "Номер протоколу: ";
    private static final String CANCEL = "Відміна";
    private static final String SAVE = "Зберегти";
    private static final String RANGE_OF_CHANNEL = "Діапазон вимірювального каналу";
    private static final String RANGE_OF_SENSOR = "Діапазон вимірювання ПВП";

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
    private final Color defaultTextColor;

    public DialogChannel(MainScreen parent, Channel oldChannel){
        super(parent, INFORMATION_ABOUT_CHANNEL, true);
        this.parent = parent;
        this.current = this;
        this.oldChannel = oldChannel;

        this.createLabels();
        this.createPrimitiveElements();
        this.createComplexElements();
        this.setReactions();
        this.build();

        this.defaultTextColor = this.codeLabel.getForeground();
    }

    private void createLabels(){
        this.codeLabel = new JLabel(CODE);
        this.nameLabel = new JLabel(NAME);
        this.measurementLabel = new JLabel(TYPE_OF_MEASUREMENT);
        this.technologyNumberLabel = new JLabel(TECHNOLOGY_NUMBER);
        this.dateLabel = new JLabel(THIS_DATE);
        this.frequencyLabel = new JLabel(FREQUENCY_CONTROL);
        this.pathLabel = new JLabel(PATH);
        this.sensorLabel = new JLabel(SENSOR);
        this.rangeLabel = new JLabel(RANGE_OF_CHANNEL_);
        this.allowableErrorLabel = new JLabel(ALLOWABLE_ERROR_OF_CHANNEL);
        this.protocolNumberLabel = new JLabel(PROTOCOL_NUMBER);
    }

    private void createPrimitiveElements(){
        this.userCode = new JTextField(10);
        this.userName = new JTextField(10);
        this.userTechnologyNumber = new JTextField(10);
        this.userProtocolNumber = new JTextField(10);

        this.negativeButton = new DefaultButton(CANCEL);
        this.positiveButton = new DefaultButton(SAVE);
    }

    private void createComplexElements(){
        this.measurementPanel = new DialogChannel_measurementPanel(this);
        this.datePanel = new DialogChannel_datePanel(this);
        this.frequencyPanel = new DialogChannel_frequencyPanel(this);
        this.pathPanel = new DialogChannel_pathPanel();
        this.sensorPanel = new DialogChannel_sensorPanel(this);
        this.rangePanel = new DialogChannel_rangePanel(this);
        this.allowableErrorPanel = new DialogChannel_allowableErrorPanel(this);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.negativeButton.addActionListener(this.clickNegativeButton);
        this.positiveButton.addActionListener(this.clickPositiveButton);
    }

    private void build() {
        if (this.oldChannel == null){
            this.setMeasurement(null);
        }else {
            this.setMeasurement(this.oldChannel.getMeasurement());
            this.userCode.setText(this.oldChannel.getCode());
            this.userName.setText(this.oldChannel.getName());
            this.measurementPanel.update(this.oldChannel.getMeasurement().getName());
            this.userTechnologyNumber.setText(this.oldChannel.getTechnologyNumber());
            this.datePanel.setDate(this.oldChannel.getDate());
            this.frequencyPanel.updateFrequency(this.oldChannel.getFrequency(), this.oldChannel.getDate());
            this.pathPanel.update(this.oldChannel.getDepartment(), this.oldChannel.getArea(), this.oldChannel.getProcess(), this.oldChannel.getInstallation());
            this.sensorPanel.update(this.oldChannel.getMeasurement().getNameConstant());
            this.sensorPanel.update(this.oldChannel.getSensor());
            this.rangePanel.updateValue(this.oldChannel.getMeasurement().getValue());
            this.rangePanel.updateRange(this.oldChannel.getRangeMin(), this.oldChannel.getRangeMax());
            double range = this.oldChannel.getRangeMax() - this.oldChannel.getRangeMin();
            this.allowableErrorPanel.updateError(this.oldChannel.getAllowableError(), false, range);
            this.userProtocolNumber.setText(this.oldChannel.getNumberOfProtocol());
            if (this.sensorRangePanel != null){
                this.sensorRangePanel.update(this.oldChannel.getSensor());
            }
            this.measurementPanel.setSelectedValue(this.oldChannel.getMeasurement().getValue());
        }

        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
        this.setContentPane(new MainPanel());
    }

    public void update(Measurement measurement) {
        if (measurement != null){
            this.setMeasurement(measurement);
        }
    }

    private boolean allFieldsAreFilled(){
        if (this.userCode.getText().length()==0) {
            this.codeLabel.setForeground(Color.RED);
            return false;
        }else if (this.oldChannel == null &&
                Application.context.channelsController.isExist(this.userCode.getText())) {
            this.codeLabel.setForeground(Color.RED);
            Application.context.channelsController.showExistMessage(current);
            return false;
        }else if (this.oldChannel != null &&
                Application.context.channelsController.isExist(this.oldChannel.getCode(), this.userCode.getText())){
            this.codeLabel.setForeground(Color.RED);
            Application.context.channelsController.showExistMessage(current);
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
            channel.setSuitability(this.oldChannel.isSuitability());
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
                this.rangeLabel.setText(RANGE_OF_CHANNEL);
                this.allowableErrorPanel.setEnabled(true);
                break;
            case PRESSURE:
                this.setSize(1000, 650);
                this.sensorRangePanel = new DialogChannel_sensorRangePanel(measurement);
                this.rangeLabel.setText(RANGE_OF_CHANNEL);
                this.allowableErrorPanel.setEnabled(true);
                break;
            case CONSUMPTION:
                this.setSize(800,650);
                this.sensorRangePanel = null;
                this.rangeLabel.setText(RANGE_OF_SENSOR);
                Channel channel = new Channel();
                channel.setMeasurement(measurement);
                channel.setRangeMin(this.rangePanel.getRangeMin());
                channel.setRangeMax(this.rangePanel.getRangeMax());
                Sensor sensor = this.sensorPanel.getSensor();
                sensor.setValue(channel.getMeasurement().getValue());
                double errorSensor = sensor.getError(channel);
                this.allowableErrorPanel.updateError(errorSensor, false, channel.getRange());
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
            if (!allFieldsAreFilled() || Application.isBusy(current)) return;

            dispose();
            ArrayList<Channel>channels;
            if (oldChannel == null) {
                channels = Application.context.channelsController.add(getChannel());
            }else {
                channels = Application.context.channelsController.set(oldChannel, getChannel());
            }
            if (Application.context.channelSorter.isOn()){
                parent.setChannelsList(Application.context.channelSorter.getCurrent());
            }else {
                parent.setChannelsList(channels);
            }
        }
    };

    private class MainPanel extends JPanel {

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
            this.add(buttonsPanel, new Cell(0,11, new Insets(40,0,20,0), 2));
        }

        private class Cell extends GridBagConstraints {

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