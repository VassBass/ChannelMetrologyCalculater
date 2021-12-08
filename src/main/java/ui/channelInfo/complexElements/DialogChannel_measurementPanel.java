package ui.channelInfo.complexElements;

import constants.MeasurementConstants;
import measurements.Measurement;
import support.Lists;
import ui.UI_Container;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class DialogChannel_measurementPanel extends JPanel implements UI_Container {

    private final DialogChannel parent;
    private final DialogChannel_measurementPanel currentPanel;

    private JComboBox<String> measurementName;
    private JComboBox<String> measurementValue;

    public DialogChannel_measurementPanel(DialogChannel parent){
        super();
        this.parent = parent;
        this.currentPanel = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements(){
        this.measurementName = new JComboBox<>(this.measurementNames());
        this.measurementName.setEditable(false);
        this.measurementName.setBackground(Color.white);

        this.measurementValue = new JComboBox<>(this.measurementValues(MeasurementConstants.TEMPERATURE.getValue()));
        this.measurementValue.setBackground(Color.white);
        this.measurementValue.setEditable(false);
    }

    @Override
    public void setReactions(){
        this.measurementName.addItemListener(changeName);
        this.measurementValue.addItemListener(changeValue);

        this.measurementValue.addFocusListener(changeFocusFromValue);
    }

    @Override
    public void build(){
        this.add(this.measurementName);
        this.add(this.measurementValue);
    }

    public void update(String measurementName){
        if (measurementName != null) {
            this.measurementName.setSelectedItem(measurementName);
            this.measurementValue.setModel(new DefaultComboBoxModel<>(measurementValues(measurementName)));
        }
    }

    private String[] measurementNames(){
        ArrayList<String> measurements = new ArrayList<>();
        for (int x = 0; x< Objects.requireNonNull(Lists.measurements()).size(); x++) {
            String name = Objects.requireNonNull(Lists.measurements()).get(x).getName();
            if (measurements.size()>0) {
                boolean exist = false;
                for (String measurement : measurements) {
                    if (measurement.equals(name)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist){
                    measurements.add(name);
                }
            }else {
                measurements.add(name);
            }
        }
        return measurements.toArray(new String[0]);
    }

    private String[]measurementValues(String nameOfMeasurement){
        ArrayList<String> values  = new ArrayList<>();
        for (int x = 0; x< Objects.requireNonNull(Lists.measurements()).size(); x++){
            if (Objects.requireNonNull(Lists.measurements()).get(x).getName().equals(nameOfMeasurement)){
                values.add(Objects.requireNonNull(Lists.measurements()).get(x).getValue());
            }
        }
        return values.toArray(new String[0]);
    }

    public Measurement getMeasurement(){
        return new Measurement(MeasurementConstants.getConstantFromString(Objects.requireNonNull(this.measurementName.getSelectedItem()).toString()),
                MeasurementConstants.getConstantFromString(Objects.requireNonNull(this.measurementValue.getSelectedItem()).toString()));
    }

    private final ItemListener changeName = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox<String> item = (JComboBox<String>) e.getSource();

                String measurementName = Objects.requireNonNull(item.getSelectedItem()).toString();
                currentPanel.update(measurementName);
                String measurementVal = Objects.requireNonNull(measurementValue.getSelectedItem()).toString();
                parent.allowableErrorPanel.update(measurementVal);
                parent.rangePanel.update(measurementVal);
                parent.sensorPanel.update(MeasurementConstants.getConstantFromString(measurementName));

                Measurement measurement = new Measurement(MeasurementConstants.getConstantFromString(measurementName),
                        MeasurementConstants.getConstantFromString(measurementVal));
                parent.update(measurement);
            }
        }
    };

    private final ItemListener changeValue = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox<String> item = (JComboBox<String>) e.getSource();
                String measurementVal = Objects.requireNonNull(item.getSelectedItem()).toString();

                parent.allowableErrorPanel.update(measurementVal);
                parent.rangePanel.update(measurementVal);
            }
        }
    };

    private final FocusListener changeFocusFromValue = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {}

        @Override
        public void focusLost(FocusEvent e) {
            JComboBox<String> item = (JComboBox<String>) e.getSource();
            String measurementVal = Objects.requireNonNull(item.getSelectedItem()).toString();

            parent.allowableErrorPanel.update(measurementVal);
            parent.rangePanel.update(measurementVal);
        }
    };
}
