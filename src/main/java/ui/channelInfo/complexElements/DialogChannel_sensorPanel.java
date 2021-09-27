package ui.channelInfo.complexElements;

import constants.MeasurementConstants;
import constants.SensorType;
import sensors.Sensor;
import support.Lists;
import ui.UI_Container;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class DialogChannel_sensorPanel extends JPanel implements UI_Container {

    private JComboBox<String>sensorsList;
    private MeasurementConstants currentMeasurement;

    public DialogChannel_sensorPanel(){
        super();

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.sensorsList = new JComboBox<>();
        this.sensorsList.setEditable(false);
        this.sensorsList.setBackground(Color.white);
    }

    @Override
    public void setReactions() {}

    @Override
    public void build() {
        this.add(this.sensorsList);
    }

    public void update(MeasurementConstants measurementName) {
        this.currentMeasurement = measurementName;
        if (measurementName != null) {
            String[]sensors = this.sensorsArray(measurementName.getValue());
            DefaultComboBoxModel<String>model = new DefaultComboBoxModel<>(sensors);
            this.sensorsList.setModel(model);
        }
    }

    public void update(Sensor sensor){
        if (sensor != null){
            if (currentMeasurement.getValue().equals(sensor.getMeasurement())) {
                String[] sensors = this.sensorsArray(sensor.getMeasurement());
                for (int x = 0; x < sensors.length; x++) {
                    if (sensor.getType() == SensorType.getConstantFromString(sensors[x])) {
                        this.sensorsList.setSelectedIndex(x);
                        break;
                    }
                }
            }
        }
    }

    private String[] sensorsArray(String measurement) {
        ArrayList<String> s = new ArrayList<>();
        for (int x = 0; x< Objects.requireNonNull(Lists.sensors()).size(); x++) {
            if (Objects.requireNonNull(Lists.sensors()).get(x).getMeasurement().equals(measurement)){
                s.add(Objects.requireNonNull(Lists.sensors()).get(x).getType().getType());
            }
        }
        return s.toArray(new String[0]);
    }

    public Sensor getSensor(){
        ArrayList<Sensor>sensors = Lists.sensors();
        String selectedSensor = Objects.requireNonNull(this.sensorsList.getSelectedItem()).toString();

        for (Sensor sensor : Objects.requireNonNull(sensors)){
            if (sensor.getType() == SensorType.getConstantFromString(selectedSensor)){
                return sensor;
            }
        }
        return null;
    }
}
