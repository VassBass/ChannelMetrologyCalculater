package ui.channelInfo;

import model.Channel;
import model.Measurement;
import model.Sensor;
import repository.SensorRepository;
import repository.impl.SensorRepositorySQLite;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.Optional;

public class PanelSensor extends JPanel {
    private static final String NO = "NO.";
    private static final String ADD_NEW_SENSOR = "<-Додати новий->";
    private static final String SENSOR = "Первинний вимірювальний пристрій";

    private final DialogChannel parent;

    private final JComboBox<String>sensorsList = new SensorsComboBox();
    private final JTextField serialNumber = new SerialNumberTextField();
    public final PanelSensorRange panelSensorRange;

    private final TitledBorder border;

    private String currentMeasurement;

    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();

    PanelSensor(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        panelSensorRange = new PanelSensorRange(parent);

        this.setBackground(Color.WHITE);
        this.setBorder(border = BorderFactory.createTitledBorder(SENSOR));
        border.setTitleJustification(TitledBorder.CENTER);

        this.add(sensorsList, new Cell(0,0));
        this.add(new Label(NO), new Cell(1,0));
        this.add(serialNumber, new Cell(2,0));

        this.add(panelSensorRange, new Cell(0,1));
    }

    public void setBorderColor(Color color){
        border.setTitleColor(color);
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    public void updateMeasurementName(@Nonnull String measurementName) {
        this.currentMeasurement = measurementName;
        String[]s = sensorRepository.getAllSensorsName(measurementName).toArray(new String[0]);
        String[]sensors = new String[s.length + 1];
        System.arraycopy(s, 0, sensors, 0, s.length);
        sensors[sensors.length - 1] = ADD_NEW_SENSOR;
        DefaultComboBoxModel<String>model = new DefaultComboBoxModel<>(sensors);
        this.sensorsList.setModel(model);

        Object selectedSensor = sensorsList.getSelectedItem();
        if (selectedSensor != null && selectedSensor.toString().contains(Sensor.ROSEMOUNT)){
            this.parent.panelMeasurement.setRosemountValues();
        }

        Optional<Measurement> m = parent.panelMeasurement.getMeasurement();
        m.ifPresent(measurement -> {
            panelSensorRange.setEnabled(!measurement.getName().equals(Measurement.CONSUMPTION));
            panelSensorRange.setDisabled(panelSensorRange.isRangesMatch());
            panelSensorRange.updateMeasurement(measurement);
        });
    }

    public void updateSensor(@Nonnull Sensor sensor){
        if (this.currentMeasurement.equals(sensor.getMeasurement())) {
            sensorsList.setSelectedItem(sensor.getName());
            serialNumber.setText(sensor.getNumber());
            panelSensorRange.updateSensor(sensor);
        }
    }

    public void setSelectedSensor(@Nullable String sensorName){
        this.sensorsList.setSelectedItem(sensorName);
    }

    public Optional<Sensor> getSelectedSensor(){
        Object selectedSensor = sensorsList.getSelectedItem();
        return selectedSensor == null ? Optional.empty() : sensorRepository.get(selectedSensor.toString());
    }

    public String getSerialNumber(){
        return this.serialNumber.getText();
    }

    /**
     * Combo box of sensors names
     */
    private class SensorsComboBox extends JComboBox<String> {

        private SensorsComboBox(){
            super();

            this.setEditable(false);
            this.setBackground(Color.WHITE);

            this.addItemListener(changeSensorName);
            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
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

                                Optional<Sensor> s = sensorRepository.get(selectedSensor.toString());
                                if (s.isPresent()){
                                    Sensor sensor = s.get();
                                    sensor.setValue(measurement.getValue());
                                    double errorSensor = sensor.getError(channel);

                                    parent.panelAllowableError.updateError(errorSensor, false, channel._getRange());
                                    if (sensor.getType().toUpperCase(Locale.ROOT).contains(Sensor.ROSEMOUNT)) {
                                        parent.panelMeasurement.setRosemountValues();
                                    }else {
                                        parent.panelMeasurement.updateMeasurementName(Measurement.CONSUMPTION);
                                    }

                                    if (panelSensorRange.isRangesMatch()){
                                        panelSensorRange.updateSensor(sensor);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(null);
            }
        };
    }

    /**
     * Text field for serial number of sensor
     */
    private class SerialNumberTextField extends JTextField {

        private SerialNumberTextField(){
            super(10);

            this.getDocument().addDocumentListener(serialNumberUpdate);
            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final DocumentListener serialNumberUpdate = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                JTextField source = SerialNumberTextField.this;
                source.setToolTipText(source.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                JTextField source = SerialNumberTextField.this;
                if (source.getText().length() > 0) {
                    source.setToolTipText(source.getText());
                }
            }

            @Override public void changedUpdate(DocumentEvent e) {}
        };

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(SerialNumberTextField.this);
            }
        };
    }

    private static class Cell extends GridBagConstraints {
        private Cell(int x, int y){
            super();
            this.fill = BOTH;
            this.weighty = 1D;

            this.gridx = x;
            this.gridy = y;
        }
    }
}
