package ui.mainScreen.menu;

import service.MainScreenEventListener;
import ui.mainScreen.channelTable.ChannelTable;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private final JMenu menuChannel;
    private JMenu menuLists;
    private JMenu menuExportImport;

    public MenuBar(MainScreenEventListener eventListener, ChannelTable channelTable){
        super();

        this.menuChannel = new MenuChannel(eventListener, channelTable);

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