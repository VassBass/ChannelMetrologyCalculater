package ui.channelInfo.panel;

import converters.VariableConverter;
import model.Measurement;
import ui.channelInfo.DialogChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.Optional;

public class PanelChannelRange extends JPanel {
    private static final String RANGE_OF_CHANNEL = "Діапазон вимірювального каналу";
    private static final String DEFAULT_MIN_VALUE = "0.00";
    private static final String DEFAULT_MAX_VALUE = "100.00";
    private static final String SEPARATOR = " ... ";

    private final DialogChannel parent;

    private final JTextField minRange;
    private final JTextField maxRange;
    private final JLabel value;

    private final TitledBorder border;

    public PanelChannelRange(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        this.setBackground(Color.WHITE);
        this.setBorder(border = BorderFactory.createTitledBorder(RANGE_OF_CHANNEL));
        border.setTitleJustification(TitledBorder.CENTER);

        this.add(minRange = new MinRangeTextField(), new Cell(0,0));
        this.add(new JLabel(SEPARATOR), new Cell(1,0));
        this.add(maxRange = new MaxRangeTextField(), new Cell(2,0));
        this.add(value = new JLabel(), new Cell(3,0));
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    public void updateTitle(@Nonnull String title){
        ((TitledBorder) this.getBorder()).setTitle(title);
    }

    public void updateMeasurementValue(@Nonnull String measurementValue) {
        value.setText(measurementValue);
    }

    public void updateRange(@Nullable Double rangeMin,@Nullable Double rangeMax){
        if (rangeMin == null) {
            rangeMin = Double.parseDouble(this.minRange.getText());
        }
        if (rangeMax == null) {
            rangeMax = Double.parseDouble(this.maxRange.getText());
        }

        setTrueValues(rangeMax, rangeMin);
    }

    public double getRange(){
        double max = Double.parseDouble(this.maxRange.getText());
        double min = Double.parseDouble(this.minRange.getText());
        return max - min;
    }

    private void setTrueValues(double val1, double val2){
        if (val1 >= val2){
            minRange.setText(VariableConverter.roundingDouble2(val2, Locale.ENGLISH));
            maxRange.setText(VariableConverter.roundingDouble2(val1, Locale.ENGLISH));
        }else {
            minRange.setText(VariableConverter.roundingDouble2(val1, Locale.ENGLISH));
            maxRange.setText(VariableConverter.roundingDouble2(val2, Locale.ENGLISH));
        }
    }

    public double getRangeMin(){
        return Double.parseDouble(this.minRange.getText());
    }
    public double getRangeMax(){
        return Double.parseDouble(this.maxRange.getText());
    }

    public boolean isRangeAvailable(){
        Optional<Measurement> m = parent.panelMeasurement.getMeasurement();

        if (m.isPresent()) {
            Measurement measurement = m.get();

            PanelSensorRange panelSensorRange = parent.panelSensor.panelSensorRange;
            String sensorValue = panelSensorRange.getValue();

            double sensorRangeMin = measurement.convertFrom(sensorValue, panelSensorRange.getRangeMin());
            double sensorRangeMax = measurement.convertFrom(sensorValue, panelSensorRange.getRangeMax());
            double channelRangeMin = this.getRangeMin();
            double channelRangeMax = this.getRangeMax();

            if (channelRangeMin < sensorRangeMin || channelRangeMax > sensorRangeMax){
                border.setTitleColor(Color.RED);
                parent.panelSensor.setBorderColor(Color.RED);

                return false;
            }else {
                border.setTitleColor(Color.BLACK);
                parent.panelSensor.setBorderColor(Color.BLACK);

                return true;
            }
        } else return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        minRange.setEnabled(enabled);
        maxRange.setEnabled(enabled);
    }

    /**
     * TextField for min value of channel range
     */
    private class MinRangeTextField extends JTextField {

        private MinRangeTextField(){
            super(DEFAULT_MIN_VALUE, 5);
            this.setHorizontalAlignment(JTextField.RIGHT);

            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                minRange.selectAll();
                parent.panelSpecialCharacters.setFieldForInsert(null);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (minRange.getText().length() == 0) {
                    minRange.setText(DEFAULT_MIN_VALUE);
                }
                String forCheck = minRange.getText();
                minRange.setText(VariableConverter.doubleString(forCheck));
                double min = Double.parseDouble(minRange.getText());
                double max = Double.parseDouble(maxRange.getText());
                setTrueValues(min, max);
                double range = max - min;

                parent.panelAllowableError.updateChannelRange(range);
                if (parent.panelSensor.panelSensorRange.isRangesMatch()){
                    parent.panelSensor.panelSensorRange.setRange(minRange.getText(), maxRange.getText());
                }
            }
        };
    }

    /**
     * TextField for max value of channel range
     */
    private class MaxRangeTextField extends JTextField {

        private MaxRangeTextField(){
            super(DEFAULT_MAX_VALUE, 5);

            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                maxRange.selectAll();
                parent.panelSpecialCharacters.setFieldForInsert(null);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (maxRange.getText().length() == 0) {
                    maxRange.setText(DEFAULT_MIN_VALUE);
                }
                String forCheck = maxRange.getText();
                maxRange.setText(VariableConverter.doubleString(forCheck));
                double min = Double.parseDouble(minRange.getText());
                double max = Double.parseDouble(maxRange.getText());
                setTrueValues(min, max);
                double range = max - min;

                parent.panelAllowableError.updateChannelRange(range);
                if (parent.panelSensor.panelSensorRange.isRangesMatch()){
                    parent.panelSensor.panelSensorRange.setRange(minRange.getText(), maxRange.getText());
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
