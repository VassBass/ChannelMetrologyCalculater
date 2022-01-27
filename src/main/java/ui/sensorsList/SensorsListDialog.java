package ui.sensorsList;

import application.Application;
import converters.ConverterUI;
import model.Channel;
import model.Sensor;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SensorsListDialog extends JDialog {
    private static final String SENSORS_LIST = "Список первинних вимірювальних пристроїв";
    private static final String ADD = "Додати";
    private static final String REMOVE = "Видалити";
    private static final String DETAILS = "Детальніше";
    private static final String CANCEL = "Відміна";

    private final MainScreen mainScreen;

    private SensorsListTable mainTable;
    private JButton buttonAdd, buttonRemove, buttonDetails, buttonCancel;

    public SensorsListDialog(MainScreen mainScreen){
        super(mainScreen, SENSORS_LIST, true);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.mainTable = new SensorsListTable();

        this.buttonAdd = new DefaultButton(ADD);
        this.buttonRemove = new DefaultButton(REMOVE);
        this.buttonDetails = new DefaultButton(DETAILS);
        this.buttonCancel = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonRemove.addActionListener(this.clickRemove);
        this.buttonDetails.addActionListener(this.clickDetails);
        this.buttonAdd.addActionListener(this.clickAdd);
    }

    private void build() {
        this.setSize(800,500);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    public void updateMain(ArrayList<Channel>channels){
        this.mainScreen.setChannelsList(channels);
    }

    public void update(){
        this.mainTable.update();
        this.refresh();
    }

    private void refresh(){
        this.setVisible(false);
        this.setVisible(true);
    }

    public Sensor getSensor(){
        int index = this.mainTable.getSelectedRow();
        if (index < 0){
            return null;
        }else {
            return Application.context.sensorsController.get(index);
        }
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainTable.getSelectedRow() >= 0) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new SensorRemoveDialog(SensorsListDialog.this).setVisible(true);
                    }
                });
            }
        }
    };

    private final ActionListener clickDetails = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainTable.getSelectedRow() != -1){
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new SensorInfoDialog(SensorsListDialog.this, Application.context.sensorsController.get(mainTable.getSelectedRow())).setVisible(true);
                    }
                });
            }
        }
    };

    private final ActionListener clickAdd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new SensorInfoDialog(SensorsListDialog.this, null).setVisible(true);
                }
            });
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(new JScrollPane(mainTable), new Cell(0,1,4,0.9));
            this.add(buttonCancel, new Cell(0,2,1,0.1));
            this.add(buttonRemove, new Cell(1,2,1,0.1));
            this.add(buttonDetails, new Cell(2,2,1,0.1));
            this.add(buttonAdd, new Cell(3,2,1,0.1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width, double weight){
                super();

                this.fill = BOTH;
                this.weightx = 1D;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
                this.weighty = weight;
            }
        }
    }
}