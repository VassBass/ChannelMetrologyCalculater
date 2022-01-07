package ui;

import controller.FileBrowser;

import javax.swing.*;
import java.awt.*;

public class SaveMessage extends JWindow {
    private final Window window;
    private JLabel anim;

    public SaveMessage(Window window){
        super();
        this.window = window;
        this.setAlwaysOnTop(true);

        this.createElements();
        this.build();
    }

    public void createElements() {
        Icon animation = new ImageIcon(FileBrowser.FILE_IMAGE_ANIM_LOAD.getAbsolutePath());
        this.anim = new JLabel(animation);
    }

    public void build() {
        this.setSize(25,25);
        this.add(this.anim);
    }
}
