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

public class NewCalibratorsTable extends Table<Calibrator> {
    private static final String NAME = "Назва";

    public NewCalibratorsTable(final CompareCalibratorsDialog parent, final ArrayList<Calibrator> calibrators){
        super(tableModel(calibrators));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener select = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (NewCalibratorsTable.this.getSelectedRow() != -1) {
                    parent.cancelSelection(parent.CHANGED_SENSORS_TABLE);
                    parent.hideOldCalibratorInfo();
                    parent.showNewCalibratorInfo(calibrators.get(NewCalibratorsTable.this.getSelectedRow()));
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