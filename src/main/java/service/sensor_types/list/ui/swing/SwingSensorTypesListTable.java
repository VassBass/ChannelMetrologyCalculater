package service.sensor_types.list.ui.swing;

import service.sensor_types.list.ui.SensorTypesListTable;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SwingSensorTypesListTable extends JTable implements SensorTypesListTable {
    private static final String COLUMN_TYPE = "Тип ПВП";

    public SwingSensorTypesListTable(){
        super();

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private DefaultTableModel tableModel(List<String> sensorTypesList){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {
                COLUMN_TYPE
        };
        model.setColumnIdentifiers(columnsHeader);

        for (String type : sensorTypesList) {
            model.addRow(new String[] { type });
        }

        return model;
    }

    @Nullable
    @Override
    public String getSelectedType() {
        int row = this.getSelectedRow();
        return row < 0 ? null : this.getModel().getValueAt(row, 0).toString();
    }

    @Override
    public void setTypeList(List<String> typeList) {
        this.setModel(tableModel(typeList));
    }
}
