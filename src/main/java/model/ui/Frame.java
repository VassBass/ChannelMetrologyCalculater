package model.ui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }
}
