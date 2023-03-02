package model.ui;

import javax.swing.*;
import java.awt.*;

public class TitledPanel extends DefaultPanel {

    public TitledPanel(String title) {
        super();
        this.setBackground(Color.WHITE);

        this.setBorder(BorderFactory.createTitledBorder(title));
    }
}
