package model.ui;

import javax.swing.*;
import java.awt.*;

public class DefaultLabel extends JLabel {

    public DefaultLabel() {
        super();
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
    }

    public DefaultLabel(String text) {
        this();
        this.setText(text);

    }

    public DefaultLabel(String text, String tooltipText) {
        this(text);
        this.setToolTipText(tooltipText);
    }

    public DefaultLabel(String text, int horizontalAlignment) {
        this(text);
        this.setHorizontalAlignment(horizontalAlignment);
    }
}
