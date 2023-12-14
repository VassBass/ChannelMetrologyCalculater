package service.sensor_error.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.dto.SensorError;
import repository.RepositoryFactory;
import repository.repos.sensor_error.SensorErrorRepository;
import service.sensor_error.list.ui.SensorErrorListTable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwingSensorErrorListTable extends JTable implements SensorErrorListTable {

    public SwingSensorErrorListTable(@Nonnull RepositoryFactory repositoryFactory){
        super();
        SensorErrorRepository sensorErrorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.setModel(tableModel(new ArrayList<>(sensorErrorRepository.getAll())));
    }

    private DefaultTableModel tableModel(List<SensorError> sensorErrorList){
        Map<String, String> labels = Labels.getRootLabels();

        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {
                labels.get(RootLabelName.NAME),
                labels.get(RootLabelName.FORMULA)
        };
        model.setColumnIdentifiers(columnsHeader);

        for (SensorError sensorError : sensorErrorList) {
            String[] data = new String[2];
            data[0] = sensorError.getId();
            data[1] = sensorError.getErrorFormula();
            model.addRow(data);
        }

        return model;
    }

    @Nullable
    @Override
    public String getSelectedId() {
        int row = this.getSelectedRow();
        return row < 0 ? null : this.getModel().getValueAt(row, 0).toString();
    }

    @Override
    public void setSensorErrorsList(List<SensorError> list) {
        this.setModel(tableModel(list));
    }
}
