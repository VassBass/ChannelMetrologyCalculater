package ui.pathLists;

import model.Model;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.UI_Constants.POINT_CENTER;

public class PathListsDialog extends JDialog {
    private static final String REMOVE = "Видалити";
    private static final String CHANGE = "Змінити";
    private static final String ADD = "Додати";
    private static final String CANCEL = "Відміна";
    public static final String DEPARTMENT = "Цех";
    public static final String AREA = "Ділянка";
    public static final String PROCESS = "Лінія, секція і т.п.";
    public static final String INSTALLATION = "Установка";
    private static final String DEPARTMENTS_LIST = "Список цехів";
    private static final String AREAS_LIST = "Список ділянок";
    private static final String PROCESSES_LIST = "Список ліній, секцій і т.п";
    private static final String INSTALLATIONS_LIST = "Список установок";

    private final MainScreen mainScreen;

    private Model model;
    private static String title(Model model){
        switch (model){
            case DEPARTMENT:
                return DEPARTMENTS_LIST;
            case AREA:
                return AREAS_LIST;
            case PROCESS:
                return PROCESSES_LIST;
            case INSTALLATION:
                return INSTALLATIONS_LIST;
            default:
                return null;
        }
    }

    private JButton buttonDepartments, buttonAreas, buttonProcesses, buttonInstallations;
    private JButton buttonCancel, buttonRemove, buttonAdd, buttonChange;

    private PathListsTable mainTable;

    public PathListsDialog(MainScreen mainScreen, Model model){
        super(mainScreen, title(model), true);
        this.mainScreen = mainScreen;
        this.model = model;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.buttonDepartments = new JButton(DEPARTMENTS_LIST);
        this.buttonDepartments.setFocusPainted(false);
        this.buttonAreas = new JButton(AREAS_LIST);
        this.buttonAreas.setFocusPainted(false);
        this.buttonProcesses = new JButton(PROCESSES_LIST);
        this.buttonProcesses.setFocusPainted(false);
        this.buttonInstallations = new JButton(INSTALLATIONS_LIST);
        this.buttonInstallations.setFocusPainted(false);

        this.buttonRemove = new DefaultButton(REMOVE);
        this.buttonChange = new DefaultButton(CHANGE);
        this.buttonAdd = new DefaultButton(ADD);
        this.buttonCancel = new DefaultButton(CANCEL);

        this.mainTable = new PathListsTable(this.model);
        this.setButtonsColor();
    }

    private void setButtonsColor(){
        switch (this.model){
            case DEPARTMENT:
                this.buttonDepartments.setBackground(Color.WHITE);
                this.buttonDepartments.setForeground(Color.BLACK);
                this.buttonAreas.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonAreas.setForeground(Color.WHITE);
                this.buttonProcesses.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonProcesses.setForeground(Color.WHITE);
                this.buttonInstallations.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonInstallations.setForeground(Color.WHITE);
                break;
            case AREA:
                this.buttonDepartments.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonDepartments.setForeground(Color.WHITE);
                this.buttonAreas.setBackground(Color.WHITE);
                this.buttonAreas.setForeground(Color.BLACK);
                this.buttonProcesses.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonProcesses.setForeground(Color.WHITE);
                this.buttonInstallations.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonInstallations.setForeground(Color.WHITE);
                break;
            case PROCESS:
                this.buttonDepartments.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonDepartments.setForeground(Color.WHITE);
                this.buttonAreas.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonAreas.setForeground(Color.WHITE);
                this.buttonProcesses.setBackground(Color.WHITE);
                this.buttonProcesses.setForeground(Color.BLACK);
                this.buttonInstallations.setBackground(DefaultButton.BACKGROUND_COLOR);
                this.buttonInstallations.setForeground(Color.WHITE);
                break;
            case INSTALLATION:
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
        this.setLocation(POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    public void update(Model model){
        this.model = model;
        this.setButtonsColor();
        this.mainTable.setList(model);
    }

    private final ActionListener clickDepartments = e -> update(Model.DEPARTMENT);

    private final ActionListener clickAreas = e -> update(Model.AREA);

    private final ActionListener clickProcesses = e -> update(Model.PROCESS);

    private final ActionListener clickInstallations = e -> update(Model.INSTALLATION);

    private final ActionListener clickAdd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(() -> new PathElementName(PathListsDialog.this, model, null).setVisible(true));
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainTable.getSelectedRow() != -1) {
//                EventQueue.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        String elementName = null;
//                        switch (model) {
//                            case DEPARTMENT:
//                                elementName = Application.context.departmentService.get(mainTable.getSelectedRow());
//                                break;
//                            case AREA:
//                                elementName = Application.context.areaService.get(mainTable.getSelectedRow());
//                                break;
//                            case PROCESS:
//                                elementName = Application.context.processService.get(mainTable.getSelectedRow());
//                                break;
//                            case INSTALLATION:
//                                elementName = Application.context.installationService.get(mainTable.getSelectedRow());
//                                break;
//                        }
//                        new PathElementName(PathListsDialog.this, model, elementName).setVisible(true);
//                    }
//                });
            }
        }
    };

    private final ActionListener clickRemove = e -> {
//            EventQueue.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    String elementName = null;
//                    if (mainTable.getSelectedRow() != -1){
//                        switch (model) {
//                            case DEPARTMENT:
//                                elementName = Application.context.departmentService.get(mainTable.getSelectedRow());
//                                break;
//                            case AREA:
//                                elementName = Application.context.areaService.get(mainTable.getSelectedRow());
//                                break;
//                            case PROCESS:
//                                elementName = Application.context.processService.get(mainTable.getSelectedRow());
//                                break;
//                            case INSTALLATION:
//                                elementName = Application.context.installationService.get(mainTable.getSelectedRow());
//                                break;
//                        }
//                    }
//                    new PathElementsRemove(PathListsDialog.this, model, elementName).setVisible(true);
//                }
//            });
    };

    private final ActionListener clickCancel = e -> dispose();

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