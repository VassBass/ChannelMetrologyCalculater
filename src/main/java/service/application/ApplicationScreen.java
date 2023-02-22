package service.application;

import model.ui.Frame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ApplicationScreen extends Frame {
    private static volatile ApplicationScreen instance;
    public static ApplicationScreen getInstance() {
        if (instance == null) {
            synchronized (ApplicationScreen.class) {
                if (instance == null) instance = new ApplicationScreen();
            }
        }
        return instance;
    }

    private static final String TITLE = "ChannelMetrologyCalculater";

    private ApplicationScreen() {
        super();
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String message = "Закрити програму?";
                int result = JOptionPane.showConfirmDialog(ApplicationScreen.this,
                        TITLE,
                        message,
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) System.exit(0);
            }
        });
    }

}
