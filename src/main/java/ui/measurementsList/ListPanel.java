package ui.measurementsList;

import backgroundTasks.RemoveMeasurement;
import service.impl.MeasurementServiceImpl;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ListPanel extends JPanel {
    private static final String ADD = "Додати";
    private static final String REMOVE = "Видалити";
    private static final String CHANGE = "Змінити";

    private JButton btn_add, btn_remove, btn_change;
    private JComboBox<String>measurementsNames;
    private MeasurementsTable measurementsTable;

    private final MeasurementsListDialog parentDialog;

    public ListPanel(MeasurementsListDialog parentDialog){
        super(new GridBagLayout());
        this.parentDialog = parentDialog;

        this.createElements();
        this.build();
        this.setReactions();
    }

    private void createElements(){
        this.measurementsNames = new JComboBox<>(MeasurementServiceImpl.getInstance().getAllNames());
        this.measurementsTable = new MeasurementsTable(this.parentDialog);
        this.btn_add = new DefaultButton(ADD);
        this.btn_remove = new DefaultButton(REMOVE);
        this.btn_remove.setEnabled(false);
        this.btn_change = new DefaultButton(CHANGE);
        this.btn_change.setEnabled(false);
    }

    private void build(){
        this.add(measurementsNames, new Cell(0, 0.05));
        this.add(new JScrollPane(measurementsTable), new Cell(1, 0.9));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btn_remove);
        buttonsPanel.add(btn_change);
        buttonsPanel.add(btn_add);
        this.add(buttonsPanel, new Cell(2, 0.05));
    }

    private void setReactions(){
        measurementsNames.addItemListener(changeMeasurement);
        btn_remove.addActionListener(clickRemove);
        btn_add.addActionListener(clickAdd);
        btn_change.addActionListener(clickChange);
    }

    public void setEnabled_btnRemove(boolean enabled){
        this.btn_remove.setEnabled(enabled);
        this.btn_change.setEnabled(enabled);
    }

    private final ActionListener clickAdd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new AddMeasurementDialog(parentDialog).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final String measurementValue = measurementsTable.getMeasurementValue();
            if (measurementValue != null){
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int result = JOptionPane.showConfirmDialog(parentDialog,
                                "!*ВАЖЛИВО*! При видаленні величини будуть також видалені всі канали та калібратори які її використовують!",
                                "Видалити \"" + measurementValue + "\"?", JOptionPane.OK_CANCEL_OPTION);
                        if (result == 0){
                            new RemoveMeasurement(parentDialog).start();
                        }
                    }
                });
            }
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ChangeMeasurementDialog(parentDialog).setVisible(true);
                }
            });
        }
    };

    public String getSelectedMeasurementName(){
        if (this.measurementsNames.getSelectedItem() != null){
            return this.measurementsNames.getSelectedItem().toString();
        }else {
            return null;
        }
    }

    public String getSelectedMeasurementValue(){
        return this.measurementsTable.getMeasurementValue();
    }

    private final ItemListener changeMeasurement = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && measurementsNames.getSelectedItem() != null){
                String selected = measurementsNames.getSelectedItem().toString();
                measurementsTable.setMeasurementValues(selected);
            }
        }
    };

    public void updateMeasurementList(String measurementName){
        if (measurementName != null) {
            measurementsTable.setMeasurementValues(measurementName);
        }
    }

    private static class Cell extends GridBagConstraints {
        Cell(int y, double weight){
            super();

            this.fill = HORIZONTAL;
            this.gridx = 0;

            this.weighty = weight;
            this.gridy = y;
        }
    }
}
