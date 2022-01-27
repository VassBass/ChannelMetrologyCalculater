package ui.model;

import application.Application;
import converters.ConverterUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LoadDialog extends JWindow {
    public static final String LOADING_PLEASE_WAIT = "Завантаження...будь ласка зачекайте...";

    private final Container parent;

    public JProgressBar progressBar;

    public LoadDialog(){
        super();
        this.parent = null;

        this.createElements();
        this.build();
    }

    public LoadDialog(Container parent){
        super();
        this.parent = parent;
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