package model.ui;

import javax.swing.*;
import java.awt.*;

public class DefaultLabel extends JLabel {

    public DefaultLabel(String text) {
        super(text);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
    }

    public DefaultLabel(String text, String tooltipText) {
        this(text);
        this.setToolTipText(tooltipText);
    }
}
