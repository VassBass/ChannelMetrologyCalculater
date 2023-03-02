package model.ui;

import javax.swing.*;
import java.awt.*;

public class DefaultComboBox extends JComboBox<String> {

    public DefaultComboBox(boolean editable) {
        super();
        this.setEditable(editable);
        this.setBackground(Color.WHITE);
    }
}
