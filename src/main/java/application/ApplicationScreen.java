package application;

import model.ui.MainScreen;

import javax.swing.*;

public class ApplicationScreen extends MainScreen {
    private final ApplicationMenu menuBar;

    public ApplicationScreen(ApplicationConfigHolder configHolder) {
        super();
        this.menuBar = new ApplicationMenu();

        this.setTitle(Labels.getInstance().applicationName);
        this.setJMenuBar(menuBar);

        this.setSize(configHolder.getScreenWidth(), configHolder.getScreenHeight());
    }

    public void addMenu(JMenu menu) {
        menuBar.add(menu);
    }

    public ApplicationMenu getMenu() {
        return menuBar;
    }
}
