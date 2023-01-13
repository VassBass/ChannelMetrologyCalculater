package ui.sensorsList;

import model.Sensor;
import service.repository.repos.sensor.SensorRepository;
import service.repository.repos.sensor.SensorRepositorySQLite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Objects;

public class SensorsListTable extends JTable {
    private static final String NAME = "Назва";
    private static final String TYPE = "Тип";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";

    private static final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();

    public SensorsListTable(){
        super(tableModel(null));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private static DefaultTableModel tableModel(String measurement){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {NAME, TYPE, TYPE_OF_MEASUREMENT};
        model.setColumnIdentifiers(columnsHeader);

        ArrayList<Sensor>sensors = measurement == null ?
                new ArrayList<>(sensorRepository.getAll())
                : new ArrayList<>(sensorRepository.getAll(measurement));
        for (Sensor sensor : Objects.requireNonNull(sensors)) {
            String[] data = new String[3];
            data[0] = sensor.getName();
            data[1] = sensor.getType();
            data[2] = sensor.getMeasurement();

            model.addRow(data);
        }

        return model;
    }

    public void update(String measurement){
        this.setModel(tableModel(measurement));
    }
}