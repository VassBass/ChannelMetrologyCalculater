package ui.channelInfo.complexElements;

import converters.VariableConverter;
import measurements.Measurement;
import sensors.Sensor;
import support.Lists;
import ui.UI_Container;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class DialogChannel_sensorRangePanel extends JPanel implements UI_Container {
    private JLabel minLabel;
    private JLabel maxLabel;

    private JTextField min;
    private JTextField max;

    private JComboBox<String>value;

    private final Measurement measurement;

    public DialogChannel_sensorRangePanel(Measurement measurement){
        super();
        this.measurement = measurement;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.minLabel = new JLabel("Від ");
        this.maxLabel = new JLabel(" до ");

        this.min = new JTextField("0.00", 5);
        this.max = new JTextField("100.00", 5);

        ArrayList<String> items = new ArrayList<>();
        for (int x = 0; x< Objects.requireNonNull(Lists.measurements()).size(); x++){
            if (Objects.requireNonNull(Lists.measurements()).get(x).getName().equals(measurement.getName())){
                items.add(Objects.requireNonNull(Lists.measurements()).get(x).getValue());
            }
        }
        this.value = new JComboBox<>(items.toArray(new String[0]));
        this.value.setEditable(false);
        this.value.setBackground(Color.white);
    }

    @Override
    public void setReactions() {
        this.min.addFocusListener(focus);
        this.max.addFocusListener(focus);
    }

    @Override
    public void build() {
        this.add(this.minLabel);
        this.add(this.min);
        this.add(this.maxLabel);
        this.add(this.max);
        this.add(this.value);
    }

    public void update(Sensor sensor) {
        if (sensor != null) {
            this.min.setText(VariableConverter.roundingDouble2(sensor.getRangeMin(), Locale.ENGLISH));
            this.max.setText(VariableConverter.roundingDouble2(sensor.getRangeMax(), Locale.ENGLISH));
            this.value.setSelectedItem(sensor.getValue());
        }
    }

    private final FocusListener focus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField)e.getSource();
            source.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();

            if (source.getText().length() == 0) {
                source.setText("0.00");
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

    public double getRangeMin(){
        return Double.parseDouble(this.min.getText());
    }

    public double getRangeMax(){
        return Double.parseDouble(this.max.getText());
    }

    public String getValue(){
        return Objects.requireNonNull(this.value.getSelectedItem()).toString();
    }
}
