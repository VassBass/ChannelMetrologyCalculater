package ui.model;

import javax.swing.*;
import java.awt.*;

import static ui.UI_ConfigHolder.SCREEN_SIZE;
import static ui.UI_Constants.POINT_CENTER;

public class DialogLoading extends JDialog {
    public static final String LOADING_PLEASE_WAIT = "Завантаження...будь ласка зачекайте...";

    public JProgressBar progressBar;

    public DialogLoading(){
        super();

        createDialog();
    }

    public DialogLoading(Window parent){
        super(parent,  ModalityType.APPLICATION_MODAL);
        this.setLocation(POINT_CENTER(parent, this));

        createDialog();
    }

    private void createDialog() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(LOADING_PLEASE_WAIT);

        this.setSize(300, 30);
        this.setLocation(POINT_CENTER(SCREEN_SIZE, this));
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);

        this.add(progressBar);
    }
}
