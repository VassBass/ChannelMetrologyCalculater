package ui.channelInfo.complexElements;

import application.Application;
import constants.MeasurementConstants;
import measurements.Measurement;
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
public class DialogChannel_measurementPanel extends JPanel {

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

    public void createElements(){
        this.measurementName = new JComboBox<>(this.measurementNames());
        this.measurementName.setEditable(false);
        this.measurementName.setBackground(Color.white);

        this.measurementValue = new JComboBox<>(this.measurementValues(MeasurementConstants.TEMPERATURE.getValue()));
        this.measurementValue.setBackground(Color.white);
        this.measurementValue.setEditable(false);
    }

    public void setReactions(){
        this.measurementName.addItemListener(changeName);
        this.measurementValue.addItemListener(changeValue);

        this.measurementValue.addFocusListener(changeFocusFromValue);
    }

    public void build(){
        this.add(this.measurementName);
        this.add(this.measurementValue);
    }

    public void setSelectedValue(String value){
        this.measurementValue.setSelectedItem(value);
    }

    public void update(String measurementName){
        if (measurementName != null) {
            this.measurementName.setSelectedItem(measurementName);
            this.measurementValue.setModel(new DefaultComboBoxModel<>(measurementValues(measurementName)));
        }
    }

    public void setRosemountValues(){
        this.measurementValue.setModel(new DefaultComboBoxModel<>(rosemountValues()));
    }

    private String[] rosemountValues(){
        ArrayList<String> values  = new ArrayList<>();
        ArrayList<Measurement>measurements = Application.context.measurementsController.getAll();
        for (Measurement measurement : measurements) {
            if (measurement.getNameConstant() == MeasurementConstants.CONSUMPTION) {
                values.add(measurement.getValue());
            }
        }
        values.add(MeasurementConstants.M_S.getValue());
        values.add(MeasurementConstants.CM_S.getValue());
        return values.toArray(new String[0]);
    }

    private String[] measurementNames(){
        return Application.context.measurementsController.getAllNames();
    }

    private String[]measurementValues(String nameOfMeasurement){
        return Application.context.measurementsController.getValues(nameOfMeasurement);
    }

    public Measurement getMeasurement(){
        return new Measurement(
                Objects.requireNonNull(this.measurementName.getSelectedItem()).toString(),
                Objects.requireNonNull(this.measurementValue.getSelectedItem()).toString());
    }

    private final ItemListener changeName = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox<String> item = (JComboBox<String>) e.getSource();

                String measurementName = Objects.requireNonNull(item.getSelectedItem()).toString();
                currentPanel.update(measurementName);
                String measurementVal = Objects.requireNonNull(measurementValue.getSelectedItem()).toString();
                parent.allowableErrorPanel.updateValue(measurementVal);
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

                parent.allowableErrorPanel.updateValue(measurementVal);
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

            parent.allowableErrorPanel.updateValue(measurementVal);
            parent.rangePanel.update(measurementVal);
        }
    };
}
