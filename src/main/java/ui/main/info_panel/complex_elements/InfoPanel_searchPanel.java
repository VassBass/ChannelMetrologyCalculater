package ui.main.info_panel.complex_elements;

import support.Lists;
import constants.Strings;
import ui.ButtonCell;
import ui.UI_Container;
import ui.main.MainScreen;
import ui.searchChannel.DialogSearch;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoPanel_searchPanel extends JPanel implements UI_Container {
    private final MainScreen mainScreen;

    private final Color buttonsColor = new Color(51,51,51);

    private JButton buttonSearch;
    private JButton field, value;

    public InfoPanel_searchPanel(MainScreen mainScreen){
        super(new GridBagLayout());
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.buttonSearch = new JButton(Strings.SEARCH);
        this.buttonSearch.setBackground(buttonsColor);
        this.buttonSearch.setForeground(Color.white);
        this.buttonSearch.setFocusPainted(false);
        this.buttonSearch.setContentAreaFilled(false);
        this.buttonSearch.setOpaque(true);

        this.field = new ButtonCell(false," - ");
        this.value = new ButtonCell(false," - ");
    }

    @Override
    public void setReactions() {
        this.buttonSearch.addChangeListener(pushButton);

        this.buttonSearch.addActionListener(clickSearch);
    }

    @Override
    public void build() {
        this.add(this.buttonSearch, new Cell(0));
        this.add(this.field, new Cell(1));
        this.add(this.value, new Cell(2));
    }

    public void update(boolean searchOn, String field, String value){
        if (searchOn){
            this.buttonSearch.setText(Strings.SEARCH_CANCEL);
            this.field.setText(field);
            this.value.setText(value);
        }else {
            this.buttonSearch.setText(Strings.SEARCH);
            this.field.setText(" - ");
            this.value.setText(" - ");
        }
    }

    private final ActionListener clickSearch = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonSearch.getText().equals(Strings.SEARCH)){
                new DialogSearch(mainScreen).setVisible(true);
            }else {
                mainScreen.update(Lists.channels(), false, null, null);
            }
        }
    };

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                button.setBackground(buttonsColor.darker());
            }else {
                button.setBackground(buttonsColor);
            }

        }
    };

    private static class Cell extends GridBagConstraints{

        protected Cell(int y){
            super();

            this.fill = BOTH;
            this.weightx = 1.0;
            this.weighty = 1.0;

            this.gridx = 0;
            this.gridy = y;
        }
    }
}
