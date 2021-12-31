package ui;

import ui.main.MainScreen;

import javax.swing.*;

public class SaveMessage extends JWindow implements UI_Container {
    private final MainScreen mainScreen;
    private JLabel message;

    public SaveMessage(MainScreen mainScreen){
        super(mainScreen);
        this.mainScreen = mainScreen;
    }
    @Override
    public void createElements() {
        this.message = new JLabel("Сохранение...");
    }

    @Override public void setReactions() {}

    @Override
    public void build() {
        this.setSize(200,100);
        this.setLocation(this.mainScreen.getLocation().x, this.mainScreen.getLocation().y);
        this.add(this.message);
    }
}
