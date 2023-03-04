package model.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TitledTextField extends JTextField {
    public static final int TITLE_TOP_CENTER = TitledBorder.CENTER;
    public static final int TEXT_CENTER = SwingConstants.CENTER;

    private TitledBorder border;

    public TitledTextField(int columns) {
        super(columns);
    }

    public TitledTextField(int columns, String title) {
        super(columns);
        border = BorderFactory.createTitledBorder(title);
        this.setBorder(border);
    }

    public TitledTextField(int columns, String title, Color borderColor) {
        super(columns);
        border = new TitledBorder(new LineBorder(borderColor), title);
        this.setBorder(border);
    }

    public void setTitleColor(Color color) {
        if (border != null) border.setTitleColor(color);
    }
}
