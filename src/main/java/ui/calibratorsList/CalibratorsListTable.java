package ui.calibratorsList;

import constants.Strings;
import support.Calibrator;
import support.Lists;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Objects;

public class CalibratorsListTable extends JTable {

    public CalibratorsListTable(){
        super(tableModel());

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private static DefaultTableModel tableModel(){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {Strings._NAME, Strings.TYPE, Strings.TYPE_OF_MEASUREMENT};
        model.setColumnIdentifiers(columnsHeader);

        ArrayList<Calibrator> calibrators = Lists.calibrators();
        for (Calibrator calibrator : Objects.requireNonNull(calibrators)) {
            String[] data = new String[3];
            data[0] = calibrator.getName();
            data[1] = calibrator.getType();
            data[2] = calibrator.getMeasurement();

            model.addRow(data);
        }

        return model;
    }

    public void update(){
        this.setModel(tableModel());
    }
}
