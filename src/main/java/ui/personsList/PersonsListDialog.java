package ui.personsList;

import support.Converter;
import support.Lists;
import constants.Strings;
import ui.UI_Container;
import ui.main.MainScreen;
import ui.personsList.personInfo.PersonInfoDialog;
import ui.personsList.personInfo.removePerson.RemovePersonDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class PersonsListDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final PersonsListDialog current;

    private PersonsListTable mainTable;

    private JButton buttonAdd, buttonRemove, buttonChange;

    private final Color buttonsColor = new Color(51,51,51);

    public PersonsListDialog(MainScreen mainScreen){
        super(mainScreen, Strings.WORKERS, true);
        this.mainScreen = mainScreen;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.mainTable = new PersonsListTable(this);

        this.buttonAdd = new JButton(Strings.ADD);
        this.buttonAdd.setBackground(buttonsColor);
        this.buttonAdd.setForeground(Color.white);
        this.buttonAdd.setFocusPainted(false);
        this.buttonAdd.setContentAreaFilled(false);
        this.buttonAdd.setOpaque(true);

        this.buttonChange = new JButton(Strings.CHANGE);
        this.buttonChange.setBackground(buttonsColor);
        this.buttonChange.setForeground(Color.white);
        this.buttonChange.setFocusPainted(false);
        this.buttonChange.setContentAreaFilled(false);
        this.buttonChange.setOpaque(true);

        this.buttonRemove = new JButton(Strings.REMOVE);
        this.buttonRemove.setBackground(buttonsColor);
        this.buttonRemove.setForeground(Color.white);
        this.buttonRemove.setFocusPainted(false);
        this.buttonRemove.setContentAreaFilled(false);
        this.buttonRemove.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.buttonAdd.addChangeListener(this.pushButton);
        this.buttonChange.addChangeListener(this.pushButton);
        this.buttonRemove.addChangeListener(this.pushButton);

        this.buttonAdd.addActionListener(this.clickAdd);
        this.buttonChange.addActionListener(this.clickChange);
        this.buttonRemove.addActionListener(this.clickRemove);
    }

    @Override
    public void build() {
        this.setSize(800,600);
        this.setLocation(Converter.POINT_CENTER(this.mainScreen, this));
        this.setEnabledButtons(false);

        this.setContentPane(new MainPanel());
    }

    public void update(){
        this.mainTable = new PersonsListTable(this);
        this.setContentPane(new MainPanel());
        setEnabledButtons(false);
        this.setVisible(false);
        this.setVisible(true);
    }

    public void setEnabledButtons(boolean enabled){
        this.buttonChange.setEnabled(enabled);
        this.buttonRemove.setEnabled(enabled);
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()){
                button.setBackground(buttonsColor.darker());
            }else {
                button.setBackground(buttonsColor);
            }
        }
    };

    private final ActionListener clickAdd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new PersonInfoDialog(current, null).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int index = mainTable.getSelectedRow();
                    new PersonInfoDialog(current, Objects.requireNonNull(Lists.persons()).get(index)).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int index = mainTable.getSelectedRow();
                    new RemovePersonDialog(current, Objects.requireNonNull(Lists.persons()).get(index)).setVisible(true);
                }
            });
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(new JScrollPane(mainTable), new Cell(0,0.9));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonRemove);
            buttonsPanel.add(buttonChange);
            buttonsPanel.add(buttonAdd);
            this.add(buttonsPanel, new Cell(1, 0.1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int y, double weighty){
                super();

                this.weightx = 1.0;
                this.weighty = weighty;
                this.gridx = 0;
                this.fill = BOTH;

                this.gridy = y;
            }
        }
    }

}
