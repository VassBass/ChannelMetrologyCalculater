package service.control_points.list.ui.swing;

import model.dto.ControlPoints;
import repository.RepositoryFactory;
import repository.repos.control_points.ControlPointsRepository;
import service.control_points.list.ui.ControlPointsListTable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class SwingControlPointsListTable extends JTable implements ControlPointsListTable {
    private static final String COLUMN_NAME = "Назва";

    public SwingControlPointsListTable(@Nonnull RepositoryFactory repositoryFactory){
        super();
        ControlPointsRepository controlPointsRepository = repositoryFactory.getImplementation(ControlPointsRepository.class);

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.setModel(tableModel(new ArrayList<>(controlPointsRepository.getAll())));
    }

    private static DefaultTableModel tableModel(List<ControlPoints> list){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {
                COLUMN_NAME
        };
        model.setColumnIdentifiers(columnsHeader);

        for (ControlPoints controlPoints : list) {
            String[] data = new String[1];
            data[0] = controlPoints.getName();
            model.addRow(data);
        }

        return model;
    }

    @Override
    public void setControlPointsList(List<ControlPoints> list) {
        this.setModel(tableModel(list));
    }

    @Nullable
    @Override
    public String getSelectedControlPointsName() {
        int row = this.getSelectedRow();
        return row < 0 ? null : this.getModel().getValueAt(row, 0).toString();
    }
}
