package ui.pathLists;

import model.Model;
import service.impl.AreaServiceImpl;
import service.impl.DepartmentServiceImpl;
import service.impl.InstallationServiceImpl;
import service.impl.ProcessServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PathListsTable extends JTable {
    private static final String DEPARTMENTS_LIST = "Список цехів";
    private static final String AREAS_LIST = "Список ділянок";
    private static final String PROCESSES_LIST = "Список ліній, секцій і т.п";
    private static final String INSTALLATIONS_LIST = "Список установок";

    private static String list(Model model){
        switch (model){
            case DEPARTMENT:
                return DEPARTMENTS_LIST;
            case AREA:
                return AREAS_LIST;
            case PROCESS:
                return PROCESSES_LIST;
            case INSTALLATION:
                return INSTALLATIONS_LIST;
            default: return null;
        }
    }
    public PathListsTable(Model model){
        super(tableModel(model));
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private static DefaultTableModel tableModel(Model model){
        DefaultTableModel defModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        defModel.setColumnIdentifiers(new String[]{list(model)});

        String[] elements;
        switch (model){
            case AREA:
                elements = AreaServiceImpl.getInstance().getAllInStrings();
                break;
            case PROCESS:
                elements = ProcessServiceImpl.getInstance().getAllInStrings();
                break;
            case INSTALLATION:
                elements = InstallationServiceImpl.getInstance().getAllInStrings();
                break;
            default:
                elements = DepartmentServiceImpl.getInstance().getAllInStrings();
                break;
        }
        for (String element : elements){
            defModel.addRow(new String[]{element});
        }
        return defModel;
    }

    public void setList(Model model){
        this.setModel(tableModel(model));
    }
}