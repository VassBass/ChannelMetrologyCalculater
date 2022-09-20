package ui.mainScreen.menu;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private JMenu menuChannel;
    private JMenu menuLists;
    private JMenu menuExportImport;

    public MenuBar(){
        super();
        this.createElements();
        this.build();
    }

    private void createElements() {
        this.menuChannel = new MenuChannel();
        this.menuLists = new MenuLists();
        this.menuExportImport = new MenuExpImp();
    }

    private void build() {
        this.add(this.menuChannel);
        this.add(this.menuLists);
        this.add(this.menuExportImport);
    }
}