package ui.pathLists;

import application.Application;
import constants.Strings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PathListsTable extends JTable {

    public PathListsTable(String list){
        super(tableModel(list));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private static DefaultTableModel tableModel(String list){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        model.setColumnIdentifiers(new String[]{list});

        String[] elements;
        switch (list){
            case Strings.AREAS_LIST:
                elements = Application.context.areasController.getAllInStrings();
                break;
            case Strings.PROCESSES_LIST:
                elements = Application.context.processesController.getAllInStrings();
                break;
            case Strings.INSTALLATIONS_LIST:
                elements = Application.context.installationsController.getAllInStrings();
                break;
            default:
                elements = Application.context.departmentsController.getAllInStrings();
                break;
        }

        for (String element : elements){
            model.addRow(new String[]{element});
        }

        return model;
    }

    public void update(String list){
        this.setModel(tableModel(list));
    }
}
