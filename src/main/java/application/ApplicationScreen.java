package application;

import model.ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ApplicationScreen extends JFrame implements UI {
    private static final String TITLE = "ChannelMetrologyCalculater";
    private final JMenuBar menuBar;

    public ApplicationScreen(ApplicationConfigHolder configHolder) {
        super();
        this.menuBar = new JMenuBar();
        
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);
        this.setSize(configHolder.getScreenWidth(), configHolder.getScreenHeight());
        this.setJMenuBar(menuBar);
    }

    private final WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            String message = "Закрити програму?";
            int result = JOptionPane.showConfirmDialog(ApplicationScreen.this,
                    message,
                    TITLE,
                    JOptionPane.YES_NO_OPTION);
            if (result == 0) shutdown();
        }
    };

    public void addMenu(JMenu menu) {
        menuBar.add(menu);
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
        EventQueue.invokeLater(() ->
                this.setVisible(true));
    }

    @Override
    public void hiding() {
        EventQueue.invokeLater(() ->
                this.setVisible(false));
    }

    @Override
    public void shutdown() {
        System.exit(0);
    }
}
