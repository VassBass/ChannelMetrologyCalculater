package ui.main.info_panel;

import ui.UI_Container;
import ui.main.MainScreen;
import ui.main.info_panel.complex_elements.InfoPanel_buttonsPanel;
import ui.main.info_panel.complex_elements.InfoPanel_infoTable;
import ui.main.info_panel.complex_elements.InfoPanel_searchPanel;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel implements UI_Container {
    private final MainScreen mainScreen;

    public InfoPanel_infoTable infoTable;
    public InfoPanel_buttonsPanel buttonsPanel;
    public InfoPanel_searchPanel searchPanel;

    public InfoPanel(MainScreen mainScreen){
        super(new GridBagLayout());
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.infoTable = new InfoPanel_infoTable();
        this.buttonsPanel = new InfoPanel_buttonsPanel(mainScreen);
        this.searchPanel = new InfoPanel_searchPanel();
    }

    @Override
    public void setReactions() {

    }

    @Override
    public void build() {
        this.add(this.infoTable, new Cell(0, 0, 2));
        this.add(this.searchPanel, new Cell(0, 1, 1));
        this.add(this.buttonsPanel, new Cell(1, 1, 1));
    }

    private static class Cell extends GridBagConstraints {

        private static final long serialVersionUID = 1L;

        protected Cell(int x, int y, int width){
            super();

            this.fill = BOTH;
            this.insets = new Insets(10,10,10,10);
            this.weightx = 1.0;
            this.weighty = 1.0;

            this.gridx = x;
            this.gridy = y;
            this.gridwidth = width;
        }
    }
}
