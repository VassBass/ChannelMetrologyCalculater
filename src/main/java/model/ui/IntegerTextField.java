package model.ui;

import util.StringHelper;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class IntegerTextField extends DefaultTextField {
    private int defaultValue = 0;

    public IntegerTextField(int columns) {
        super(columns);
        this.addFocusListener(focusListener);
    }

    public IntegerTextField(int columns, String tooltipText) {
        super(columns, tooltipText);
        this.addFocusListener(focusListener);
    }

    public IntegerTextField(int columns, int horizontalAlignment) {
        super(columns, horizontalAlignment);
        this.addFocusListener(focusListener);
    }

    public IntegerTextField(int columns, int defaultValue, int horizontalAlignment) {
        super(columns, horizontalAlignment);
        this.defaultValue = defaultValue;
        this.setText(String.valueOf(defaultValue));
        this.addFocusListener(focusListener);
    }

    public IntegerTextField(int columns, String tooltipText, int horizontalAlignment) {
        super(columns, tooltipText, horizontalAlignment);
        this.addFocusListener(focusListener);
    }

    public IntegerTextField(int columns, int defaultValue, String tooltipText) {
        super(columns, String.valueOf(defaultValue), tooltipText);
        this.defaultValue = defaultValue;
        this.addFocusListener(focusListener);
    }

    @Override
    public void setText(String t) {
        if (StringHelper.isInt(t)) {
            super.setText(t);
        } else if (StringHelper.isDouble(t)) {
            super.setText(StringHelper.roundingDouble(Double.parseDouble(t), 0));
        } else {
            super.setText(String.valueOf(defaultValue));
        }
    }

    public int getInput() {
        this.setText(this.getText());
        return Integer.parseInt(this.getText());
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setText(int t) {
        super.setText(String.valueOf(t));
    }

    private final FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.setText(source.getText());
        }
    };
}
