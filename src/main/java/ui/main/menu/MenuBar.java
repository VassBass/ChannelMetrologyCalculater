package ui.main.menu;

import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;

public class MenuBar extends JMenuBar implements UI_Container {
    private final MainScreen mainScreen;

    private JMenu menuChannel;
    private JMenu menuLists;
    private JMenu menuExportImport;

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
        this.menuExportImport = new MenuExpImp(this.mainScreen);
    }

    @Override public void setReactions() {}

    @Override
    public void build() {
        this.add(this.menuChannel);
        this.add(this.menuLists);
        this.add(this.menuExportImport);
    }
}
