package ui.pathLists;

import application.Application;
import converters.ConverterUI;
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

    private final PathListsDialog parent;
    private final String elementType;
    private String elementName;

    private String[]elements;

    private JComboBox<String>elementList = null;
    private JLabel removingElement;
    private JButton buttonCancel, buttonRemoveAll, buttonRemove;

    public PathElementsRemove(PathListsDialog parent, String elementType, String elementName){
        super(parent, REMOVE + " \"" + elementType + "\"", true);
        this.parent = parent;
        this.elementType = elementType;
        this.elementName = elementName;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        switch (this.elementType){
            case PathListsTable.DEPARTMENTS_LIST:
                this.elements = Application.context.departmentService.getAllInStrings();
                break;
            case PathListsTable.AREAS_LIST:
                this.elements = Application.context.areaService.getAllInStrings();
                break;
            case PathListsTable.PROCESSES_LIST:
                this.elements = Application.context.processService.getAllInStrings();
                break;
            case PathListsTable.INSTALLATIONS_LIST:
                this.elements = Application.context.installationService.getAllInStrings();
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

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickRemoveAll = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ConfirmDialog(parent, elementType).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(PathElementsRemove.this)) return;
            dispose();
            if (elementName == null){
                try {
                    elementName = Objects.requireNonNull(elementList.getSelectedItem()).toString();
                }catch (NullPointerException ignored){}
            }
            switch (elementType){
                case PathListsTable.DEPARTMENTS_LIST:
                    Application.context.departmentService.remove(elementName);
                    break;
                case PathListsTable.AREAS_LIST:
                    Application.context.areaService.remove(elementName);
                    break;
                case PathListsTable.PROCESSES_LIST:
                    Application.context.processService.remove(elementName);
                    break;
                case PathListsTable.INSTALLATIONS_LIST:
                    Application.context.installationService.remove(elementName);
                    break;
            }
            parent.update(elementType);
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