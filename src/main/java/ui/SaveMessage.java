package ui;

import javax.swing.*;
import java.awt.*;

public class SaveMessage extends JWindow implements UI_Container {
    private final Window window;
    private JLabel message;

    public SaveMessage(Window window){
        super(window);
        this.window = window;
    }
    @Override
    public void createElements() {
        this.message = new JLabel("Сохранение...");
    }

    @Override public void setReactions() {}

    @Override
    public void build() {
        this.setSize(200,100);
        this.setLocation(this.window.getLocation().x, this.window.getLocation().y);
        this.add(this.message);
    }
}
