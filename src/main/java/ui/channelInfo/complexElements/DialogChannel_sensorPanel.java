package ui.channelInfo.complexElements;

import application.Application;
import constants.MeasurementConstants;
import constants.Strings;
import model.Channel;
import model.Sensor;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.Objects;

public class DialogChannel_sensorPanel extends JPanel {
    private final DialogChannel parent;

    private JComboBox<String>sensorsList;
    private JLabel NO;
    private JTextField serialNumber;
    private MeasurementConstants currentMeasurement;

    public DialogChannel_sensorPanel(DialogChannel parent){
        super();
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    public void createElements() {
        this.sensorsList = new JComboBox<>();
        this.sensorsList.setEditable(false);
        this.sensorsList.setBackground(Color.white);

        this.NO = new JLabel("NO.");
        this.serialNumber = new JTextField(10);
    }

    public void setReactions() {
        this.sensorsList.addItemListener(changeSensorName);
    }

    public void build() {
        this.add(this.sensorsList);
    }

    private final ItemListener changeSensorName = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED){
                if (parent.measurementPanel.getMeasurement().getNameConstant() == MeasurementConstants.CONSUMPTION) {
                    Channel channel = new Channel();
                    channel.setMeasurement(parent.measurementPanel.getMeasurement());
                    channel.setRangeMin(parent.rangePanel.getRangeMin());
                    channel.setRangeMax(parent.rangePanel.getRangeMax());
                    Sensor sensor = getSensor();
                    sensor.setValue(channel.getMeasurement().getValue());
                    double errorSensorInPercent = sensor.getError(channel);
                    parent.allowableErrorPanel.updateError(errorSensorInPercent, true, channel.getRange());
                    if (sensor.getType().toUpperCase(Locale.ROOT).contains(Strings.SENSOR_ROSEMOUNT)){
                        setRosemountValues();
                    }else {
                        parent.measurementPanel.update(MeasurementConstants.CONSUMPTION.getValue());
                    }
                }
            }
        }
    };

    private void setRosemountValues(){
        this.parent.measurementPanel.setRosemountValues();
    }

    public void update(MeasurementConstants measurementName) {
        this.removeAll();

        this.currentMeasurement = measurementName;
        if (measurementName != null) {
            String[]sensors = this.sensorsArray(measurementName.getValue());
            DefaultComboBoxModel<String>model = new DefaultComboBoxModel<>(sensors);
            this.sensorsList.setModel(model);
        }
        if (Objects.requireNonNull(this.sensorsList.getSelectedItem()).toString().contains(Strings.SENSOR_ROSEMOUNT)){
            this.setRosemountValues();
        }

        this.add(this.sensorsList);
        if (measurementName == MeasurementConstants.CONSUMPTION){
            this.add(this.NO);
            this.add(this.serialNumber);
        }
    }

    public void update(Sensor sensor){
        if (sensor != null){
            if (this.currentMeasurement.getValue().equals(sensor.getMeasurement())) {
                String[] sensors = this.sensorsArray(sensor.getMeasurement());
                for (int x = 0; x < sensors.length; x++) {
                    if (sensor.getName().equals(sensors[x])) {
                        this.sensorsList.setSelectedIndex(x);
                        break;
                    }
                }
                if (this.currentMeasurement == MeasurementConstants.CONSUMPTION){
                    this.serialNumber.setText(sensor.getNumber());
                }
            }
        }
    }

    private String[] sensorsArray(String measurement) {
        return Application.context.sensorsController.getAllSensorsName(measurement);
    }

    public Sensor getSensor(){
        String selectedSensor = Objects.requireNonNull(this.sensorsList.getSelectedItem()).toString();

        return Application.context.sensorsController.get(selectedSensor);
    }

    public String getSerialNumber(){
        return this.serialNumber.getText();
    }
}
