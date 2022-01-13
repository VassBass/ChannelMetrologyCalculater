package ui.model;

import application.Application;
import controller.FileBrowser;
import converters.ConverterUI;

import javax.swing.*;
import java.awt.*;

public class ApplicationLogo extends JWindow {
    public static final String LOADING = "Завантаження...";
    public static final String DEVELOPER = "VassBassApp";
    public static final String VERSION = "v5.0.4";

    private final JLabel message;

    public ApplicationLogo(){
        super();

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
    }

    public void setMessage(String message){
        this.message.setText(message);
    }

    private static class Cell extends GridBagConstraints {

        private static final long serialVersionUID = 1L;

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