package ui.importData.compareCalibrators.complexElements;

import model.Calibrator;
import ui.importData.compareCalibrators.CompareCalibratorsDialog;
import ui.model.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ChangedCalibratorsTable extends Table<Calibrator> {
    private static final String NAME = "Назва";

    public ChangedCalibratorsTable(final CompareCalibratorsDialog parent,
                               final ArrayList<Calibrator> calibratorsForChange, final ArrayList<Calibrator>changedCalibrators){
        super(tableModel(calibratorsForChange));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener select = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = ChangedCalibratorsTable.this.getSelectedRow();
                if (index != -1) {
                    Calibrator newCalibrator = calibratorsForChange.get(index);
                    Calibrator oldCalibrator = changedCalibrators.get(index);
                    parent.cancelSelection(parent.NEW_SENSORS_TABLE);
                    parent.showNewCalibratorInfo(newCalibrator);
                    parent.showOldCalibratorInfo(oldCalibrator);
                }
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
    public void setList(ArrayList<Calibrator>calibratorList){
        this.setModel(tableModel(calibratorList));
        this.setCenterAlignment();
    }

    private static DefaultTableModel tableModel(ArrayList<Calibrator> calibratorList){
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
