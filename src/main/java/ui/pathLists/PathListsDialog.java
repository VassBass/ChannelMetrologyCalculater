package ui.pathLists;

import constants.Strings;
import support.Converter;
import ui.UI_Container;
import ui.main.MainScreen;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PathListsDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;

    private String title;

    private JButton buttonDepartments, buttonAreas, buttonProcesses, buttonInstallations;
    private JButton buttonCancel, buttonRemove, buttonAdd, buttonChange;

    private PathListsTable mainTable;

    private final Color buttonsColor = new Color(51,51,51);

    public PathListsDialog(MainScreen mainScreen, String title){
        super(mainScreen, title, true);
        this.mainScreen = mainScreen;
        this.title = title;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.buttonDepartments = new JButton(Strings.DEPARTMENTS_LIST);
        this.buttonDepartments.setBackground(buttonsColor);
        this.buttonDepartments.setForeground(Color.white);
        this.buttonDepartments.setFocusPainted(false);
        this.buttonDepartments.setContentAreaFilled(false);
        this.buttonDepartments.setOpaque(true);

        this.buttonAreas = new JButton(Strings.AREAS_LIST);
        this.buttonAreas.setBackground(buttonsColor);
        this.buttonAreas.setForeground(Color.white);
        this.buttonAreas.setFocusPainted(false);
        this.buttonAreas.setContentAreaFilled(false);
        this.buttonAreas.setOpaque(true);

        this.buttonProcesses = new JButton(Strings.PROCESSES_LIST);
        this.buttonProcesses.setBackground(buttonsColor);
        this.buttonProcesses.setForeground(Color.white);
        this.buttonProcesses.setFocusPainted(false);
        this.buttonProcesses.setContentAreaFilled(false);
        this.buttonProcesses.setOpaque(true);

        this.buttonInstallations = new JButton(Strings.INSTALLATIONS_LIST);
        this.buttonInstallations.setBackground(buttonsColor);
        this.buttonInstallations.setForeground(Color.white);
        this.buttonInstallations.setFocusPainted(false);
        this.buttonInstallations.setContentAreaFilled(false);
        this.buttonInstallations.setOpaque(true);

        this.buttonRemove = new JButton(Strings.REMOVE);
        this.buttonRemove.setBackground(buttonsColor);
        this.buttonRemove.setForeground(Color.white);
        this.buttonRemove.setFocusPainted(false);
        this.buttonRemove.setContentAreaFilled(false);
        this.buttonRemove.setOpaque(true);

        this.buttonChange = new JButton(Strings.CHANGE);
        this.buttonChange.setBackground(buttonsColor);
        this.buttonChange.setForeground(Color.white);
        this.buttonChange.setFocusPainted(false);
        this.buttonChange.setContentAreaFilled(false);
        this.buttonChange.setOpaque(true);

        this.buttonAdd = new JButton(Strings.ADD);
        this.buttonAdd.setBackground(buttonsColor);
        this.buttonAdd.setForeground(Color.white);
        this.buttonAdd.setFocusPainted(false);
        this.buttonAdd.setContentAreaFilled(false);
        this.buttonAdd.setOpaque(true);

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(buttonsColor);
        this.buttonCancel.setForeground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);

        this.mainTable = new PathListsTable(title);
        this.setButtonsColor();
    }

    private void setButtonsColor(){
        switch (this.title){
            case Strings.DEPARTMENTS_LIST:
                this.buttonDepartments.setBackground(Color.white);
                this.buttonDepartments.setForeground(Color.black);
                this.buttonAreas.setBackground(buttonsColor);
                this.buttonAreas.setForeground(Color.white);
                this.buttonProcesses.setBackground(buttonsColor);
                this.buttonProcesses.setForeground(Color.white);
                this.buttonInstallations.setBackground(buttonsColor);
                this.buttonInstallations.setForeground(Color.white);
                break;
            case Strings.AREAS_LIST:
                this.buttonDepartments.setBackground(buttonsColor);
                this.buttonDepartments.setForeground(Color.white);
                this.buttonAreas.setBackground(Color.white);
                this.buttonAreas.setForeground(Color.black);
                this.buttonProcesses.setBackground(buttonsColor);
                this.buttonProcesses.setForeground(Color.white);
                this.buttonInstallations.setBackground(buttonsColor);
                this.buttonInstallations.setForeground(Color.white);
                break;
            case Strings.PROCESSES_LIST:
                this.buttonDepartments.setBackground(buttonsColor);
                this.buttonDepartments.setForeground(Color.white);
                this.buttonAreas.setBackground(buttonsColor);
                this.buttonAreas.setForeground(Color.white);
                this.buttonProcesses.setBackground(Color.white);
                this.buttonProcesses.setForeground(Color.black);
                this.buttonInstallations.setBackground(buttonsColor);
                this.buttonInstallations.setForeground(Color.white);
                break;
            case Strings.INSTALLATIONS_LIST:
                this.buttonDepartments.setBackground(buttonsColor);
                this.buttonDepartments.setForeground(Color.white);
                this.buttonAreas.setBackground(buttonsColor);
                this.buttonAreas.setForeground(Color.white);
                this.buttonProcesses.setBackground(buttonsColor);
                this.buttonProcesses.setForeground(Color.white);
                this.buttonInstallations.setBackground(Color.white);
                this.buttonInstallations.setForeground(Color.black);
                break;
        }
    }

    @Override
    public void setReactions() {
        this.buttonCancel.addChangeListener(pushButton);
        this.buttonRemove.addChangeListener(pushButton);
        this.buttonChange.addChangeListener(pushButton);
        this.buttonAdd.addChangeListener(pushButton);

        this.buttonDepartments.addActionListener(clickDepartments);
        this.buttonAreas.addActionListener(clickAreas);
        this.buttonProcesses.addActionListener(clickProcesses);
        this.buttonInstallations.addActionListener(clickInstallations);
        this.buttonCancel.addActionListener(clickCancel);
    }

    @Override
    public void build() {
        this.setSize(800,500);
        this.setLocation(Converter.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
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

    private final ActionListener clickDepartments = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            title = Strings.DEPARTMENTS_LIST;
            setButtonsColor();
            mainTable.update(title);
        }
    };

    private final ActionListener clickAreas = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            title = Strings.AREAS_LIST;
            setButtonsColor();
            mainTable.update(title);
        }
    };

    private final ActionListener clickProcesses = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            title = Strings.PROCESSES_LIST;
            setButtonsColor();
            mainTable.update(title);
        }
    };

    private final ActionListener clickInstallations = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            title = Strings.INSTALLATIONS_LIST;
            setButtonsColor();
            mainTable.update(title);
        }
    };

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(buttonDepartments, new Cell(0,0,1,0.1));
            this.add(buttonAreas, new Cell(1,0,1,0.1));
            this.add(buttonProcesses, new Cell(2,0,1,0.1));
            this.add(buttonInstallations, new Cell(3,0,1,0.1));
            this.add(new JScrollPane(mainTable), new Cell(0,1,4,0.8));
            this.add(buttonCancel, new Cell(0,2,1,0.1));
            this.add(buttonRemove, new Cell(1,2,1,0.1));
            this.add(buttonChange, new Cell(2,2,1,0.1));
            this.add(buttonAdd, new Cell(3,2,1,0.1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width, double weight){
                super();

                this.fill = BOTH;
                this.weightx = 1D;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
                this.weighty = weight;
            }
        }
    }
}
