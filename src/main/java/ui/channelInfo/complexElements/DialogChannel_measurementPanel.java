package ui.channelInfo.complexElements;

import model.Measurement;
import service.impl.MeasurementServiceImpl;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;

public class DialogChannel_measurementPanel extends JPanel {
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";

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

    private void createElements(){
        this.measurementName = new JComboBox<>(MeasurementServiceImpl.getInstance().getAllNames());
        this.measurementName.setEditable(false);
        this.measurementName.setBackground(Color.WHITE);

        this.measurementValue = new JComboBox<>(MeasurementServiceImpl.getInstance().getValues(Measurement.TEMPERATURE));
        this.measurementValue.setBackground(Color.WHITE);
        this.measurementValue.setEditable(false);
    }

    private void setReactions(){
        this.measurementName.addItemListener(this.changeName);
        this.measurementValue.addItemListener(this.changeValue);

        this.measurementValue.addFocusListener(this.changeFocusFromValue);
        this.measurementName.addFocusListener(focusOnName);
    }

    private void build(){
        this.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(TYPE_OF_MEASUREMENT);
        this.setBorder(border);
        this.add(this.measurementName);
        this.add(this.measurementValue);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
    }

    public void setSelectedValue(String value){
        this.measurementValue.setSelectedItem(value);
    }

    public void update(String measurementName){
        if (measurementName != null) {
            this.measurementName.setSelectedItem(measurementName);
            this.measurementValue.setModel(
                    new DefaultComboBoxModel<>(MeasurementServiceImpl.getInstance().getValues(measurementName)));
        }
    }

    public void setRosemountValues(){
        this.measurementValue.setModel(new DefaultComboBoxModel<>(rosemountValues()));
    }

    private String[] rosemountValues(){
        ArrayList<String> values  = new ArrayList<>();
        ArrayList<Measurement>measurements = new ArrayList<>(MeasurementServiceImpl.getInstance().getAll());
        for (Measurement measurement : measurements) {
            if (measurement.getName().equals(Measurement.CONSUMPTION)) {
                values.add(measurement.getValue());
            }
        }
        values.add(Measurement.M_S);
        values.add(Measurement.CM_S);
        return values.toArray(new String[0]);
    }

    public Measurement getMeasurement(){
        if (measurementValue.getSelectedItem() == null) return null;

        return MeasurementServiceImpl.getInstance().get(measurementValue.getSelectedItem().toString()).get();
    }

    @SuppressWarnings("unchecked")
    private final ItemListener changeName = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox<String> item = (JComboBox<String>) e.getSource();

                String measurementNameStr = Objects.requireNonNull(item.getSelectedItem()).toString();
                currentPanel.update(measurementNameStr);
                String measurementVal = Objects.requireNonNull(measurementValue.getSelectedItem()).toString();
                parent.allowableErrorPanel.updateValue(measurementVal);
                parent.rangePanel.updateValue(measurementVal);
                parent.sensorPanel.update(measurementNameStr);

                Measurement measurement = new Measurement(measurementNameStr, measurementVal);
                parent.update(measurement);
                measurementName.requestFocus();
            }
        }
    };

    @SuppressWarnings("unchecked")
    private final ItemListener changeValue = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox<String> item = (JComboBox<String>) e.getSource();
                String measurementVal = Objects.requireNonNull(item.getSelectedItem()).toString();

                parent.allowableErrorPanel.updateValue(measurementVal);
                parent.rangePanel.updateValue(measurementVal);
                if (parent.rangeLikeChannel.isSelected()){
                    parent.sensorRangePanel.setValue(measurementVal);
                }
            }
        }
    };

    private final FocusListener changeFocusFromValue = new FocusListener() {
        @Override public void focusGained(FocusEvent e) {
            parent.resetSpecialCharactersPanel();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (measurementValue.getSelectedItem() != null) {
                String measurementVal = measurementValue.getSelectedItem().toString();

                parent.allowableErrorPanel.updateValue(measurementVal);
                parent.rangePanel.updateValue(measurementVal);
            }
        }
    };

    private final FocusListener focusOnName = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            parent.resetSpecialCharactersPanel();
        }
    };
}