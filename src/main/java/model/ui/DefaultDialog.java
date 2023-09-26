package model.ui;

import javax.swing.*;
import java.awt.*;

public abstract class DefaultDialog extends JDialog {

    public DefaultDialog(MainScreen owner, String title) {
        super(owner, title, true);
    }

    public DefaultDialog(DefaultDialog owner, String title) {
        super(owner, title, true);
    }

    public void refresh() {
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }

    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }

    public void shutdown() {
        this.dispose();
    }

    public boolean isVisible() {
        return super.isVisible();
    }
}
