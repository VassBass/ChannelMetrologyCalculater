package ui.pathLists;

import application.Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PathListsTable extends JTable {
    public static final String DEPARTMENTS_LIST = "Список цехів";
    public static final String AREAS_LIST = "Список ділянок";
    public static final String PROCESSES_LIST = "Список ліній, секцій і т.п";
    public static final String INSTALLATIONS_LIST = "Список установок";

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
            case AREAS_LIST:
                elements = Application.context.areaService.getAllInStrings();
                break;
            case PROCESSES_LIST:
                elements = Application.context.processService.getAllInStrings();
                break;
            case INSTALLATIONS_LIST:
                elements = Application.context.installationService.getAllInStrings();
                break;
            default:
                elements = Application.context.departmentService.getAllInStrings();
                break;
        }
        for (String element : elements){
            model.addRow(new String[]{element});
        }
        return model;
    }

    public void setList(String list){
        this.setModel(tableModel(list));
    }
}