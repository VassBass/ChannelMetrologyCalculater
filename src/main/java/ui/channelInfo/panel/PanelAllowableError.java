package ui.channelInfo.panel;

import converters.ErrorConverter;
import converters.VariableConverter;
import ui.channelInfo.DialogChannel;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Locale;

public class PanelAllowableError extends JPanel {
    private static final String PERCENT = "% ";
    private static final String AND = " та ";
    private static final String DEFAULT_VALUE = "1.5";
    private static final String ALLOWABLE_ERROR_OF_CHANNEL = "Допустима похибка вимірювального каналу";

    private final DialogChannel parent;

    private final JLabel value;

    private final JTextField percentValue;
    private final JTextField numberValue;

    public PanelAllowableError(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        percentValue = new PercentValueTextField();
        numberValue = new NumberValueTextField();
        value = new JLabel();

        this.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(ALLOWABLE_ERROR_OF_CHANNEL);
        border.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(border);

        this.add(percentValue, new Cell(0,0));
        this.add(new JLabel(PERCENT), new Cell(1,0));
        this.add(new JLabel(AND), new Cell(2,0));
        this.add(numberValue, new Cell(3,0));
        this.add(value, new Cell(4,0));
    }

    public void updateChannelRange(@Nonnegative double range) {
        double errorPercent = Double.parseDouble(percentValue.getText());
        double error = ErrorConverter.getErrorFrom(errorPercent, true, range);
        String errorString;
        if (range < 10D){
            errorString = String.format(Locale.ENGLISH,"%.3f", error);
        }else {
            errorString = String.format(Locale.ENGLISH, "%.2f", error);
        }
        this.numberValue.setText(errorString);
    }

    public void updateMeasurementValue(@Nonnull String measurementValue){
        this.value.setText(measurementValue);
    }

    @Override
    public void setEnabled(boolean enabled) {
        percentValue.setEnabled(enabled);
        numberValue.setEnabled(enabled);
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        for (Component component : this.getComponents()){
            if (component != null) component.addKeyListener(l);
        }
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
        percentValue.setText(errorPercent);
        numberValue.setText(errorValue);
    }

    public void updateError(){
        double range = parent.panelChannelRange.getRange();
        double errorPercent = Double.parseDouble(percentValue.getText());
        String errorValue;
        if (range < 10D){
            errorValue = String.format(Locale.ENGLISH, "%.3f", ErrorConverter.getErrorFrom(errorPercent, true, range));
        }else {
            errorValue = String.format(Locale.ENGLISH, "%.2f", ErrorConverter.getErrorFrom(errorPercent, true, range));
        }
        numberValue.setText(errorValue);
    }

    public double getAllowableErrorNumber(){
        return Double.parseDouble(numberValue.getText());
    }
    public double getAllowableErrorPercent(){
        return Double.parseDouble(percentValue.getText());
    }

    private class PercentValueTextField extends JTextField {

        private PercentValueTextField(){
            super(DEFAULT_VALUE, 5);
            this.setHorizontalAlignment(JTextField.CENTER);

            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                percentValue.selectAll();
                parent.panelSpecialCharacters.setFieldForInsert(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                double range = parent.panelChannelRange.getRange();

                if (percentValue.getText().length() > 0) {
                    String text = VariableConverter.doubleString(percentValue.getText());
                    percentValue.setText(text);
                }else {
                    percentValue.setText(DEFAULT_VALUE);
                }

                String errorString;
                double errorPercent = Double.parseDouble(percentValue.getText());
                double error = ErrorConverter.getErrorFrom(errorPercent, true, range);
                if (range < 10D) {
                    errorString = String.format(Locale.ENGLISH, "%.3f", error);
                } else {
                    errorString = String.format(Locale.ENGLISH, "%.2f", error);
                }
                numberValue.setText(errorString);
            }
        };
    }

    private class NumberValueTextField extends JTextField {

        private NumberValueTextField(){
            super(DEFAULT_VALUE, 5);
            this.setHorizontalAlignment(JTextField.CENTER);

            this.addFocusListener(focus);
        }

        @SuppressWarnings("FieldCanBeLocal")
        private final FocusListener focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                numberValue.selectAll();
                parent.panelSpecialCharacters.setFieldForInsert(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                double range = parent.panelChannelRange.getRange();

                if (numberValue.getText().length() > 0) {
                    String text = VariableConverter.doubleString(numberValue.getText());
                    numberValue.setText(text);
                }else {
                    numberValue.setText(DEFAULT_VALUE);
                }

                String errorString;
                double error = Double.parseDouble(numberValue.getText());
                double errorPercentD = ErrorConverter.getErrorFrom(error, false, range);
                errorString = String.format(Locale.ENGLISH, "%.2f", errorPercentD);
                percentValue.setText(errorString);
            }
        };
    }

    private static class Cell extends GridBagConstraints {
        private Cell(int x, int y){
            super();
            this.fill = BOTH;
            this.weighty = 1D;

            this.gridx = x;
            this.gridy = y;
        }
    }
}
