package ui.pathLists;

import converters.ConverterUI;
import model.Model;
import repository.PathElementRepository;
import repository.impl.AreaRepositorySQLite;
import repository.impl.DepartmentRepositorySQLite;
import repository.impl.InstallationRepositorySQLite;
import repository.impl.ProcessRepositorySQLite;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class PathElementsRemove extends JDialog {
    private static final String REMOVE = "Видалити";
    private static final String CANCEL = "Відміна";
    private static final String REMOVE_ALL = "Видалити всі";
    private static final String DEPARTMENTS_LIST = "Список цехів";
    private static final String AREAS_LIST = "Список ділянок";
    private static final String PROCESSES_LIST = "Список ліній, секцій і т.п";
    private static final String INSTALLATIONS_LIST = "Список установок";

    private final PathListsDialog parent;

    private final Model model;
    private static String elementType(Model model){
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
    private String elementName;

    private String[]elements;

    private JComboBox<String>elementList = null;
    private JLabel removingElement;
    private JButton buttonCancel, buttonRemoveAll, buttonRemove;

    private final PathElementRepository departmentRepository = DepartmentRepositorySQLite.getInstance();
    private final PathElementRepository areaRepository = AreaRepositorySQLite.getInstance();
    private final PathElementRepository processRepository = ProcessRepositorySQLite.getInstance();
    private final PathElementRepository installationRepository = InstallationRepositorySQLite.getInstance();

    public PathElementsRemove(PathListsDialog parent, Model model, String elementName){
        super(parent, REMOVE + " \"" + elementType(model) + "\"", true);
        this.parent = parent;
        this.model = model;
        this.elementName = elementName;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        switch (model){
            case DEPARTMENT:
                this.elements = departmentRepository.getAll().toArray(new String[0]);
                break;
            case AREA:
                this.elements = areaRepository.getAll().toArray(new String[0]);
                break;
            case PROCESS:
                this.elements = processRepository.getAll().toArray(new String[0]);
                break;
            case INSTALLATION:
                this.elements = installationRepository.getAll().toArray(new String[0]);
                break;
        }
        if (this.elementName == null){
            this.elementList = new JComboBox<>(this.elements);
        }else {
            this.removingElement = new JLabel(REMOVE
                    + " \""
                    + this.elementName
                    + "\""
                    + "?");
        }

        this.buttonCancel = new DefaultButton(CANCEL);
        this.buttonRemoveAll = new DefaultButton(REMOVE_ALL);
        this.buttonRemove = new DefaultButton(REMOVE);
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonRemoveAll.addActionListener(this.clickRemoveAll);
        this.buttonRemove.addActionListener(this.clickRemove);
    }

    private void build() {
        this.setSize(400,100);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = e -> dispose();

    private final ActionListener clickRemoveAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            EventQueue.invokeLater(() -> new ConfirmDialog(parent, model).setVisible(true));
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            if (elementName == null){
                try {
                    elementName = Objects.requireNonNull(elementList.getSelectedItem()).toString();
                }catch (NullPointerException ignored){}
            }
            switch (model){
                case DEPARTMENT:
                    departmentRepository.remove(elementName);
                    break;
                case AREA:
                    areaRepository.remove(elementName);
                    break;
                case PROCESS:
                    processRepository.remove(elementName);
                    break;
                case INSTALLATION:
                    installationRepository.remove(elementName);
                    break;
            }
            parent.update(model);
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            if (elementName == null){
                this.add(elementList, new Cell(0,0,3));
            }else {
                this.add(removingElement, new Cell(0,0,3));
            }
            this.add(buttonCancel, new Cell(0,1,1));
            this.add(buttonRemoveAll, new Cell(1,1,1));
            this.add(buttonRemove, new Cell(2,1,1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell (int x, int y, int width){
                super();

                this.fill = BOTH;
                this.weightx = 1.0;
                this.weighty = 1.0;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}