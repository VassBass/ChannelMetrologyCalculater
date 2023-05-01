package service.measurement.list.ui.swing;

import service.measurement.list.ui.MeasurementListFactorTable;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.Objects;

public class SwingMeasurementListFactorTable extends JTable implements MeasurementListFactorTable {

    public SwingMeasurementListFactorTable(){
        super();

        this.getTableHeader().setReorderingAllowed(false);
    }

    private DefaultTableModel tableModel(@Nullable String sourceValue, @Nullable Map<String, Double> valueList){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        if (Objects.nonNull(sourceValue)) {
            String[] columnsHeader = new String[]{ String.format("1 %s = ", sourceValue), "..." };
            model.setColumnIdentifiers(columnsHeader);
        }

        if (Objects.nonNull(valueList)) {
            for (Map.Entry<String, Double> entry : valueList.entrySet()) {
                String[] data = new String[2];
                data[0] = entry.getKey();
                data[1] = String.valueOf(entry.getValue());
                model.addRow(data);
            }
        }

        return model;
    }

    @Override
    public void setFactorList(@Nullable String sourceValue, @Nullable Map<String, Double> factorList) {
        this.setModel(tableModel(sourceValue, factorList));
    }
}
