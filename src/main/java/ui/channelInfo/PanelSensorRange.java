package ui.channelInfo;

import converters.VariableConverter;
import model.Measurement;
import model.Sensor;
import repository.MeasurementRepository;
import repository.impl.MeasurementRepositorySQLite;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.Optional;

public class PanelSensorRange extends JPanel {
    private static final String MIN = "Від ";
    private static final String MAX = " до ";
    private static final String DEFAULT_MIN_VALUE = "0.00";
    private static final String DEFAULT_MAX_VALUE = "100.00";

    private final JTextField min;
    private final JTextField max;
    private final JComboBox<String>value;
    private final JCheckBox rangesMatch;

    private final DialogChannel parent;

    private final MeasurementRepository measurementRepository = MeasurementRepositorySQLite.getInstance();

    PanelSensorRange(@Nonnull DialogChannel parent){
        super();
        this.parent = parent;

        min = new MinValueTextField();
        max = new MaxValueTextField();
        value = new MeasurementValueComboBox();
        rangesMatch = new RangesMatchCheckBox();

        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel(MIN));
        topPanel.add(min);
        topPanel.add(new JLabel(MAX));
        topPanel.add(max);
        topPanel.add(value);

        this.add(topPanel);
        this.add(rangesMatch);
    }

    public void updateMeasurement(@Nonnull Measurement measurement){
        value.setModel(new DefaultComboBoxModel<>(measurementRepository.getValues(measurement)));
    }

    public void updateSensor(@Nonnull Sensor sensor) {
        this.min.setText(VariableConverter.roundingDouble2(sensor.getRangeMin(), Locale.ENGLISH));
        this.max.setText(VariableConverter.roundingDouble2(sensor.getRangeMax(), Locale.ENGLISH));
        this.value.setSelectedItem(sensor.getValue());
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.min.setEnabled(enabled);
        this.max.setEnabled(enabled);
        this.value.setEnabled(enabled);
        this.rangesMatch.setEnabled(enabled);
    }

    public void setDisabled(boolean disabled){
        this.min.setEnabled(!disabled);
        this.max.setEnabled(!disabled);
        this.value.setEnabled(!disabled);
    }

    public void setRange(@Nonnull String min, @Nonnull String max, @Nonnull String value){
        this.min.setText(min);
        this.max.setText(max);
        this.value.setSelectedItem(value);
    }

    public void setRange(@Nonnull String min, @Nonnull String max){
        this.min.setText(min);
        this.max.setText(max);
    }

    public void setRangeMin(@Nonnull String min){
        this.min.setText(min);
    }

    public void setRangeMax(@Nonnull String max){
        this.max.setText(max);
    }

    public void updateMeasurementValue(@Nonnull String value){
        this.value.setSelectedItem(value);
    }

    public boolean isRangesMatch(){
        return rangesMatch.isSelected();
    }

    public double getRangeMin(){
        return Double.parseDouble(this.min.getText());
    }
    public double getRangeMax(){
        return Double.parseDouble(this.max.getText());
    }

    public String getValue(){
        Object selected = value.getSelectedItem();
        return selected == null ? Measurement.DEGREE_CELSIUS : selected.toString();
    }

    private class MinValueTextField extends JTextField {

        private MinValueTextField() {
            super(DEFAULT_MIN_VALUE, 5);

            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                MinValueTextField.this.selectAll();
                parent.specialCharactersPanel.setFieldForInsert(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField source = MinValueTextField.this;

                if (source.getText().length() == 0) {
                    source.setText(DEFAULT_MIN_VALUE);
                }
                String forCheck = source.getText();
                source.setText(VariableConverter.doubleString(forCheck));

                double minD = Double.parseDouble(min.getText());
                double maxD = Double.parseDouble(max.getText());
                if (maxD < minD) {
                    min.setText(String.valueOf(maxD));
                    max.setText(String.valueOf(minD));
                }
            }
        };
    }

    private class MaxValueTextField extends JTextField {

        private MaxValueTextField() {
            super(DEFAULT_MAX_VALUE, 5);

            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                MaxValueTextField.this.selectAll();
                parent.specialCharactersPanel.setFieldForInsert(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField source = MaxValueTextField.this;

                if (source.getText().length() == 0) {
                    source.setText(DEFAULT_MIN_VALUE);
                }
                String forCheck = source.getText();
                source.setText(VariableConverter.doubleString(forCheck));
            }
        };
    }

    private class MeasurementValueComboBox extends JComboBox<String> {

        private MeasurementValueComboBox(){
            super();

            this.setBackground(Color.WHITE);
            this.setEditable(false);

            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(null);
            }
        };
    }

    private class RangesMatchCheckBox extends JCheckBox {
        private static final String RANGES_MATCH = "Однакові діапазони";

        private RangesMatchCheckBox(){
            super(RANGES_MATCH);

            this.setBackground(Color.WHITE);

            this.addFocusListener(focus);
            this.addItemListener(click);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                parent.specialCharactersPanel.setFieldForInsert(null);
            }
        };

        @SuppressWarnings("FieldCanBeLocal")
        private final ItemListener click = e -> {
            if (parent.panelSensor.panelSensorRange.isEnabled()) {
                if (RangesMatchCheckBox.this.isSelected()) {
                    Optional<Measurement> m = parent.panelMeasurement.getMeasurement();
                    m.ifPresent(measurement ->
                            parent.panelSensor.panelSensorRange.setRange(
                                    String.valueOf(parent.panelChannelRange.getRangeMin()),
                                    String.valueOf(parent.panelChannelRange.getRangeMax()),
                                    measurement.getValue()
                            ));
                    parent.panelSensor.panelSensorRange.setDisabled(true);
                } else {
                    parent.panelSensor.panelSensorRange.setDisabled(false);

                    Optional<Sensor> s = parent.panelSensor.getSelectedSensor();
                    s.ifPresent(sensor -> parent.panelSensor.panelSensorRange.updateSensor(sensor));
                }
            }
        };
    }
}
