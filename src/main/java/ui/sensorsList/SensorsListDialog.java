package ui.sensorsList;

import model.Channel;
import model.Sensor;
import repository.impl.MeasurementRepositorySQLite;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static ui.UI_Constants.POINT_CENTER;

public class SensorsListDialog extends JDialog {
    private static final String SENSORS_LIST = "Список первинних вимірювальних пристроїв";
    private static final String ADD = "Додати";
    private static final String REMOVE = "Видалити";
    private static final String DETAILS = "Детальніше";
    private static final String CANCEL = "Відміна";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String ALL = "Всі";

    private final MainScreen mainScreen;
    private MainPanel mainPanel;

    private SensorsListTable mainTable;
    private JButton buttonAdd, buttonRemove, buttonDetails, buttonCancel;
    private JComboBox<String>measurements;

    public SensorsListDialog(MainScreen mainScreen){
        super(mainScreen, SENSORS_LIST, true);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.mainTable = new SensorsListTable();

        String[]buffer = MeasurementRepositorySQLite.getInstance().getAllNames();
        String[]m = new String[buffer.length + 1];
        int index = 0;
        m[index] = ALL;
        for (String s : buffer){
            m[++index] = s;
        }
        this.measurements = new JComboBox<>(m);
        TitledBorder border = BorderFactory.createTitledBorder(TYPE_OF_MEASUREMENT);
        this.measurements.setBorder(border);

        this.buttonAdd = new DefaultButton(ADD);
        this.buttonRemove = new DefaultButton(REMOVE);
        this.buttonDetails = new DefaultButton(DETAILS);
        this.buttonCancel = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.measurements.addItemListener(changeMeasurement);

        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonRemove.addActionListener(this.clickRemove);
        this.buttonDetails.addActionListener(this.clickDetails);
        this.buttonAdd.addActionListener(this.clickAdd);
    }

    private void build() {
        this.setSize(800,500);
        this.setLocation(POINT_CENTER(this.mainScreen, this));

        this.mainPanel = new MainPanel();
        this.setContentPane(this.mainPanel);
    }

    public void updateMain(ArrayList<Channel>channels){
        this.mainScreen.setChannelsList(channels);
    }

    public void update(String measurement){
        this.mainTable.update(measurement);
        if (measurement == null) measurement = ALL;
        this.measurements.setSelectedItem(measurement);
        this.mainPanel.revalidate();
    }

    public String getMeasurement(){
        if (this.measurements.getSelectedItem() != null){
            return this.measurements.getSelectedItem().toString();
        } else return null;
    }

    public Sensor getSensor(){
        int index = this.mainTable.getSelectedRow();
        if (index < 0){
            return null;
        }else {
            //return Application.context.sensorService.get(index);
            return null;
        }
    }

    private final ActionListener clickCancel = e -> dispose();

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainTable.getSelectedRow() >= 0) {
                EventQueue.invokeLater(() -> new SensorRemoveDialog(SensorsListDialog.this).setVisible(true));
            }
        }
    };

    private final ActionListener clickDetails = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainTable.getSelectedRow() != -1){
                EventQueue.invokeLater(() -> {
                    //new SensorInfoDialog(SensorsListDialog.this, Application.context.sensorService.get(mainTable.getSelectedRow())).setVisible(true);
                });
            }
        }
    };

    private final ActionListener clickAdd = e -> EventQueue.invokeLater(() -> new SensorInfoDialog(SensorsListDialog.this, null).setVisible(true));

    private final ItemListener changeMeasurement = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED){
                if (measurements.getSelectedItem() == null || measurements.getSelectedItem().toString().equals(ALL)){
                    update((String) null);
                }else update(measurements.getSelectedItem().toString());
            }
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(measurements, new Cell(1,0,2,0.001));

            this.add(new JScrollPane(mainTable), new Cell(0,1,4,0.998));

            this.add(buttonCancel, new Cell(0,2,1,0.001));
            this.add(buttonRemove, new Cell(1,2,1,0.001));
            this.add(buttonDetails, new Cell(2,2,1,0.001));
            this.add(buttonAdd, new Cell(3,2,1,0.001));
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