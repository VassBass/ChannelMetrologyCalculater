package ui.model;

import application.Application;
import converters.ConverterUI;

import javax.swing.*;

public class ImportLoadWindow extends JWindow {
    private static final String TITLE = "Імпорт даних";

    private JProgressBar progressBar;

    public ImportLoadWindow(){
        super(Application.context.mainScreen);

        this.createElements();
        this.build();
    }

    private void createElements(){
        this.progressBar = new JProgressBar();
        this.progressBar.setStringPainted(true);
        this.progressBar.setMinimum(0);
        this.progressBar.setMinimum(100);
    }

    private void build(){
        this.setSize(300,30);
        this.setLocation(ConverterUI.POINT_CENTER(Application.context.mainScreen, this));
        this.setAlwaysOnTop(true);

        this.add(this.progressBar);
    }

    public void setValue(int value){
        this.progressBar.setValue(value);
    }
}
