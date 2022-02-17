package ui.controlPointsValues;

import application.Application;
import constants.MeasurementConstants;
import converters.ConverterUI;
import model.ControlPointsValues;
import model.Sensor;
import ui.controlPointsValues.complexElements.*;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPointsValuesAddDialog extends JDialog {
    private static final String CANCEL = "Відмінити";
    private static final String SAVE = "Зберегти";

    private final ControlPointsListDialog parent;
    private final ControlPointsValues values;

    private TopPanel topPanel;
    private ControlPointsPanel controlPointsPanel;
    private JButton btnCancel, btnSave;

    ControlPointsValuesAddDialog(ControlPointsListDialog parent, ControlPointsValues values){
        super(parent, values.getSensorType(), true);

        this.parent = parent;
        this.values = values;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        String measurement = Application.context.sensorService.getMeasurement(this.values.getSensorType());
        if (measurement.equals(MeasurementConstants.TEMPERATURE.getValue())){
            this.controlPointsPanel = new TemperaturePanel();
        }else if (measurement.equals(MeasurementConstants.PRESSURE.getValue())){
            this.controlPointsPanel = new PressurePanel();
        }else if (measurement.equals(MeasurementConstants.CONSUMPTION.getValue())){
            this.controlPointsPanel = new ConsumptionPanel();
        }

        this.topPanel = new TopPanel(this.controlPointsPanel);
        this.btnCancel = new DefaultButton(CANCEL);
        this.btnSave = new DefaultButton(SAVE);
    }

    private void setReactions(){
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(closeDialog);

        this.btnCancel.addActionListener(clickCancel);
    }

    private void build(){
        this.setSize(600,300);
        this.setLocation(ConverterUI.POINT_CENTER(Application.context.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
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
