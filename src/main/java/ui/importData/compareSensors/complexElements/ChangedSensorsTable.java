package ui.importData.compareSensors.complexElements;

import model.Sensor;
import ui.importData.compareSensors.CompareSensorsDialog;
import ui.model.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ChangedSensorsTable extends Table<Sensor> {
    private static final String NAME = "Назва";

    public ChangedSensorsTable(final CompareSensorsDialog parent,
                               final ArrayList<Sensor> sensorsForChange, final ArrayList<Sensor>changedSensors){
        super(tableModel(sensorsForChange));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener select = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = ChangedSensorsTable.this.getSelectedRow();
                if (index != -1) {
                    Sensor newSensor = sensorsForChange.get(index);
                    Sensor oldSensor = changedSensors.get(index);
                    parent.cancelSelection(parent.NEW_SENSORS_TABLE);
                    parent.showNewSensorInfo(newSensor);
                    parent.showOldSensorInfo(oldSensor);
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
    public void setList(List<Sensor> sensorList){
        this.setModel(tableModel(sensorList));
        this.setCenterAlignment();
    }

    private static DefaultTableModel tableModel(List<Sensor> sensorList){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {NAME};
        model.setColumnIdentifiers(columnsHeader);

        for (Sensor sensor : sensorList) {
            String[] data = new String[1];
            data[0] = sensor.getName();
            model.addRow(data);
        }

        return model;
    }
}