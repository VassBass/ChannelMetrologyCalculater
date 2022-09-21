package ui.importData.compareCalibrators.complexElements;

import model.Calibrator;
import ui.importData.compareCalibrators.CompareCalibratorsDialog;
import ui.model.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class NewCalibratorsTable extends Table<Calibrator> {
    private static final String NAME = "Назва";

    public NewCalibratorsTable(final CompareCalibratorsDialog parent, final List<Calibrator> calibrators){
        super(tableModel(calibrators));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener select = e -> {
            if (NewCalibratorsTable.this.getSelectedRow() != -1) {
                parent.cancelSelection(parent.CHANGED_SENSORS_TABLE);
                parent.hideOldCalibratorInfo();
                parent.showNewCalibratorInfo(calibrators.get(NewCalibratorsTable.this.getSelectedRow()));
            }
        };
        this.getSelectionModel().addListSelectionListener(select);
        this.setCenterAlignment();
    }

    private void setCenterAlignment(){
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        for (int x=0;x<this.getColumnCount();x++){
            this.getColumnModel().getColumn(x).setCellRenderer(centerRender);
        }
    }

    @Override
    public void setList(List<Calibrator> calibratorList){
        this.setModel(tableModel(calibratorList));
        this.setCenterAlignment();
    }

    private static DefaultTableModel tableModel(List<Calibrator> calibratorList){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {NAME};
        model.setColumnIdentifiers(columnsHeader);

        for (Calibrator calibrator : calibratorList) {
            String[] data = new String[1];
            data[0] = calibrator.getName();
            model.addRow(data);
        }

        return model;
    }
}