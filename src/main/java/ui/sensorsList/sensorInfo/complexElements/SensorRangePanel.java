package ui.sensorsList.sensorInfo.complexElements;

import constants.MeasurementConstants;
import converters.VariableConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SensorRangePanel extends JPanel {
    private static final String DEFAULT_VALUE = "0.00";
    private static final String DASH = " - ";

    private JTextField rangeMin, rangeMax;
    private JLabel t, value;
    private String rMin, rMax;

    public SensorRangePanel(){
        super(new GridBagLayout());

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.rangeMin = new JTextField(DEFAULT_VALUE,4);
        this.rangeMin.setHorizontalAlignment(SwingConstants.CENTER);

        this.rangeMax = new JTextField(DEFAULT_VALUE,4);
        this.rangeMax.setHorizontalAlignment(SwingConstants.CENTER);

        this.t = new JLabel(DASH);
        this.t.setHorizontalAlignment(SwingConstants.CENTER);

        this.value = new JLabel(MeasurementConstants.DEGREE_CELSIUS.getValue());
    }

    private void setReactions() {
        this.rangeMin.addFocusListener(rangeFocus);
        this.rangeMax.addFocusListener(rangeFocus);
    }

    private void build() {
        this.add(this.rangeMin, new Cell(0));
        this.add(this.t, new Cell(1));
        this.add(this.rangeMax, new Cell(2));
        this.add(this.value, new Cell(3));
    }

    public void setRange(double r1, double r2){
        if (r1 >= r2){
            this.rMax = String.valueOf(r1);
            this.rMin = String.valueOf(r2);
        }else {
            this.rMax = String.valueOf(r2);
            this.rMin = String.valueOf(r1);
        }
        this.rangeMax.setText(this.rMax);
        this.rangeMin.setText(this.rMin);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled){
            this.value.setText(MeasurementConstants.DEGREE_CELSIUS.getValue());
            this.rangeMin.setText(this.rMin);
            this.rangeMax.setText(this.rMax);
        }else {
            this.rMin = this.rangeMin.getText();
            this.rMax = this.rangeMax.getText();
            this.value.setText(DASH);
            this.rangeMin.setText(DASH);
            this.rangeMax.setText(DASH);
        }
        this.rangeMin.setEnabled(enabled);
        this.rangeMax.setEnabled(enabled);
    }

    public String getValue(){
        if (this.isEnabled()){
            return this.value.getText();
        }else {
            return null;
        }
    }

    public double getRangeMin(){
        try {
            return Double.parseDouble(VariableConverter.doubleString(this.rangeMin.getText()));
        }catch (Exception e){
            return 0D;
        }
    }

    public double getRangeMax(){
        try {
            return Double.parseDouble(VariableConverter.doubleString(this.rangeMax.getText()));
        }catch (Exception e){
            return 0D;
        }
    }

    private final FocusListener rangeFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField field = (JTextField) e.getSource();
            field.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (rangeMin.getText().length() == 0){
                rangeMin.setText(DEFAULT_VALUE);
            }
            if (rangeMax.getText().length() == 0){
                rangeMax.setText(DEFAULT_VALUE);
            }
            double r1 = 0D;
            double r2 = 0D;
            try {
                r1 = Double.parseDouble(VariableConverter.doubleString(rangeMin.getText()));
                r2 = Double.parseDouble(VariableConverter.doubleString(rangeMax.getText()));
            }catch (Exception ignored){}
            setRange(r1, r2);
        }
    };

    private static class Cell extends GridBagConstraints {
        protected Cell(int x){
            super();

            this.fill = BOTH;
            this.weightx = 1D;
            this.weighty = 1D;

            this.gridx = x;
            this.gridy = 0;
        }
    }
}