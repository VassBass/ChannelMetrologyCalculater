package model.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TitledTextArea extends JTextArea {
    public static final int TITLE_TOP_CENTER = TitledBorder.CENTER;
    public static final int TEXT_CENTER = SwingConstants.CENTER;

    private TitledBorder border;

    public TitledTextArea(int rows, int columns) {
        super(rows, columns);
    }

    public TitledTextArea(int rows, int columns, String title) {
        super(rows, columns);
        border = BorderFactory.createTitledBorder(title);
        this.setBorder(border);
    }

    public TitledTextArea(int rows, int columns, String title, Color borderColor) {
        super(rows, columns);
        border = new TitledBorder(new LineBorder(borderColor), title);
        this.setBorder(border);
    }

    public void setTitleColor(Color color) {
        if (border != null) border.setTitleColor(color);
    }
}
