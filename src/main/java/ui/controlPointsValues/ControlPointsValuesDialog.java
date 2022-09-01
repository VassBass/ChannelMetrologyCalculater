package ui.controlPointsValues;

import converters.ConverterUI;
import model.ControlPointsValues;
import model.Measurement;
import repository.impl.SensorRepositorySQLite;
import ui.controlPointsValues.complexElements.*;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPointsValuesDialog extends JDialog {
    private static final String CANCEL = "Відмінити";
    private static final String SAVE = "Зберегти";

    private final ControlPointsListDialog parent;
    private final ControlPointsValues values;

    private TopPanel topPanel;
    private ControlPointsPanel controlPointsPanel;
    private JButton btnCancel, btnSave;

    ControlPointsValuesDialog(ControlPointsListDialog parent, ControlPointsValues values){
        super(parent, values.getSensorType(), true);

        this.parent = parent;
        this.values = values;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        String measurement = SensorRepositorySQLite.getInstance().getMeasurement(this.values.getSensorType());
        if (measurement.equals(Measurement.TEMPERATURE)){
            this.controlPointsPanel = new TemperaturePanel(this.values.getRangeMin(), this.values.getRangeMax());
        }else if (measurement.equals(Measurement.PRESSURE)){
            this.controlPointsPanel = new PressurePanel(this.values.getRangeMin(), this.values.getRangeMax());
        }else if (measurement.equals(Measurement.CONSUMPTION)){
            this.controlPointsPanel = new ConsumptionPanel(this.values.getRangeMin(), this.values.getRangeMax());
        }

        this.topPanel = new TopPanel(this.controlPointsPanel);
        this.btnCancel = new DefaultButton(CANCEL);
        this.btnSave = new DefaultButton(SAVE);

        if (this.values.getValues() != null) this.setValues();
    }

    private void setValues(){
        this.topPanel.setRangeMin(this.values.getRangeMin());
        this.topPanel.setRangeMax(this.values.getRangeMax());
        this.controlPointsPanel.setValues(this.values.getValues());
    }

    private void setReactions(){
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this.closeDialog);

        this.btnCancel.addActionListener(this.clickCancel);
        this.btnSave.addActionListener(this.clickSave);
    }

    private void build(){
        this.setSize(600,150);
        this.setLocation(ConverterUI.POINT_CENTER(MainScreen.getInstance(), this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            parent.setVisible(true);
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            values.setValues(controlPointsPanel.getValues());
            values.setRangeMin(topPanel.getRangeMin());
            values.setRangeMax(topPanel.getRangeMax());
            //Application.context.controlPointsValuesService.put(values);
            parent.setList(values.getSensorType());
            dispose();
            parent.setVisible(true);
        }
    };

    private final WindowListener closeDialog = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            dispose();
            parent.setVisible(true);
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(topPanel, new Cell(0,0.025));
            this.add(controlPointsPanel, new Cell(1,0.95));
            JPanel btnPanel = new JPanel();
            btnPanel.add(btnCancel);
            btnPanel.add(btnSave);
            this.add(btnPanel, new Cell(2,0.025));
        }
        private class Cell extends GridBagConstraints {
            protected Cell(int y, double weight){
                this.fill = BOTH;
                this.weightx = 1D;
                this.gridx = 0;
                this.gridy = y;
                this.weighty = weight;
            }
        }
    }
}
