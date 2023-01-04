package ui.model;

import javax.swing.*;

public class CheckBox extends JCheckBox {

    public CheckBox(String title) {
        super(title);

        this.setFocusPainted(false);
    }
}
