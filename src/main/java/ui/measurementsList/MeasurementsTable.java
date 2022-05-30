package ui.measurementsList;

import application.Application;
import model.Measurement;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class MeasurementsTable extends JTable {
    private static final String MEASUREMENT_VALUE = "Вимірювальна величина";

    private String actualMeasurement = Measurement.TEMPERATURE;
    private boolean crunch = false;

    private final MeasurementsListDialog parentDialog;

    public MeasurementsTable(final MeasurementsListDialog parentDialog){
        super(tableModel(Measurement.TEMPERATURE));
        this.parentDialog = parentDialog;

        this.getTableHeader().setReorderingAllowed(false);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener select = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (crunch){
                    crunch = false;
                    return;
                }
                if (MeasurementsTable.this.getSelectedRow() != -1) {
                    String field = (String) MeasurementsTable.this.getValueAt(MeasurementsTable.this.getSelectedRow(), 0);
                    parentDialog.updateValuesPanel(field);
                    parentDialog.setEnabledInListPanel_btnRemove(true);
                }else {
                    parentDialog.updateValuesPanel(null);
                    parentDialog.setEnabledInListPanel_btnRemove(false);
                }
                crunch = true;
            }
        };
        this.getSelectionModel().addListSelectionListener(select);
    }

    public void setMeasurementValues(String measurementName){
        this.actualMeasurement = measurementName;
        this.setModel(tableModel(measurementName));
        this.revalidate();
    }

    public String getMeasurementValue(){
        return this.getSelectedRow() >= 0 ? (String) this.getValueAt(this.getSelectedRow(), 0) : null;
    }

    private static DefaultTableModel tableModel(String measurementName){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[]columnsHeader = new String[] {MEASUREMENT_VALUE};
        model.setColumnIdentifiers(columnsHeader);

        ArrayList<Measurement>measurements = Application.context.measurementService.getMeasurements(measurementName);
        for (Measurement measurement : measurements) {
            String[] data = new String[1];
            data[0] = measurement.getValue();

            model.addRow(data);
        }

        return model;
    }
}
