package ui.pathLists;

import constants.Strings;
import support.Lists;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Objects;

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
                elements = Objects.requireNonNull(Lists.areas()).toArray(new String[0]);
                break;
            case Strings.PROCESSES_LIST:
                elements = Objects.requireNonNull(Lists.processes()).toArray(new String[0]);
                break;
            case Strings.INSTALLATIONS_LIST:
                elements = Objects.requireNonNull(Lists.installations()).toArray(new String[0]);
                break;
            default:
                elements = Objects.requireNonNull(Lists.departments()).toArray(new String[0]);
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
