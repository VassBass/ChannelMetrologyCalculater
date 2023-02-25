package service.application;

import model.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ApplicationScreen extends JFrame implements UI {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationScreen.class);

    private static final String TITLE = "ChannelMetrologyCalculater";
    private static ApplicationConfigHolder configHolder;
    private final JMenuBar menuBar;

    private static volatile ApplicationScreen instance;
    public static ApplicationScreen getInstance() {
        if (configHolder == null) {
            String message = "Before getting the instance, you need to execute the static init() method to initialize the screen";
            logger.warn(message);
            return null;
        }

        if (instance == null) {
            synchronized (ApplicationScreen.class) {
                if (instance == null) instance = new ApplicationScreen();
            }
        }
        return instance;
    }

    public static void init(ApplicationConfigHolder config) {
        configHolder = config;
    }

    private ApplicationScreen() {
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
    public void showing() {
        EventQueue.invokeLater(() ->
                ApplicationScreen.this.setVisible(true));
    }

    @Override
    public void hiding() {
        EventQueue.invokeLater(() ->
                ApplicationScreen.this.setVisible(false));
    }

    @Override
    public void shutdown() {
        System.exit(0);
    }
}
