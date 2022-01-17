package ui.calibratorsList;

import application.Application;
import model.Calibrator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Objects;

public class CalibratorsListTable extends JTable {
    private static final String NAME = "Назва";
    private static final String TYPE = "Тип";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";

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

        String[]columnsHeader = new String[] {NAME, TYPE, TYPE_OF_MEASUREMENT};
        model.setColumnIdentifiers(columnsHeader);

        ArrayList<Calibrator> calibrators = Application.context.calibratorsController.getAll();
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