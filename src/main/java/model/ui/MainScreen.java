package model.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import localization.Labels;

public abstract class MainScreen extends JFrame {

    private static final String CLOSE_TITLE = Labels.getRootLabels().get("close");
    private static final String CLOSE_MESSAGE = Labels.getLabels(MainScreen.class).get("closeApp?");

    private DefaultDialog activeDialog;

    protected MainScreen() {
        super();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);
    }

    private final WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            int result = JOptionPane.showConfirmDialog(MainScreen.this,
                    CLOSE_MESSAGE,
                    CLOSE_TITLE,
                    JOptionPane.YES_NO_OPTION);
            if (result == 0) shutdown();
        }
    };

    public void setActiveDialog(DefaultDialog activeDialog) {
        this.activeDialog = activeDialog;
    }

    public DefaultDialog getActiveDialog() {
        return activeDialog;
    }

    public void refresh() {
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }

    public void showing() {
        EventQueue.invokeLater(() ->
                this.setVisible(true));
    }

    public void hiding() {
        EventQueue.invokeLater(() ->
                this.setVisible(false));
    }

    public void shutdown() {
        System.exit(0);
    }
}
