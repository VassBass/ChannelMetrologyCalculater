package ui.model;

import application.Application;
import converters.ConverterUI;

import javax.swing.*;
import java.awt.*;

public class LoadDialog extends JWindow {
    public static final String LOADING_PLEASE_WAIT = "Завантаження...будь ласка зачекайте...";

    public JProgressBar progressBar;

    public LoadDialog(){
        super();

        this.createElements();
        this.build();
    }

    public LoadDialog(Container parent){
        super();
        this.setLocation(ConverterUI.POINT_CENTER(parent, this));

        this.createElements();
        this.build();
        this.setVisible(true);
    }

    public void createElements() {
        this.progressBar = new JProgressBar();
        this.progressBar.setIndeterminate(true);
        this.progressBar.setStringPainted(true);
        this.progressBar.setString(LOADING_PLEASE_WAIT);
    }

    public void build() {
        this.setSize(300, 30);
        this.setLocation(ConverterUI.POINT_CENTER(Application.sizeOfScreen, this));
        this.setAlwaysOnTop(true);

        this.add(this.progressBar);
    }
}