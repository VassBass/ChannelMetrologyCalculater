package ui.main.menu;

import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;

public class MenuBar extends JMenuBar implements UI_Container {
    private final MainScreen mainScreen;

    private MenuChannel menuChannel;
    private JMenu menuLists;

    public MenuBar(MainScreen mainScreen){
        super();
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.menuChannel = new MenuChannel(this.mainScreen);
        this.menuLists = new MenuLists(this.mainScreen);
    }

    @Override
    public void setReactions() {

    }

    @Override
    public void build() {
        this.add(this.menuChannel);
        this.add(this.menuLists);
    }
}
