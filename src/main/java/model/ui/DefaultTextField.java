package model.ui;

import javax.swing.*;
import java.awt.*;

public class DefaultTextField extends JTextField {

    public DefaultTextField(int columns) {
        super(columns);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
    }

    public DefaultTextField(int columns, String tooltipText) {
        this(columns);
        this.setToolTipText(tooltipText);
    }

    public DefaultTextField(int columns, String text, String tooltipText) {
        this(columns,tooltipText);
        this.setText(text);
    }
}
