package ui.model;

import converters.ConverterUI;
import ui.mainScreen.MainScreen;

import javax.swing.*;

public class LoadWindow extends JWindow {
    private JProgressBar progressBar;
    private JDialog parent;

    public LoadWindow(){
        super(MainScreen.getInstance());

        this.createElements();
        this.build();
    }

    public LoadWindow(JDialog parent){
        super(parent);
        this.parent = parent;

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
        if (this.parent == null) {
            this.setLocation(ConverterUI.POINT_CENTER(MainScreen.getInstance(), this));
        }else {
            this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
        }
        this.setAlwaysOnTop(true);

        this.add(this.progressBar);
    }

    public void setValue(int value){
        this.progressBar.setValue(value);
    }
}