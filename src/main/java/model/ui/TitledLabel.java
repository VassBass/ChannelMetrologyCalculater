package model.ui;

import javax.swing.*;
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
}
