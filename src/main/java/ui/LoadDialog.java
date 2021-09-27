package ui;

import support.Converter;
import constants.Strings;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LoadDialog extends JDialog implements UI_Container {
    private final Container parent;

    public JProgressBar progressBar;

    public LoadDialog(){
        super();
        this.parent = null;

        this.createElements();
        this.setReactions();
        this.build();
    }

    public LoadDialog(Container parent){
        super();
        this.parent = parent;
        this.setLocation(Converter.POINT_CENTER(parent, this));

        this.createElements();
        this.setReactions();
        this.build();
        this.setVisible(true);
    }
    @Override
    public void createElements() {
        this.progressBar = new JProgressBar();
        this.progressBar.setIndeterminate(true);
        this.progressBar.setStringPainted(true);
        this.progressBar.setString(Strings.LOADING_PLEASE_WAIT);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        if (parent == null){
            this.addWindowListener(windowListener);
        }
    }

    @Override
    public void build() {
        this.setSize(300, 60);
        this.setLocation(Converter.POINT_CENTER(MainScreen.sizeOfMainScreen, this));
        this.setResizable(false);
        this.setAlwaysOnTop(true);

        this.add(this.progressBar);
    }

    private final WindowListener windowListener = new WindowListener() {
        @Override public void windowOpened(WindowEvent e) {}
        @Override public void windowClosed(WindowEvent e) {}
        @Override public void windowIconified(WindowEvent e) {}
        @Override public void windowDeiconified(WindowEvent e) {}
        @Override public void windowActivated(WindowEvent e) {}
        @Override public void windowDeactivated(WindowEvent e) {}

        @Override
        public void windowClosing(WindowEvent e) {
            if (parent == null) {
                System.exit(0);
            }
        }
    };
}
