package ui.model;

import javax.swing.*;

public class CheckBox extends JCheckBox {

    public CheckBox(String title) {
        super(title);

        this.setFocusPainted(false);
    }

    public CheckBox(String title, int textHorizontalAlignment) {
        super(title);
        setHorizontalAlignment(textHorizontalAlignment);

        this.setFocusPainted(false);
    }
}
