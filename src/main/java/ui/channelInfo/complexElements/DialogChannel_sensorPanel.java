package ui.channelInfo.complexElements;

import constants.MeasurementConstants;
import constants.Strings;
import model.Channel;
import model.Sensor;
import support.Lists;
import ui.UI_Container;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class DialogChannel_sensorPanel extends JPanel implements UI_Container {
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

    @Override
    public void createElements() {
        this.sensorsList = new JComboBox<>();
        this.sensorsList.setEditable(false);
        this.sensorsList.setBackground(Color.white);

        this.NO = new JLabel("NO.");
        this.serialNumber = new JTextField(10);
    }

    @Override
    public void setReactions() {
        this.sensorsList.addItemListener(changeSensorName);
    }

    @Override
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
                    parent.allowableErrorPanel.update(errorSensorInPercent, true, channel.getRange());
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
        ArrayList<String> s = new ArrayList<>();
        for (int x = 0; x< Objects.requireNonNull(Lists.sensors()).size(); x++) {
            if (Objects.requireNonNull(Lists.sensors()).get(x).getMeasurement().equals(measurement)){
                s.add(Objects.requireNonNull(Lists.sensors()).get(x).getName());
            }
        }
        return s.toArray(new String[0]);
    }

    public Sensor getSensor(){
        ArrayList<Sensor>sensors = Lists.sensors();
        String selectedSensor = Objects.requireNonNull(this.sensorsList.getSelectedItem()).toString();

        for (Sensor sensor : Objects.requireNonNull(sensors)){
            if (sensor.getName().equals(selectedSensor)){
                return sensor;
            }
        }
        return null;
    }

    public String getSerialNumber(){
        return this.serialNumber.getText();
    }
}
