package ui.controlPointsValues;

import converters.ConverterUI;
import model.ControlPointsValues;
import service.impl.ControlPointsValuesServiceImpl;
import service.impl.SensorServiceImpl;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Objects;

public class ControlPointsListDialog extends JDialog {
    private static final String TITLE = "Контрольні точки";
    private static final String CANCEL = "Відміна";
    private static final String CHANGE = "Змінити";
    private static final String ADD = "Додати";
    private static final String REMOVE = "Видалити";
    private static final String CLEAR = "Очистити";

    private JComboBox<String>sensorsTypes;
    public ControlPointsValuesTable mainTable;
    private JButton btnCancel, btnChange, btnAdd, btnRemove, btnClear;

    public ControlPointsListDialog(Frame parent){
        super(parent, TITLE, true);

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        String[]sTypes = SensorServiceImpl.getInstance().getAllTypesWithoutROSEMOUNT();
        this.sensorsTypes = new JComboBox<>(sTypes);
        String sensorType = Objects.requireNonNull(this.sensorsTypes.getSelectedItem()).toString();
        this.mainTable = new ControlPointsValuesTable(new ArrayList<>(ControlPointsValuesServiceImpl.getInstance().getBySensorType(sensorType)));
        this.btnCancel = new DefaultButton(CANCEL);
        this.btnChange = new DefaultButton(CHANGE);
        this.btnAdd = new DefaultButton(ADD);
        this.btnRemove = new DefaultButton(REMOVE);
        this.btnClear = new DefaultButton(CLEAR);
    }

    private void setReactions(){
        this.sensorsTypes.addItemListener(this.changeSensorType);

        this.btnCancel.addActionListener(this.clickCancel);
        this.btnAdd.addActionListener(this.clickAdd);
        this.btnChange.addActionListener(this.clickChange);
        this.btnRemove.addActionListener(this.clickRemove);
        this.btnClear.addActionListener(this.clickClear);
    }

    private void build(){
        this.setSize(300,300);
        this.setLocation(ConverterUI.POINT_CENTER(MainScreen.getInstance(), this));
        this.setContentPane(new MainPanel());
    }

    public void setList(String sensorType){
        this.sensorsTypes.setSelectedItem(sensorType);
        mainTable.setList(new ArrayList<>(ControlPointsValuesServiceImpl.getInstance().getBySensorType(sensorType)));
    }

    private final ItemListener changeSensorType = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED){
                String sensorType = Objects.requireNonNull(sensorsTypes.getSelectedItem()).toString();
                mainTable.setList(new ArrayList<>(ControlPointsValuesServiceImpl.getInstance().getBySensorType(sensorType)));
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
                    new ControlPointsValuesDialog(ControlPointsListDialog.this, cpv).setVisible(true);
                }
            });
        }
    };

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = mainTable.getSelectedRow();
            if (index >= 0){
                String sensorType = Objects.requireNonNull(sensorsTypes.getSelectedItem()).toString();
                //ControlPointsValues cpv = Application.context.controlPointsValuesService.getControlPointsValues(sensorType, index);
                //new ControlPointsValuesDialog(ControlPointsListDialog.this, cpv).setVisible(true);
            }
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = mainTable.getSelectedRow();
            if (index >= 0){
                String sensorType = Objects.requireNonNull(sensorsTypes.getSelectedItem()).toString();
                //ControlPointsValues cpv = Application.context.controlPointsValuesService.getControlPointsValues(sensorType, index);
                String question = "Ви впевнені що хочете видалити контрольні точки в діапазоні "
                //        + "[" + cpv.getRangeMin() + "..." + cpv.getRangeMax() + "] "
                        + "для ПВП типу \"" + sensorType + "\"?";
                int answer = JOptionPane.showConfirmDialog(ControlPointsListDialog.this, question, REMOVE, JOptionPane.OK_CANCEL_OPTION);
                if (answer == 0){
                    //Application.context.controlPointsValuesService.remove(cpv);
                    setList(sensorType);
                }
            }
        }
    };

    private final ActionListener clickClear = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String sensorType = Objects.requireNonNull(sensorsTypes.getSelectedItem()).toString();
            String question = "Ви впевнені що хочете видалити ВСІ контрольні точки для ПВП типу \"" + sensorType + "\"?";
            int answer = JOptionPane.showConfirmDialog(ControlPointsListDialog.this, question, CLEAR, JOptionPane.OK_CANCEL_OPTION);
            if (answer == 0) {
                //Application.context.controlPointsValuesService.clear(sensorType);
                setList(sensorType);
            }
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(sensorsTypes, new Cell(0,0.025));
            this.add(new JScrollPane(mainTable), new Cell(1,0.95));
            JPanel btnPanel = new JPanel(new GridBagLayout());
            btnPanel.add(btnChange, new Cell(0,0,1));
            btnPanel.add(btnAdd, new Cell(1,0,1));
            btnPanel.add(btnClear, new Cell(0,1,1));
            btnPanel.add(btnRemove, new Cell(1,1,1));
            btnPanel.add(btnCancel, new Cell(0,2,2));
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

            protected Cell(int x, int y, int width){
                this.fill = BOTH;
                this.weightx = 1D;
                this.weighty = 1D;
                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}
