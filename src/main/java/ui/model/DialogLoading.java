package ui.model;

import application.Application;
import converters.ConverterUI;

import javax.swing.*;
import java.awt.*;

public class DialogLoading extends JDialog {
    public static final String LOADING_PLEASE_WAIT = "Завантаження...будь ласка зачекайте...";

    public JProgressBar progressBar;

    public DialogLoading(){
        super();

        createDialog();
    }

    public DialogLoading(Window parent){
        super(parent,  ModalityType.APPLICATION_MODAL);
        this.setLocation(ConverterUI.POINT_CENTER(parent, this));

        createDialog();
    }

    private void createDialog() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(LOADING_PLEASE_WAIT);

        this.setSize(300, 30);
        this.setLocation(ConverterUI.POINT_CENTER(Application.sizeOfScreen, this));
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);

        this.add(progressBar);
    }
}
