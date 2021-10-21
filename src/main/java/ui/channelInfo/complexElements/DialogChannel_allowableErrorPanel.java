package ui.channelInfo.complexElements;

import constants.MeasurementConstants;
import converters.ErrorConverter;
import converters.VariableConverter;
import ui.UI_Container;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class DialogChannel_allowableErrorPanel extends JPanel implements UI_Container {
    private final DialogChannel parent;

    private JLabel labelPercent;
    private JLabel and;
    private JLabel value;

    private JTextField errorPercent;
    private JTextField errorValue;

    public DialogChannel_allowableErrorPanel(DialogChannel parent){
        super();
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.labelPercent = new JLabel("% ");
        this.and = new JLabel(" та ");
        this.value = new JLabel(MeasurementConstants.DEGREE_CELSIUS.getValue());

        this.errorPercent = new JTextField("0.00", 5);
        this.errorValue = new JTextField("0.00", 5);
    }

    @Override
    public void setReactions() {
        this.errorPercent.addFocusListener(focus);
        this.errorValue.addFocusListener(focus);
    }

    @Override
    public void build() {
        this.add(errorPercent);
        this.add(labelPercent);
        this.add(and);
        this.add(errorValue);
        this.add(value);
    }

    public void update(double range) {
        double errorPercent = Double.parseDouble(this.errorPercent.getText());
        double error = ErrorConverter.getErrorFrom(errorPercent, true, range);
        String errorString;
        if (range < 10D){
            errorString = String.format(Locale.ENGLISH,"%.3f", error);
        }else {
            errorString = String.format(Locale.ENGLISH, "%.2f", error);
        }
        this.errorValue.setText(errorString);
    }

    public void update(String measurementValue){
        if (measurementValue != null){
            this.value.setText(measurementValue);
        }
    }

    public void update(double error, boolean inPercent, double range){
        String errorPercent, errorValue;
        if (inPercent){
            errorPercent = String.format(Locale.ENGLISH, "%.2f", error);
            if (range < 10D){
                errorValue = String.format(Locale.ENGLISH, "%.3f", ErrorConverter.getErrorFrom(error, true, range));
            }else {
                errorValue = String.format(Locale.ENGLISH, "%.2f", ErrorConverter.getErrorFrom(error, true, range));
            }
        }else {
            if (range < 10D){
                errorValue = String.format(Locale.ENGLISH, "%.3f", error);
            }else {
                errorValue = String.format(Locale.ENGLISH, "%.2f", error);
            }
            errorPercent = String.format(Locale.ENGLISH, "%.2f", ErrorConverter.getErrorFrom(error, false, range));
        }
        this.errorPercent.setText(errorPercent);
        this.errorValue.setText(errorValue);
    }

    private final FocusListener focus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField item = (JTextField) e.getSource();
            item.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField item = (JTextField) e.getSource();
            double range = parent.rangePanel.getRange();
            if (item.getText().length() > 0) {
                String text = VariableConverter.doubleString(item.getText());
                item.setText(text);
            }else {
                item.setText("0.00");
            }
            String errorString;
            if (item.equals(errorPercent)) {
                double errorPercent = Double.parseDouble(item.getText());
                double error = ErrorConverter.getErrorFrom(errorPercent, true, range);
                if (range < 10D) {
                    errorString = String.format(Locale.ENGLISH, "%.3f", error);
                } else {
                    errorString = String.format(Locale.ENGLISH, "%.2f", error);
                }
                errorValue.setText(errorString);
            }else if (item.equals((errorValue))){
                double error = Double.parseDouble(item.getText());
                double errorPercentD = ErrorConverter.getErrorFrom(error, false, range);
                errorString = String.format(Locale.ENGLISH, "%.2f", errorPercentD);
                errorPercent.setText(errorString);
            }
        }
    };

    public double getAllowableErrorValue(){
        return Double.parseDouble(this.errorValue.getText());
    }

    public double getAllowableErrorPercent(){
        return Double.parseDouble(this.errorPercent.getText());
    }

}
