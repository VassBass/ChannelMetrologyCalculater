package ui.channelInfo;

import converters.VariableConverter;
import model.Measurement;
import model.Sensor;
import repository.MeasurementRepository;
import repository.impl.MeasurementRepositorySQLite;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.Objects;

public class PanelSensorRange extends JPanel {
    private static final String MIN = "Від ";
    private static final String MAX = " до ";
    private static final String DEFAULT_MIN_VALUE = "0.00";
    private static final String DEFAULT_MAX_VALUE = "100.00";

    private JLabel minLabel;
    private JLabel maxLabel;

    private JTextField min;
    private JTextField max;

    private JComboBox<String>value;

    private JCheckBox rangesMatch;

    private final DialogChannel parent;

    private MeasurementRepository measurementRepository = MeasurementRepositorySQLite.getInstance();

    public PanelSensorRange(DialogChannel parent){
        super();
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.minLabel = new JLabel(MIN);
        this.maxLabel = new JLabel(MAX);

        this.min = new JTextField(DEFAULT_MIN_VALUE, 5);
        this.max = new JTextField(DEFAULT_MAX_VALUE, 5);

        this.value = new JComboBox<>();
        this.value.setEditable(false);
        this.value.setBackground(Color.WHITE);
    }

    private void setReactions() {
        this.min.addFocusListener(focus);
        this.max.addFocusListener(focus);
        this.value.addFocusListener(focus_);
    }

    private void build() {
        this.setBackground(Color.WHITE);

        this.add(this.minLabel);
        this.add(this.min);
        this.add(this.maxLabel);
        this.add(this.max);
        this.add(this.value);
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
    public synchronized void addKeyListener(KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.min.setEnabled(enabled);
        this.max.setEnabled(enabled);
        this.value.setEnabled(enabled);
    }

    public void setRange(String min, String max, String value){
        this.min.setText(min);
        this.max.setText(max);
        this.value.setSelectedItem(value);
    }

    public void setRange(String min, String max){
        if (min != null) this.min.setText(min);
        if (max != null) this.max.setText(max);
    }

    public void updateMeasurementValue(String value){
        this.value.setSelectedItem(value);
    }

    public boolean isRangesMatch(){
        return rangesMatch.isSelected();
    }

    private final FocusListener focus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField)e.getSource();
            source.selectAll();
            parent.resetSpecialCharactersPanel();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();

            if (source.getText().length() == 0) {
                source.setText(DEFAULT_MIN_VALUE);
            }
            String forCheck = source.getText();
            source.setText(VariableConverter.doubleString(forCheck));

            if (source.equals(min)) {
                double minD = Double.parseDouble(min.getText());
                double maxD = Double.parseDouble(max.getText());
                if (maxD < minD) {
                    min.setText(String.valueOf(maxD));
                    max.setText(String.valueOf(minD));
                }
            }
        }
    };

    private final FocusListener focus_ = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            parent.resetSpecialCharactersPanel();
        }
    };

    public double getRangeMin(){
        return Double.parseDouble(this.min.getText());
    }
    public double getRangeMax(){
        return Double.parseDouble(this.max.getText());
    }
    public JTextField getRangeMinField(){return this.min;}
    public JTextField getRangeMaxField(){return this.max;}

    public String getValue(){
        return Objects.requireNonNull(this.value.getSelectedItem()).toString();
    }
}
