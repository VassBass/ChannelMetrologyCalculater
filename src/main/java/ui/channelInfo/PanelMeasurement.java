package ui.channelInfo;

import model.Channel;
import model.Measurement;
import model.Sensor;
import repository.MeasurementRepository;
import repository.impl.MeasurementRepositorySQLite;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Optional;

public class PanelMeasurement extends JPanel {
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String RANGE_OF_CHANNEL = "Діапазон вимірювального каналу";
    private static final String RANGE_OF_SENSOR = "Діапазон вимірювання ПВП";

    private final DialogChannel parent;

    private final MeasurementNamesComboBox measurementNames;
    private final MeasurementValuesComboBox measurementValues;

    private final MeasurementRepository measurementRepository = MeasurementRepositorySQLite.getInstance();

    PanelMeasurement(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        measurementNames = new MeasurementNamesComboBox();
        measurementValues = new MeasurementValuesComboBox();

        this.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(TYPE_OF_MEASUREMENT);
        border.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(border);

        this.add(measurementNames, new Cell(0,0));
        this.add(measurementValues, new Cell(1,0));
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    public void updateMeasurementValue(@Nonnull String value){
        measurementValues.setSelectedItem(value);
    }

    public void updateMeasurementName(@Nonnull String measurementName){
        measurementNames.setSelectedItem(measurementName);
        measurementValues.setModel(
                new DefaultComboBoxModel<>(measurementRepository.getValues(measurementName)));
    }

    public void updateMeasurement(@Nonnull Measurement measurement) {
        this.updateMeasurementName(measurement.getName());
        this.updateMeasurementValue(measurement.getValue());
    }

    public void setRosemountValues(){
        measurementValues.setModel(
                new DefaultComboBoxModel<>(rosemountValues()));
    }

    private String[] rosemountValues(){
        ArrayList<String> values  = new ArrayList<>();
        ArrayList<Measurement>measurements = new ArrayList<>(measurementRepository.getAll());
        for (Measurement measurement : measurements) {
            if (measurement.getName().equals(Measurement.CONSUMPTION)) {
                values.add(measurement.getValue());
            }
        }
        values.add(Measurement.M_S);
        values.add(Measurement.CM_S);
        return values.toArray(new String[0]);
    }

    public Optional<Measurement> getMeasurement(){
        Object selectedMeasurement = measurementValues.getSelectedItem();
        return selectedMeasurement == null ? Optional.empty() : measurementRepository.get(selectedMeasurement.toString());
    }

    /**
     * ComboBox with measurement names
     */
    @SuppressWarnings("FieldCanBeLocal")
    private class MeasurementNamesComboBox extends JComboBox<String> {

        private MeasurementNamesComboBox(){
            super(measurementRepository.getAllNames());

            this.setEditable(false);
            this.setBackground(Color.WHITE);

            this.addItemListener(changeName);
            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    parent.specialCharactersPanel.setFieldForInsert(null);
                }
            });
        }

        private final ItemListener changeName = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object selectedName = measurementNames.getSelectedItem();
                    if (selectedName != null) {
                        String name = selectedName.toString();

                        updateMeasurementName(name);

                        Object selectedValue = measurementValues.getSelectedItem();
                        if (selectedValue != null){
                            String val = selectedValue.toString();

                            updateMeasurementValue(val);

                            Measurement measurement = new Measurement(name, val);
                            updateMeasurement(measurement);
                        }
                    }

                    MeasurementNamesComboBox.this.requestFocus();
                }
            }
        };

        private void updateMeasurementName(String measurementName){
            parent.panelMeasurement.updateMeasurementName(measurementName);
            parent.panelSensor.updateMeasurementName(measurementName);
        }

        private void updateMeasurementValue(String measurementValue){
            parent.panelAllowableError.updateMeasurementValue(measurementValue);
            parent.panelChannelRange.updateMeasurementValue(measurementValue);
        }

        private void updateMeasurement(Measurement measurement){
            Optional<Sensor> s = parent.panelSensor.getSelectedSensor();
            if (measurement.getName().equals(Measurement.TEMPERATURE)
                    || measurement.getName().equals(Measurement.PRESSURE)) {
                parent.panelSensor.panelSensorRange.updateMeasurement(measurement);

                s.ifPresent(parent.panelSensor.panelSensorRange::updateSensor);

                parent.panelChannelRange.updateTitle(RANGE_OF_CHANNEL);
                parent.panelAllowableError.setEnabled(true);

            }else if (measurement.getName().equals(Measurement.CONSUMPTION)) {
                parent.panelSensor.panelSensorRange.setEnabled(false);
                parent.panelChannelRange.updateTitle(RANGE_OF_SENSOR);

                if (s.isPresent()) {
                    Channel channel = new Channel();
                    channel.setMeasurement(measurement);
                    channel.setRangeMin(parent.panelChannelRange.getRangeMin());
                    channel.setRangeMax(parent.panelChannelRange.getRangeMax());
                    Sensor sensor = s.get();
                    sensor.setValue(channel.getMeasurement().getValue());
                    double errorSensor = sensor.getError(channel);
                    parent.panelAllowableError.updateError(errorSensor, false, channel._getRange());
                    parent.panelAllowableError.setEnabled(false);
                }
            }
        }
    }

    /**
     * ComboBox with measurement values
     */
    @SuppressWarnings("FieldCanBeLocal")
    private class MeasurementValuesComboBox extends JComboBox<String> {

        private MeasurementValuesComboBox(){
            super(new String[0]);

            this.setEditable(false);
            this.setBackground(Color.WHITE);

            this.addItemListener(changeValue);
            this.addFocusListener(changeFocusOnValue);
        }

        private final ItemListener changeValue = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object selectedValue = MeasurementValuesComboBox.this.getSelectedItem();
                    if (selectedValue != null){
                        String val = selectedValue.toString();

                        parent.panelAllowableError.updateMeasurementValue(val);
                        parent.panelChannelRange.updateMeasurementValue(val);
                        if (parent.panelSensor.panelSensorRange.isRangesMatch()){
                            parent.panelSensor.panelSensorRange.updateMeasurementValue(val);
                        }
                    }
                }
            }
        };

        private final FocusListener changeFocusOnValue = new FocusListener() {
            @Override public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                Object selectedValue = MeasurementValuesComboBox.this.getSelectedItem();
                if (selectedValue != null){
                    String val = selectedValue.toString();

                    parent.panelAllowableError.updateMeasurementValue(val);
                    parent.panelChannelRange.updateMeasurementValue(val);
                }
            }
        };
    }

    private static class Cell extends GridBagConstraints {
        private Cell(int x, int y){
            super();
            this.weighty = 1D;

            this.gridx = x;
            this.gridy = y;
        }
    }
}
