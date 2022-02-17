package ui.controlPointsValues;

import application.Application;
import converters.ConverterUI;
import model.ControlPointsValues;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

public class ControlPointsListDialog extends JDialog {
    private static final String TITLE = "Контрольні точки";
    private static final String CANCEL = "Відміна";
    private static final String CHANGE = "Змінити";
    private static final String ADD = "Додати";

    private JComboBox<String>sensorsTypes;
    public ControlPointsValuesTable mainTable;
    private JButton btnCancel, btnChange, btnAdd;

    public ControlPointsListDialog(Frame parent){
        super(parent, TITLE, true);

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        String[]sTypes = Application.context.sensorService.getAllTypesWithoutROSEMOUNT();
        this.sensorsTypes = new JComboBox<>(sTypes);
        String sensorType = Objects.requireNonNull(this.sensorsTypes.getSelectedItem()).toString();
        this.mainTable = new ControlPointsValuesTable(Application.context.controlPointsValuesService.getBySensorType(sensorType));
        this.btnCancel = new DefaultButton(CANCEL);
        this.btnChange = new DefaultButton(CHANGE);
        this.btnAdd = new DefaultButton(ADD);
    }

    private void setReactions(){
        this.sensorsTypes.addItemListener(this.changeSensorType);

        this.btnCancel.addActionListener(this.clickCancel);
        this.btnAdd.addActionListener(this.clickAdd);
    }

    private void build(){
        this.setSize(300,300);
        this.setLocation(ConverterUI.POINT_CENTER(Application.context.mainScreen, this));
        this.setContentPane(new MainPanel());
    }

    private final ItemListener changeSensorType = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED){
                String sensorType = Objects.requireNonNull(sensorsTypes.getSelectedItem()).toString();
                mainTable.setList(Application.context.controlPointsValuesService.getBySensorType(sensorType));
            }
        }
    };

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickAdd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setVisible(false);
                    String sensorType = Objects.requireNonNull(sensorsTypes.getSelectedItem()).toString();
                    ControlPointsValues cpv = new ControlPointsValues(sensorType,0D,100D,null);
                    new ControlPointsValuesAddDialog(ControlPointsListDialog.this, cpv).setVisible(true);
                }
            });
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(sensorsTypes, new Cell(0,0.025));
            this.add(new JScrollPane(mainTable), new Cell(1,0.95));
            JPanel btnPanel = new JPanel();
            btnPanel.add(btnCancel);
            btnPanel.add(btnChange);
            btnPanel.add(btnAdd);
            this.add(btnPanel, new Cell(2,0.025));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int y, double weight){
                super();

                this.fill = BOTH;
                this.weightx = 1D;
                this.gridx = 0;
                this.insets = new Insets(5,0,5,0);

                this.gridy = y;
                this.weighty = weight;
            }
        }
    }
}
