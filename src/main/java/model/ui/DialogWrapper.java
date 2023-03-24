package model.ui;

import javax.swing.*;
import java.awt.*;

public class DialogWrapper extends JDialog implements UI {

    public DialogWrapper(Window owner, JDialog dialog, Point location) {
        super(owner, ModalityType.APPLICATION_MODAL);
        clone(dialog);
        this.setLocation(location);
    }

    private void clone(JDialog dialog) {
        this.setContentPane(dialog.getContentPane());
        this.setSize(dialog.getSize());
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);
    }

    @Override
    public void refresh() {
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }

    @Override
    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }

    @Override
    public void shutdown() {
        this.dispose();
    }
}
