package ui.sensorsList;

import application.Application;
import constants.Strings;
import model.Sensor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Objects;

public class SensorsListTable extends JTable {

    public SensorsListTable(){
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

        ArrayList<Sensor>sensors = Application.context.sensorsController.getAll();
        for (Sensor sensor : Objects.requireNonNull(sensors)) {
            String[] data = new String[3];
            data[0] = sensor.getName();
            data[1] = sensor.getType();
            data[2] = sensor.getMeasurement();

            model.addRow(data);
        }

        return model;
    }

    public void update(){
        this.setModel(tableModel());
    }
}
