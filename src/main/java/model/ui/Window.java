package model.ui;

import javax.swing.*;
import java.awt.*;

public class Window extends JWindow {

    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }
}
