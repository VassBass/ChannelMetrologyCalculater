package ui.mainScreen.menu;

import ui.mainScreen.MainScreen;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private final MainScreen mainScreen;

    private JMenu menuChannel;
    private JMenu menuLists;
    private JMenu menuExportImport;

    public MenuBar(MainScreen mainScreen){
        super();
        this.mainScreen = mainScreen;

        this.createElements();
        this.build();
    }

    private void createElements() {
        this.menuChannel = new MenuChannel(this.mainScreen);
        this.menuLists = new MenuLists(this.mainScreen);
        this.menuExportImport = new MenuExpImp(this.mainScreen);
    }

    private void build() {
        this.add(this.menuChannel);
        this.add(this.menuLists);
        this.add(this.menuExportImport);
    }
}