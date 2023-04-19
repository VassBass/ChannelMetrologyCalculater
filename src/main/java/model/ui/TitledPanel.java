package model.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TitledPanel extends DefaultPanel {

    private final TitledBorder border;

    public TitledPanel(String title) {
        super();
        this.setBackground(Color.WHITE);

        border = BorderFactory.createTitledBorder(title);
        this.setBorder(border);
    }

    public TitledPanel(String title, Color borderColor) {
        super();
        this.setBackground(Color.WHITE);

        border = new TitledBorder(new LineBorder(borderColor), title);
        this.setBorder(border);
    }

    public void setTitleColor(Color color) {
        if (border != null) border.setTitleColor(color);
    }
}
