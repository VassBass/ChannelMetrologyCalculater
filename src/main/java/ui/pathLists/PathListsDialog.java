package ui.pathLists;

import application.Application;
import converters.ConverterUI;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PathListsDialog extends JDialog {
    private static final String REMOVE = "Видалити";
    private static final String CHANGE = "Змінити";
    private static final String ADD = "Додати";
    private static final String CANCEL = "Відміна";
    private static final String DEPARTMENT = "Цех";
    private static final String AREA = "Ділянка";
    private static final String PROCESS = "Лінія, секція і т.п.";
    private static final String INSTALLATION = "Установка";

    private final MainScreen mainScreen;
    private final PathListsDialog current;

    private String title;

    private JButton buttonDepartments, buttonAreas, buttonProcesses, buttonInstallations;
    private JButton buttonCancel, buttonRemove, buttonAdd, buttonChange;

    private PathListsTable mainTable;

    public PathListsDialog(MainScreen mainScreen, String title){
        super(mainScreen, title, true);
        this.current = this;
        this.mainScreen = mainScreen;
        this.title = title;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.buttonDepartments = new DefaultButton(PathListsTable.DEPARTMENTS_LIST);
        this.buttonAreas = new DefaultButton(PathListsTable.AREAS_LIST);
        this.buttonProcesses = new DefaultButton(PathListsTable.PROCESSES_LIST);
        this.buttonInstallations = new DefaultButton(PathListsTable.INSTALLATIONS_LIST);

        this.buttonRemove = new DefaultButton(REMOVE);
        this.buttonChange = new DefaultButton(CHANGE);
        this.buttonAdd = new DefaultButton(ADD);
        this.buttonCancel = new DefaultButton(CANCEL);

        this.mainTable = new PathListsTable(title);
        this.setButtonsColor();
    }

    private void setButtonsColor(){
        switch (this.title){
            case PathListsTable.DEPARTMENTS_LIST:
                this.buttonDepartments.setBackground(Color.WHITE);
                this.buttonDepartments.setForeground(Color.BLACK);
                this.buttonAreas.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonAreas.setForeground(Color.WHITE);
                this.buttonProcesses.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonProcesses.setForeground(Color.WHITE);
                this.buttonInstallations.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonInstallations.setForeground(Color.WHITE);
                break;
            case PathListsTable.AREAS_LIST:
                this.buttonDepartments.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonDepartments.setForeground(Color.WHITE);
                this.buttonAreas.setBackground(Color.WHITE);
                this.buttonAreas.setForeground(Color.BLACK);
                this.buttonProcesses.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonProcesses.setForeground(Color.WHITE);
                this.buttonInstallations.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonInstallations.setForeground(Color.WHITE);
                break;
            case PathListsTable.PROCESSES_LIST:
                this.buttonDepartments.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonDepartments.setForeground(Color.WHITE);
                this.buttonAreas.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonAreas.setForeground(Color.WHITE);
                this.buttonProcesses.setBackground(Color.WHITE);
                this.buttonProcesses.setForeground(Color.BLACK);
                this.buttonInstallations.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonInstallations.setForeground(Color.WHITE);
                break;
            case PathListsTable.INSTALLATIONS_LIST:
                this.buttonDepartments.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonDepartments.setForeground(Color.WHITE);
                this.buttonAreas.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonAreas.setForeground(Color.WHITE);
                this.buttonProcesses.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonProcesses.setForeground(Color.WHITE);
                this.buttonInstallations.setBackground(Color.WHITE);
                this.buttonInstallations.setForeground(Color.BLACK);
                break;
        }
    }

    private void setReactions() {
        this.buttonDepartments.addActionListener(this.clickDepartments);
        this.buttonAreas.addActionListener(this.clickAreas);
        this.buttonProcesses.addActionListener(this.clickProcesses);
        this.buttonInstallations.addActionListener(this.clickInstallations);
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonAdd.addActionListener(this.clickAdd);
        this.buttonChange.addActionListener(this.clickChange);
        this.buttonRemove.addActionListener(this.clickRemove);
    }

    private void build() {
        this.setSize(800,500);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    public void update(String elementsType){
        switch (elementsType){
            case PathListsTable.AREAS_LIST:
                this.title = PathListsTable.AREAS_LIST;
                break;
            case PathListsTable.PROCESSES_LIST:
                this.title = PathListsTable.PROCESSES_LIST;
                break;
            case PathListsTable.INSTALLATIONS_LIST:
                this.title = PathListsTable.INSTALLATIONS_LIST;
                break;
            default:
                this.title = PathListsTable.DEPARTMENTS_LIST;
                break;
        }
        this.setButtonsColor();
        this.mainTable.setList(this.title);
    }

    private final ActionListener clickDepartments = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            title = PathListsTable.DEPARTMENTS_LIST;
            setButtonsColor();
            mainTable.setList(title);
        }
    };

    private final ActionListener clickAreas = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            title = PathListsTable.AREAS_LIST;
            setButtonsColor();
            mainTable.setList(title);
        }
    };

    private final ActionListener clickProcesses = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            title = PathListsTable.PROCESSES_LIST;
            setButtonsColor();
            mainTable.setList(title);
        }
    };

    private final ActionListener clickInstallations = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            title = PathListsTable.INSTALLATIONS_LIST;
            setButtonsColor();
            mainTable.setList(title);
        }
    };

    private final ActionListener clickAdd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    String elementType = null;
                    switch (title){
                        case PathListsTable.DEPARTMENTS_LIST:
                            elementType = DEPARTMENT;
                            break;
                        case PathListsTable.AREAS_LIST:
                            elementType = AREA;
                            break;
                        case PathListsTable.PROCESSES_LIST:
                            elementType = PROCESS;
                            break;
                        case PathListsTable.INSTALLATIONS_LIST:
                            elementType = INSTALLATION;
                            break;
                    }
                    new PathElementName(current, elementType, null).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainTable.getSelectedRow() != -1) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        String elementType = null;
                        String elementName = null;
                        switch (title) {
                            case PathListsTable.DEPARTMENTS_LIST:
                                elementType = DEPARTMENT;
                                elementName = Application.context.departmentsController.get(mainTable.getSelectedRow());
                                break;
                            case PathListsTable.AREAS_LIST:
                                elementType = AREA;
                                elementName = Application.context.areasController.get(mainTable.getSelectedRow());
                                break;
                            case PathListsTable.PROCESSES_LIST:
                                elementType = PROCESS;
                                elementName = Application.context.processesController.get(mainTable.getSelectedRow());
                                break;
                            case PathListsTable.INSTALLATIONS_LIST:
                                elementType = INSTALLATION;
                                elementName = Application.context.installationsController.get(mainTable.getSelectedRow());
                                break;
                        }
                        new PathElementName(current, elementType, elementName).setVisible(true);
                    }
                });
            }
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    String elementName = null;
                    if (mainTable.getSelectedRow() != -1){
                        switch (title) {
                            case PathListsTable.DEPARTMENTS_LIST:
                                elementName = Application.context.departmentsController.get(mainTable.getSelectedRow());
                                break;
                            case PathListsTable.AREAS_LIST:
                                elementName = Application.context.areasController.get(mainTable.getSelectedRow());
                                break;
                            case PathListsTable.PROCESSES_LIST:
                                elementName = Application.context.processesController.get(mainTable.getSelectedRow());
                                break;
                            case PathListsTable.INSTALLATIONS_LIST:
                                elementName = Application.context.installationsController.get(mainTable.getSelectedRow());
                                break;
                        }
                    }
                    new PathElementsRemove(current, title, elementName).setVisible(true);
                }
            });
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