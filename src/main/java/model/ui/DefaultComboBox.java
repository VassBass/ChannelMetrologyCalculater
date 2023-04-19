package model.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class DefaultComboBox extends JComboBox<String> {

    public DefaultComboBox(boolean editable) {
        super();
        this.setEditable(editable);
        this.setBackground(Color.WHITE);
    }

    public void setList(List<String> list) {
        this.setModel(new DefaultComboBoxModel<>(list.toArray(new String[0])));
    }

    public String getSelectedString() {
        Object selected = super.getSelectedItem();
        return selected == null ? EMPTY : selected.toString();
    }
}
