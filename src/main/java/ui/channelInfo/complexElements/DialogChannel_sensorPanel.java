package ui.channelInfo.complexElements;

import application.Application;
import model.Channel;
import model.Measurement;
import model.Sensor;
import ui.channelInfo.DialogChannel;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.Objects;

public class DialogChannel_sensorPanel extends JPanel {
    private static final String NO = "NO.";
    private static final String ADD_NEW_SENSOR = "<-Додати новий->";

    private final DialogChannel parent;

    private JComboBox<String>sensorsList;
    private JLabel number;
    private JTextField serialNumber;
    private String currentMeasurement;

    public DialogChannel_sensorPanel(DialogChannel parent){
        super();
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.sensorsList = new JComboBox<>();
        this.sensorsList.setEditable(false);
        this.sensorsList.setBackground(Color.WHITE);

        this.number = new JLabel(NO);
        this.serialNumber = new JTextField(10);
    }

    private void setReactions() {
        this.sensorsList.addItemListener(this.changeSensorName);

        this.serialNumber.getDocument().addDocumentListener(this.serialNumberUpdate);
    }

    private void build() {
        this.add(this.sensorsList);
    }

    private final ItemListener changeSensorName = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && sensorsList.getSelectedItem() != null) {
                if (sensorsList.getSelectedItem().toString().equals(ADD_NEW_SENSOR)) {
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new SensorInfoDialog(parent).setVisible(true);
                        }
                    });
                } else {
                    if (parent.measurementPanel.getMeasurement().getName().equals(Measurement.CONSUMPTION)) {
                        Channel channel = new Channel();
                        channel.setMeasurement(parent.measurementPanel.getMeasurement());
                        channel.setRangeMin(parent.rangePanel.getRangeMin());
                        channel.setRangeMax(parent.rangePanel.getRangeMax());
                        Sensor sensor = getSensor();
                        sensor.setValue(channel.getMeasurement().getValue());
                        double errorSensorInPercent = sensor.getError(channel);
                        parent.allowableErrorPanel.updateError(errorSensorInPercent, true, channel._getRange());
                        if (sensor.getType().toUpperCase(Locale.ROOT).contains(Sensor.ROSEMOUNT)) {
                            parent.measurementPanel.setRosemountValues();
                        } else {
                            parent.measurementPanel.update(Measurement.CONSUMPTION);
                        }
                    }
                    if (parent.sensorRangePanel != null){
                        parent.sensorRangePanel.update(Application.context.sensorService.get(sensorsList.getSelectedItem().toString()));
                    }
                }
            }
        }
    };

    private final DocumentListener serialNumberUpdate = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            serialNumber.setToolTipText(serialNumber.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (serialNumber.getText().length() > 0) {
                serialNumber.setToolTipText(serialNumber.getText());
            }
        }

        @Override public void changedUpdate(DocumentEvent e) {}
    };

    public void update(String measurementName) throws NullPointerException {
        if (measurementName == null) throw new NullPointerException();
        this.removeAll();

        this.currentMeasurement = measurementName;
        String[]s = Application.context.sensorService.getAllSensorsName(measurementName);
        String[]sensors = new String[s.length + 1];
        System.arraycopy(s, 0, sensors, 0, s.length);
        sensors[sensors.length - 1] = ADD_NEW_SENSOR;
        DefaultComboBoxModel<String>model = new DefaultComboBoxModel<>(sensors);
        this.sensorsList.setModel(model);
        if (Objects.requireNonNull(this.sensorsList.getSelectedItem()).toString().contains(Sensor.ROSEMOUNT)){
            this.parent.measurementPanel.setRosemountValues();
        }

        this.add(this.sensorsList);
        if (measurementName.equals(Measurement.CONSUMPTION)){
            this.add(this.number);
            this.add(this.serialNumber);
        }
    }

    public void update(Sensor sensor){
        if (sensor != null){
            if (this.currentMeasurement.equals(sensor.getMeasurement())) {
                String[] sensors = Application.context.sensorService.getAllSensorsName(sensor.getMeasurement());
                for (int x = 0; x < sensors.length; x++) {
                    if (sensor.getName().equals(sensors[x])) {
                        this.sensorsList.setSelectedIndex(x);
                        break;
                    }
                }
                if (this.currentMeasurement.equals(Measurement.CONSUMPTION)){
                    this.serialNumber.setText(sensor.getNumber());
                }
            }
        }
    }

    public void setSelectedSensor(String sensorName){
        if (sensorName == null){
            this.sensorsList.setSelectedIndex(0);
        }else {
            this.sensorsList.setSelectedItem(sensorName);
        }
    }

    public Sensor getSensor(){
        String selectedSensor = Objects.requireNonNull(this.sensorsList.getSelectedItem()).toString();
        return Application.context.sensorService.get(selectedSensor);
    }

    public String getSerialNumber(){
        return this.serialNumber.getText();
    }
}