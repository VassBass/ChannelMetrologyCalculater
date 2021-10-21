package ui.calculate.start.complexElements;

import converters.VariableConverter;
import support.Channel;
import constants.Strings;
import ui.UI_Container;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class CalculateStartDialog_alarmPanel extends JPanel implements UI_Container {
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

    @Override
    public void createElements() {
        this.message = new JLabel(Strings.ALARM_MESSAGE);
        this.value = new JTextField("0.00",5);
        this.measurementValue = new JLabel(this.channel.getMeasurement().getValue());
    }

    @Override
    public void setReactions() {
        this.value.addFocusListener(focus);
    }

    @Override
    public void build() {
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
        if (this.value.getText().length()==0 || this.value.getText().equals("-")){
            this.value.setText("0.00");
        }else{
            String check = VariableConverter.doubleString(this.value.getText());
            double d = Double.parseDouble(check);
            this.value.setText(String.format(Locale.ENGLISH, "%.2f", d));
        }
    }
}
