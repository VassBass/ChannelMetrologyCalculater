package ui.sensorsList.sensorInfo.complexElements;

import converters.VariableConverter;
import service.repository.repos.measurement.MeasurementRepositorySQLite;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SensorRangePanel extends JPanel {
    private static final String DEFAULT_VALUE = "0.00";
    private static final String DASH = " - ";

    private final SensorInfoDialog parent;

    private JTextField rangeMin, rangeMax;
    private JLabel t;
    private JComboBox<String>values;
    private String rMin, rMax;

    public SensorRangePanel(SensorInfoDialog parent){
        super(new GridBagLayout());
        this.parent = parent;

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

        this.values = new JComboBox<>();
        this.values.setBackground(Color.WHITE);
    }

    private void setReactions() {
        this.rangeMin.addFocusListener(rangeFocus);
        this.rangeMax.addFocusListener(rangeFocus);
        this.values.addFocusListener(focusValue);
    }

    private void build() {
        this.setBackground(Color.WHITE);

        this.add(this.rangeMin, new Cell(0));
        this.add(this.t, new Cell(1));
        this.add(this.rangeMax, new Cell(2));
        this.add(this.values, new Cell(3));
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
            this.rangeMin.setText(this.rMin);
            this.rangeMax.setText(this.rMax);
        }else {
            this.rMin = this.rangeMin.getText();
            this.rMax = this.rangeMax.getText();
            this.rangeMin.setText(DASH);
            this.rangeMax.setText(DASH);
        }
        this.values.setEnabled(enabled);
        this.rangeMin.setEnabled(enabled);
        this.rangeMax.setEnabled(enabled);
    }

    public void setValues(String measurementName){
        if (measurementName != null && measurementName.length() > 0){
            String[]values = MeasurementRepositorySQLite.getInstance().getValues(measurementName);
            DefaultComboBoxModel<String>model = new DefaultComboBoxModel<>(values);
            this.values.setModel(model);
            setEnabled(true);
            if (rangeMin.getText().length() == 0) rangeMin.setText(DEFAULT_VALUE);
            if (rangeMax.getText().length() == 0) rangeMax.setText(DEFAULT_VALUE);
        }else {
            DefaultComboBoxModel<String>dash = new DefaultComboBoxModel<>(new String[]{DASH});
            this.values.setModel(dash);
            setEnabled(false);
        }
    }

    public void setValue(String value){
        if (value != null && value.length() > 0){
            this.values.setSelectedItem(value);
        }
    }

    public String getValue(){
        if (this.isEnabled() && this.values.getSelectedItem() != null){
            return this.values.getSelectedItem().toString();
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
            parent.specialCharactersPanel.setFieldForInsert(null);
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

    private final FocusListener focusValue = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            parent.specialCharactersPanel.setFieldForInsert(null);
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