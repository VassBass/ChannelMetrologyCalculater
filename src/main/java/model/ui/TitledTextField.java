package model.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class TitledTextField extends JTextField {
    public static final int BORDER_TOP_CENTER = TitledBorder.CENTER;
    public static final int TEXT_CENTER = SwingConstants.CENTER;

    public TitledTextField(int columns) {
        super(columns);
    }

    public TitledTextField(String title, int columns) {
        super(columns);
        TitledBorder border = BorderFactory.createTitledBorder(title);
        this.setBorder(border);
    }
}
