package ui.mainScreen.menu;

import ui.event.EventSource;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private final JMenu menuChannel;
    private JMenu menuLists;
    private JMenu menuExportImport;

    public MenuBar(EventSource eventSource){
        super();

        this.menuChannel = new MenuChannel(eventSource);

        this.createElements();
        this.build();
    }

    private void createElements() {
        this.menuLists = new MenuLists();
        this.menuExportImport = new MenuExpImp();
    }

    private void build() {
        this.add(this.menuChannel);
        this.add(this.menuLists);
        this.add(this.menuExportImport);
    }
}