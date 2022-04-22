package ui.channelInfo.complexElements;

import converters.ErrorConverter;
import converters.VariableConverter;
import model.Measurement;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Locale;

public class DialogChannel_allowableErrorPanel extends JPanel {
    private static final String PERCENT = "% ";
    private static final String AND = " та ";
    private static final String DEFAULT_VALUE = "1.5";
    private static final String ALLOWABLE_ERROR_OF_CHANNEL = "Допустима похибка вимірювального каналу";

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

    private void createElements() {
        this.labelPercent = new JLabel(PERCENT);
        this.and = new JLabel(AND);
        this.value = new JLabel(Measurement.DEGREE_CELSIUS);

        this.errorPercent = new JTextField(DEFAULT_VALUE, 5);
        this.errorValue = new JTextField(DEFAULT_VALUE, 5);
    }

    private void setReactions() {
        this.errorPercent.addFocusListener(this.focus);
        this.errorValue.addFocusListener(this.focus);
    }

    private void build() {
        this.add(this.errorPercent);
        this.add(this.labelPercent);
        this.add(this.and);
        this.add(this.errorValue);
        this.add(this.value);
        TitledBorder border = BorderFactory.createTitledBorder(ALLOWABLE_ERROR_OF_CHANNEL);
        this.setBorder(border);
    }

    public void updateRange(double range) {
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

    public void updateValue(String measurementValue){
        if (measurementValue != null){
            this.value.setText(measurementValue);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.errorPercent.setEnabled(enabled);
        this.errorValue.setEnabled(enabled);
    }

    public void updateError(double error, boolean inPercent, double range){
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
                item.setText(DEFAULT_VALUE);
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