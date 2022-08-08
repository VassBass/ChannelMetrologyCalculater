package ui.calculate.start.complexElements;

import converters.VariableConverter;
import model.Channel;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class CalculateStartDialog_alarmPanel extends JPanel {
    private static final String ALARM_MESSAGE = "Сигналізація спрацювала при t = ";
    private static final String DEFAULT_ALARM_VALUE = "0.00";
    private static final String DASH = "-";

    private final Channel channel;

    private JLabel message;
    private JLabel measurementValue;

    private JTextField value;

    public CalculateStartDialog_alarmPanel(Channel channel){
        super();
        this.channel = channel;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.message = new JLabel(ALARM_MESSAGE);
        this.value = new JTextField(DEFAULT_ALARM_VALUE,5);
        this.measurementValue = new JLabel(this.channel.getMeasurementValue());
    }

    private void setReactions() {
        this.value.addFocusListener(this.focus);
    }

    private void build() {
        this.value.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.message);
        this.add(this.value);
        this.add(this.measurementValue);
    }

    public void setValue(String value){
        this.value.setText(value);
    }

    public String getValue(){
        return this.value.getText();
    }

    private final FocusListener focus = new FocusListener(){
        @Override
        public void focusGained(FocusEvent e){
            value.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e){
            checkString();
        }
    };

    private void checkString(){
        if (this.value.getText().length()==0 || this.value.getText().contains(DASH)){
            this.value.setText(DEFAULT_ALARM_VALUE);
        }else{
            String check = VariableConverter.doubleString(this.value.getText());
            double d = Double.parseDouble(check);
            this.value.setText(String.format(Locale.ENGLISH, "%.2f", d));
        }
    }
}