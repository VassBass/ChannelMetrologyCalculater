package model.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TitledComboBox extends DefaultComboBox {
    private final TitledBorder border;

    public TitledComboBox(boolean editable, String title) {
        super(editable);
        border = BorderFactory.createTitledBorder(title);
        this.setBorder(border);
    }

    public TitledComboBox(boolean editable, String title, Color titleColor) {
        super(editable);
        border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(titleColor);
        this.setBorder(border);
    }

    public void setTitleColor(Color color) {
        border.setTitleColor(color);
    }
}
