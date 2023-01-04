package ui.model;

import javax.swing.*;
import java.awt.*;

public class StringsList extends JComboBox<String> {
    public static final int CENTER = 0;

    public StringsList(String ... list) {
        super(list);

        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
    }

    public StringsList(boolean editable, String ... list) {
        super(list);

        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
        this.setEditable(editable);
    }

    @Override
    public void setEditable(boolean aFlag) {
        super.setEditable(aFlag);
        this.setFocusable(aFlag);
    }

    public void setHorizontalAlignment(int alignment) {
        ((JLabel)this.getRenderer()).setHorizontalAlignment(alignment);
    }
}
