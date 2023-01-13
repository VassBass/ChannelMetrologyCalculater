package ui.pathLists;

import model.Model;
import repository.PathElementRepository;
import service.repository.repos.area.AreaRepositorySQLite;
import service.repository.repos.department.DepartmentRepositorySQLite;
import service.repository.repos.installation.InstallationRepositorySQLite;
import service.repository.repos.process.ProcessRepositorySQLite;

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

    private static final PathElementRepository departmentRepository = DepartmentRepositorySQLite.getInstance();
    private static final PathElementRepository areaRepository = AreaRepositorySQLite.getInstance();
    private static final PathElementRepository processRepository = ProcessRepositorySQLite.getInstance();
    private static final PathElementRepository installationRepository = InstallationRepositorySQLite.getInstance();

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
                elements = areaRepository.getAll().toArray(new String[0]);
                break;
            case PROCESS:
                elements = processRepository.getAll().toArray(new String[0]);
                break;
            case INSTALLATION:
                elements = installationRepository.getAll().toArray(new String[0]);
                break;
            default:
                elements = departmentRepository.getAll().toArray(new String[0]);
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