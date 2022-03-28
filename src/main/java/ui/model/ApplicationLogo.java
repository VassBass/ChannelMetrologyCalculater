package ui.model;

import application.Application;
import converters.ConverterUI;
import service.FileBrowser;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ApplicationLogo extends JWindow {
    private static final Logger LOGGER = Logger.getLogger(ApplicationLogo.class.getName());

    private static final String LOADING = "Завантаження...";
    private static final String DEVELOPER = "VassBassApp";
    private static final String VERSION = Application.appVersion;

    private final JLabel message;

    public ApplicationLogo(){
        super();
        LOGGER.fine("ApplicationLogo: creation ...");

        Icon appName = new ImageIcon(FileBrowser.FILE_IMAGE_NAME_LOGO.getAbsolutePath());
        JLabel name = new JLabel(appName);

        this.message = new JLabel(LOADING);
        this.message.setHorizontalAlignment(JLabel.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        JLabel developer = new JLabel(DEVELOPER);

        JLabel version = new JLabel(VERSION);
        version.setHorizontalAlignment(JLabel.RIGHT);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.add(name, new Cell(0,0,2));
        panel.add(this.message, new Cell(0,1,2));
        panel.add(developer, new Cell(0,2,1, new Insets(5,5,0,5)));
        panel.add(version, new Cell(1,2,1, new Insets(5,5,0,5)));
        panel.add(progressBar, new Cell(0,3,2, new Insets(5,0,0,0)));

        this.setSize(340,153);
        this.setLocation(ConverterUI.POINT_CENTER(Application.sizeOfScreen, this));
        this.getContentPane().add(panel);

        LOGGER.info("ApplicationLogo: creation SUCCESS");
    }

    public void setMessage(String message){
        this.message.setText(message);
    }

    private static class Cell extends GridBagConstraints {

        protected Cell(int x, int y, int width) {
            this.fill = BOTH;
            this.weightx = 1.0;

            this.gridx = x;
            this.gridy = y;
            this.gridwidth = width;
        }

        protected Cell(int x, int y, int width, Insets insets) {
            this.fill = BOTH;
            this.weightx = 1.0;

            this.gridx = x;
            this.gridy = y;
            this.gridwidth = width;
            this.insets = insets;
        }
    }
}