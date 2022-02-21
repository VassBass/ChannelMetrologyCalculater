package ui.controlPointsValues;

import model.ControlPointsValues;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ControlPointsValuesTable extends JTable {

    public ControlPointsValuesTable(ArrayList<ControlPointsValues>list) {
        super(tableModel(list));

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCenterAlignment();
    }

    public void setList(ArrayList<ControlPointsValues> list) {
        this.setModel(tableModel(list));
        this.setCenterAlignment();
    }

    private static DefaultTableModel tableModel(ArrayList<ControlPointsValues> cpvList){
        String channelRange = "Діапазон вимірювального каналу";
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        model.setColumnIdentifiers(new String[] {channelRange});

        for (ControlPointsValues cpv : cpvList) {
            String range = cpv.getRangeMin() + " ... " + cpv.getRangeMax();
            String[] data = new String[]{range};

            model.addRow(data);
        }

        return model;
    }

    private void setCenterAlignment(){
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        for (int x=0;x<this.getColumnCount();x++){
            this.getColumnModel().getColumn(x).setCellRenderer(centerRender);
        }
    }
}