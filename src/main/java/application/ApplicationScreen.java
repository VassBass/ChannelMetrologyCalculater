package application;

import localization.Labels;
import model.ui.MainScreen;

import javax.swing.*;

public class ApplicationScreen extends MainScreen {
    private final ApplicationMenu menuBar;

    public ApplicationScreen(ApplicationConfigHolder configHolder) {
        super();
        this.menuBar = new ApplicationMenu();

        this.setTitle(Labels.APPLICATION_NAME);
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
