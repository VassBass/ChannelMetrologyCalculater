package service.measurement.list.ui.swing;

import service.measurement.list.MeasurementListManager;
import service.measurement.list.ui.MeasurementListValueTable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SwingMeasurementListValueTable extends JTable implements MeasurementListValueTable {
     private static final String COLUMN_VALUE = "Величина";

    public SwingMeasurementListValueTable(@Nonnull MeasurementListManager manager){
        super();

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.getSelectionModel().addListSelectionListener(e ->
                manager.selectMeasurementValue());
    }

    private DefaultTableModel tableModel(List<String> valueList){
        DefaultTableModel model = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };

        String[]columnsHeader = new String[] { COLUMN_VALUE };
        model.setColumnIdentifiers(columnsHeader);

        for (String val : valueList) {
            model.addRow(new String[] { val });
        }

        return model;
    }

    @Nullable
    @Override
    public String getSelectedValue() {
        int row = this.getSelectedRow();
        return row < 0 ? null : this.getValueAt(row, 0).toString();
    }

    @Override
    public void setValueList(List<String> valueList) {
        this.setModel(tableModel(valueList));
    }
}
