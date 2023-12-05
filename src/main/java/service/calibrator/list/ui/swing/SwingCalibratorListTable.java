package service.calibrator.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.dto.Calibrator;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import service.calibrator.list.ui.CalibratorListTable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwingCalibratorListTable extends JTable implements CalibratorListTable {

    public SwingCalibratorListTable(@Nonnull RepositoryFactory repositoryFactory){
        super();
        CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.setModel(tableModel(new ArrayList<>(calibratorRepository.getAll())));
    }

    private DefaultTableModel tableModel(List<Calibrator> calibratorsList){
        Map<String, String> labels = Labels.getRootLabels();

        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {
                labels.get(RootLabelName.NAME),
                labels.get(RootLabelName.MODEL),
                labels.get(RootLabelName.TYPE_OF_MEASUREMENT)
        };
        model.setColumnIdentifiers(columnsHeader);

        for (Calibrator calibrator : calibratorsList) {
            String[] data = new String[3];
            data[0] = calibrator.getName();
            data[1] = calibrator.getType();
            data[2] = calibrator.getMeasurementName();
            model.addRow(data);
        }

        return model;
    }

    @Override
    public void setCalibratorList(List<Calibrator> calibratorList) {
        this.setModel(tableModel(calibratorList));
    }

    @Nullable
    @Override
    public String getSelectedCalibratorName() {
        int row = this.getSelectedRow();
        return row < 0 ?
                null :
                this.getModel().getValueAt(row, 0).toString();
    }
}
