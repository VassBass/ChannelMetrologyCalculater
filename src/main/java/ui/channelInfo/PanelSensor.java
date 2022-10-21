package ui.channelInfo;

import model.Channel;
import model.Measurement;
import model.Sensor;
import repository.SensorRepository;
import repository.impl.SensorRepositorySQLite;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PanelSensor extends JPanel {
    private static final String NO = "NO.";
    private static final String ADD_NEW_SENSOR = "<-Додати новий->";
    private static final String SENSOR = "Первинний вимірювальний пристрій";

    private final DialogChannel parent;

    private JComboBox<String>sensorsList;
    private JLabel number;
    private JTextField serialNumber;
    private String currentMeasurement;

    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();

    public PanelSensor(DialogChannel parent){
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

        this.sensorsList.addFocusListener(focusOn);
        this.serialNumber.addFocusListener(focusOn);
    }

    private void build() {
        this.setBackground(Color.WHITE);

        this.add(this.sensorsList);

        TitledBorder border = BorderFactory.createTitledBorder(SENSOR);
        this.setBorder(border);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    private final ItemListener changeSensorName = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && sensorsList.getSelectedItem() != null) {
                if (sensorsList.getSelectedItem().toString().equals(ADD_NEW_SENSOR)) {
                    EventQueue.invokeLater(() -> new SensorInfoDialog(parent).setVisible(true));
                } else {
                    if (parent.measurementPanel.getMeasurement().getName().equals(Measurement.CONSUMPTION)) {
                        Channel channel = new Channel();
                        channel.setMeasurement(parent.measurementPanel.getMeasurement());
                        channel.setRangeMin(parent.rangePanel.getRangeMin());
                        channel.setRangeMax(parent.rangePanel.getRangeMax());
                        Sensor sensor = getSensor();
                        sensor.setValue(channel.getMeasurement().getValue());
                        double errorSensor = sensor.getError(channel);
                        parent.allowableErrorPanel.updateError(errorSensor, false, channel._getRange());
                        if (sensor.getType().toUpperCase(Locale.ROOT).contains(Sensor.ROSEMOUNT)) {
                            parent.measurementPanel.setRosemountValues();
                        } else {
                            parent.measurementPanel.update(Measurement.CONSUMPTION);
                        }
                    }
                    if (parent.sensorRangePanel != null && !parent.rangeLikeChannel.isSelected()){
                        parent.sensorRangePanel.update(sensorRepository.get(sensorsList.getSelectedItem().toString()).get());
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

    private final FocusListener focusOn = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            parent.resetSpecialCharactersPanel();
        }
    };

    public void updateMeasurementName(@Nonnull String measurementName) {
        this.removeAll();

        this.currentMeasurement = measurementName;
        String[]s = sensorRepository.getAllSensorsName(measurementName).toArray(new String[0]);
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
                List<String> sensors = new ArrayList<>(sensorRepository.getAllSensorsName(sensor.getMeasurement()));
                for (int x = 0; x < sensors.size(); x++) {
                    if (sensor.getName().equals(sensors.get(x))) {
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

    public Optional<Sensor> getSelectedSensor(){
        Object selectedSensor = sensorsList.getSelectedItem();
        return selectedSensor == null ? Optional.empty() : sensorRepository.get(selectedSensor.toString());
    }

    public String getSerialNumber(){
        return this.serialNumber.getText();
    }

    private class SensorsComboBox extends JComboBox<String> {

        private SensorsComboBox(){
            super();

            this.setEditable(false);
            this.setBackground(Color.WHITE);
        }

        private final ItemListener changeSensorName = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object selectedSensor = SensorsComboBox.this.getSelectedItem();
                if (e.getStateChange() == ItemEvent.SELECTED && selectedSensor != null) {
                    if (selectedSensor.toString().equals(ADD_NEW_SENSOR)) {
                        EventQueue.invokeLater(() -> new SensorInfoDialog(parent).setVisible(true));
                    } else {
                        Optional<Measurement>m = parent.panelMeasurement.getMeasurement();
                        if (m.isPresent()){
                            Measurement measurement = m.get();
                            if (measurement.getName().equals(Measurement.CONSUMPTION)){
                                Channel channel = new Channel();
                                channel.setMeasurement(measurement);
                                channel.setRangeMin(parent.panelChannelRange.getRangeMin());
                                channel.setRangeMax(parent.panelChannelRange.getRangeMax());

                                Optional<Sensor>s =

                            }
                        }
                        if (parent.panelMeasurement.getMeasurement().getName().equals(Measurement.CONSUMPTION)) {
                            Channel channel = new Channel();
                            channel.setMeasurement(parent.measurementPanel.getMeasurement());
                            channel.setRangeMin(parent.rangePanel.getRangeMin());
                            channel.setRangeMax(parent.rangePanel.getRangeMax());
                            Sensor sensor = getSensor();
                            sensor.setValue(channel.getMeasurement().getValue());
                            double errorSensor = sensor.getError(channel);
                            parent.allowableErrorPanel.updateError(errorSensor, false, channel._getRange());
                            if (sensor.getType().toUpperCase(Locale.ROOT).contains(Sensor.ROSEMOUNT)) {
                                parent.measurementPanel.setRosemountValues();
                            } else {
                                parent.measurementPanel.update(Measurement.CONSUMPTION);
                            }
                        }
                        if (parent.sensorRangePanel != null && !parent.rangeLikeChannel.isSelected()){
                            parent.sensorRangePanel.update(sensorRepository.get(sensorsList.getSelectedItem().toString()).get());
                        }
                    }
                }
            }
        };
    }
}
