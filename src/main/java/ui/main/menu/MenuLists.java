package ui.main.menu;

import constants.Strings;
import ui.UI_Container;
import ui.main.MainScreen;
import ui.personsList.PersonsListDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuLists extends JMenu implements UI_Container {
    private final MainScreen mainScreen;

    private JMenuItem buttonPersons;

    public MenuLists(MainScreen mainScreen){
        super(Strings.LISTS);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.buttonPersons = new JMenuItem(Strings.WORKERS);
    }

    @Override
    public void setReactions() {
        this.buttonPersons.addActionListener(clickButtonPersons);
    }

    @Override
    public void build() {
        this.add(this.buttonPersons);
    }

    private final ActionListener clickButtonPersons = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new PersonsListDialog(mainScreen).setVisible(true);
                }
            });
        }
    };
}
