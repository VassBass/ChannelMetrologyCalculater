package model.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TitledLabel extends JLabel {

    public TitledLabel(String title) {
        super();
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
    }

    public TitledLabel(String title, String text) {
        this(title);
        this.setText(text);
    }

    public TitledLabel(String title, Color borderColor) {
        super();
        this.setBorder(new TitledBorder(new LineBorder(borderColor), title));
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
    }

    public TitledLabel(String title, String text, Color borderColor) {
        this(title, borderColor);
        this.setText(text);
    }
}
