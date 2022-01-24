package ui.channelInfo.complexElements;

import constants.MeasurementConstants;
import converters.VariableConverter;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class DialogChannel_rangePanel extends JPanel {
    private static final String DEFAULT_MIN_VALUE = "0.00";
    private static final String DEFAULT_MAX_VALUE = "100.00";
    private static final String DASH = " - ";

    private final DialogChannel parent;

    private JTextField minRange;
    private JLabel dath;
    private JTextField maxRange;
    private JLabel value;

    public final double OLD = -999999999D;

    public DialogChannel_rangePanel(DialogChannel parent){
        super();
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.minRange = new JTextField(DEFAULT_MIN_VALUE,5);
        this.dath = new JLabel(DASH);
        this.maxRange = new JTextField(DEFAULT_MAX_VALUE,5);
        this.value = new JLabel(MeasurementConstants.DEGREE_CELSIUS.getValue());
    }

    private void setReactions() {
        this.minRange.addFocusListener(this.minRangeFocus);
        this.maxRange.addFocusListener(this.maxRangeFocus);
    }

    private void build() {
        this.add(this.minRange);
        this.add(this.dath);
        this.add(this.maxRange);
        this.add(this.value);
    }

    public void updateValue(String value) {
        if (value != null){
            this.value.setText(value);
        }
    }

    public void updateRange(double rangeMin, double rangeMax){
        if (rangeMin == OLD) {
            rangeMin = Double.parseDouble(this.minRange.getText());
        }
        if (rangeMax == OLD) {
            rangeMax = Double.parseDouble(this.maxRange.getText());
        }
        this.setTrueValues(rangeMax, rangeMin);
    }

    private final FocusListener minRangeFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            minRange.selectAll();
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

            parent.allowableErrorPanel.updateRange(range);
            if (parent.rangeLikeChannel.isSelected()){
                parent.sensorRangePanel.setRange(minRange.getText(), maxRange.getText());
            }
        }
    };

    private final FocusListener maxRangeFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            maxRange.selectAll();
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

            parent.allowableErrorPanel.updateRange(range);
            if (parent.rangeLikeChannel.isSelected()){
                parent.sensorRangePanel.setRange(minRange.getText(), maxRange.getText());
            }
        }
    };

    public double getRange(){
        double max = Double.parseDouble(this.maxRange.getText());
        double min = Double.parseDouble(this.minRange.getText());
        return max - min;
    }

    private void setTrueValues(double val1, double val2){
        if (val1 >= val2){
            this.minRange.setText(VariableConverter.roundingDouble2(val2, Locale.ENGLISH));
            this.maxRange.setText(VariableConverter.roundingDouble2(val1, Locale.ENGLISH));
        }else {
            this.minRange.setText(VariableConverter.roundingDouble2(val1, Locale.ENGLISH));
            this.maxRange.setText(VariableConverter.roundingDouble2(val2, Locale.ENGLISH));
        }
    }

    public double getRangeMin(){
        return Double.parseDouble(this.minRange.getText());
    }
    public double getRangeMax(){
        return Double.parseDouble(this.maxRange.getText());
    }
}