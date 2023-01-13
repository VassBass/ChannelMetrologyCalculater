package ui.channelInfo.panel;

import converters.VariableConverter;
import model.Measurement;
import model.Sensor;
import service.repository.repos.measurement.MeasurementRepository;
import service.repository.repos.measurement.MeasurementRepositorySQLite;
import ui.channelInfo.DialogChannel;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class PanelSensorRange extends JPanel {
    private static final String MIN = "Від ";
    private static final String MAX = " до ";
    private static final String DEFAULT_MIN_VALUE = "0.00";
    private static final String DEFAULT_MAX_VALUE = "100.00";

    private final JTextField min;
    private final JTextField max;
    private final MeasurementValueComboBox value;
    private final JCheckBox rangesMatch;

    private final DialogChannel parent;

    private final MeasurementRepository measurementRepository = MeasurementRepositorySQLite.getInstance();

    public PanelSensorRange(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        min = new MinValueTextField();
        max = new MaxValueTextField();
        value = new MeasurementValueComboBox();
        rangesMatch = new RangesMatchCheckBox();

        this.setBackground(Color.WHITE);

        this.add(new JLabel(MIN), new Cell(0,0));
        this.add(min, new Cell(1,0));
        JLabel to = new JLabel(MAX);
        to.setHorizontalAlignment(JLabel.CENTER);
        this.add(to, new Cell(2,0));
        this.add(max, new Cell(3,0));
        this.add(value, new Cell(4,0));
        this.add(rangesMatch, new Cell(2,1));
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

    /**
     * Sets enabled or not ALL panel elements
     * @param enabled ALL panel elements
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        rangesMatch.setEnabled(enabled);
        rangesMatch.setSelected(!enabled);
        value.setEnabled(enabled);
    }

    /**
     * Sets disabled or not all panel elements EXCEPT FOR {@link #rangesMatch}
     * @param disabled all panel elements EXCEPT FOR {@link #rangesMatch}
     */
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

    public void setRosemountValues(){
        value.setRosemountValues();
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
                parent.panelSpecialCharacters.setFieldForInsert(null);
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

                min.setText(String.valueOf(Math.min(minD, maxD)));
                max.setText(String.valueOf(Math.max(minD, maxD)));
                if (!parent.panelSensor.panelSensorRange.isEnabled()) {
                    parent.panelChannelRange.updateRange(Math.min(minD, maxD), Math.max(minD, maxD));
                    parent.panelAllowableError.updateError();
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
                parent.panelSpecialCharacters.setFieldForInsert(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField source = MaxValueTextField.this;

                if (source.getText().length() == 0) {
                    source.setText(DEFAULT_MIN_VALUE);
                }
                String forCheck = source.getText();
                source.setText(VariableConverter.doubleString(forCheck));

                double minD = Double.parseDouble(min.getText());
                double maxD = Double.parseDouble(max.getText());

                min.setText(String.valueOf(Math.min(minD, maxD)));
                max.setText(String.valueOf(Math.max(minD, maxD)));
                if (!parent.panelSensor.panelSensorRange.isEnabled()) {
                    parent.panelChannelRange.updateRange(Math.min(minD, maxD), Math.max(minD, maxD));
                    parent.panelAllowableError.updateError();
                }
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
                parent.panelSpecialCharacters.setFieldForInsert(null);
            }
        };

        public void setRosemountValues(){
            this.setModel(new DefaultComboBoxModel<>(rosemountValues()));
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
                parent.panelSpecialCharacters.setFieldForInsert(null);
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
                    s.ifPresent(parent.panelSensor.panelSensorRange::updateSensor);
                }
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
