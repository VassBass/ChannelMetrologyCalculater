package ui.importData.compareSensors;

import backgroundTasks.SaveImportedSensors;
import converters.ConverterUI;
import model.Sensor;
import ui.importData.compareSensors.complexElements.ChangedSensorsTable;
import ui.importData.compareSensors.complexElements.NewSensorsTable;
import ui.importData.compareSensors.complexElements.SensorInfoWindow;
import ui.mainScreen.MainScreen;
import ui.model.ButtonCell;
import ui.model.DefaultButton;
import ui.model.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class CompareSensorsDialog extends JDialog {
    private static final String IMPORT = "Імпорт ПВП";
    private static final String IMPORTED_SENSOR = "Імпортуємий ПВП";
    private static final String OLD_SENSOR = "ПВП з поточного списку";
    private static final String NEW_SENSORS = "Нові ПВП";
    private static final String SENSORS_FOR_CHANGE = "ПВП на заміну";
    private static final String REMOVE = "Видалити";
    private static final String CONFIRM = "Підтвердити";

    public final boolean NEW_SENSORS_TABLE = true;
    public final boolean CHANGED_SENSORS_TABLE = false;
    private File importFile;

    private final ArrayList<Sensor>newSensors, sensorsForChange, changedSensors;

    private JWindow newSensorInfo, oldSensorInfo;

    private ButtonCell titleNewSensors, titleSensorsForChange;
    private Table<Sensor> newSensorsTable, changedSensorsTable;
    private JButton removeFromNew, removeFromChanges, btnConfirmNew, btnConfirmChanges;

    public CompareSensorsDialog(ArrayList<Sensor>newSensorsList,ArrayList<Sensor>sensorsForChange, ArrayList<Sensor>changedSensorsList){
        super(MainScreen.getInstance(), IMPORT, true);
        this.newSensors = newSensorsList;
        this.sensorsForChange = sensorsForChange;
        this.changedSensors = changedSensorsList;

        this.createElements();
        this.setReactions();
        this.build();
    }

    public CompareSensorsDialog(ArrayList<Sensor>newSensorsList,ArrayList<Sensor>sensorsForChange, ArrayList<Sensor>changedSensorsList, File file){
        super(MainScreen.getInstance(), IMPORT, true);
        this.newSensors = newSensorsList;
        this.sensorsForChange = sensorsForChange;
        this.changedSensors = changedSensorsList;
        this.importFile = file;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        this.titleNewSensors = new ButtonCell(true, NEW_SENSORS);
        this.titleSensorsForChange = new ButtonCell(true, SENSORS_FOR_CHANGE);
        this.newSensorsTable = new NewSensorsTable(CompareSensorsDialog.this, this.newSensors);
        this.changedSensorsTable = new ChangedSensorsTable(CompareSensorsDialog.this,this.sensorsForChange, this.changedSensors);
        this.removeFromNew = new DefaultButton(REMOVE);
        this.removeFromNew.setEnabled(false);
        this.removeFromChanges = new DefaultButton(REMOVE);
        this.removeFromChanges.setEnabled(false);
        this.btnConfirmNew = new DefaultButton(CONFIRM);
        this.btnConfirmChanges = new DefaultButton(CONFIRM);

        if (this.newSensors.isEmpty()){
            this.newSensorsTable.setEnabled(false);
            this.removeFromNew.setEnabled(false);
            this.btnConfirmNew.setEnabled(false);
        }
        if (this.sensorsForChange.isEmpty()){
            this.changedSensorsTable.setEnabled(false);
            this.removeFromChanges.setEnabled(false);
            this.btnConfirmChanges.setEnabled(false);
        }
    }

    private void setReactions(){
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this.windowListener);
        this.addComponentListener(this.moveWindow);

        this.removeFromNew.addActionListener(this.clickRemoveFromNew);
        this.removeFromChanges.addActionListener(this.clickRemoveFromChanged);
        this.btnConfirmNew.addActionListener(this.clickConfirmNew);
        this.btnConfirmChanges.addActionListener(this.clickConfirmChanges);
    }

    private void build(){
        this.setSize(400,400);
        this.setResizable(false);
        this.setLocation(ConverterUI.POINT_CENTER(MainScreen.getInstance(), this));
        this.setContentPane(new MainPanel());
    }

    public void showNewSensorInfo(Sensor sensor){
        if (this.newSensorInfo != null) this.newSensorInfo.dispose();

        this.newSensorInfo = new SensorInfoWindow(IMPORTED_SENSOR,CompareSensorsDialog.this,sensor);
        this.newSensorInfo.setLocation(ConverterUI.LEFT_FROM_PARENT(CompareSensorsDialog.this, this.newSensorInfo));
        this.newSensorInfo.setVisible(true);
    }

    private void hideNewSensorInfo(){
        if (this.newSensorInfo != null) this.newSensorInfo.dispose();
    }

    public void showOldSensorInfo(Sensor sensor){
        if (this.oldSensorInfo != null) this.oldSensorInfo.dispose();

        this.oldSensorInfo = new SensorInfoWindow(OLD_SENSOR,CompareSensorsDialog.this,sensor);
        this.oldSensorInfo.setLocation(ConverterUI.RIGHT_FROM_PARENT(CompareSensorsDialog.this, this.oldSensorInfo));
        this.oldSensorInfo.setVisible(true);
    }

    public void hideOldSensorInfo(){
        if (this.oldSensorInfo != null) this.oldSensorInfo.dispose();
    }

    public void cancelSelection(boolean table){
        if (table == NEW_SENSORS_TABLE){
            this.newSensorsTable.clearSelection();
            this.removeFromNew.setEnabled(false);
            if (this.changedSensorsTable.getSelectedRow() >= 0) this.removeFromChanges.setEnabled(true);
        }else {
            this.changedSensorsTable.clearSelection();
            this.removeFromChanges.setEnabled(false);
            if (this.newSensorsTable.getSelectedRow() >= 0) this.removeFromNew.setEnabled(true);
        }
    }

    private final WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            int result = JOptionPane.showConfirmDialog(CompareSensorsDialog.this,
                    "Припинити імпорт?", IMPORT, JOptionPane.OK_CANCEL_OPTION);
            if (result == 0){
                dispose();
            }else {
                setVisible(true);
            }
        }
    };

    private final ComponentListener moveWindow = new ComponentAdapter() {
        @Override
        public void componentMoved(ComponentEvent e) {
            if (newSensorInfo != null && newSensorInfo.isVisible()){
                newSensorInfo.setLocation(ConverterUI.LEFT_FROM_PARENT(CompareSensorsDialog.this, newSensorInfo));
            }
            if (oldSensorInfo != null && oldSensorInfo.isVisible()){
                oldSensorInfo.setLocation(ConverterUI.RIGHT_FROM_PARENT(CompareSensorsDialog.this, oldSensorInfo));
            }
        }
    };

    private final ActionListener clickRemoveFromNew = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            newSensors.remove(newSensorsTable.getSelectedRow());
            hideNewSensorInfo();
            cancelSelection(NEW_SENSORS_TABLE);
            newSensorsTable.setList(newSensors);
        }
    };

    private final ActionListener clickRemoveFromChanged = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = changedSensorsTable.getSelectedRow();
            sensorsForChange.remove(index);
            changedSensors.remove(index);
            hideNewSensorInfo();
            hideOldSensorInfo();
            cancelSelection(CHANGED_SENSORS_TABLE);
            changedSensorsTable.setList(sensorsForChange);
        }
    };

    private final ActionListener clickConfirmNew = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnConfirmChanges.isEnabled()){
                if (newSensorsTable.getSelectedRow() >= 0) {
                    hideNewSensorInfo();
                    cancelSelection(NEW_SENSORS_TABLE);
                }
                removeFromNew.setEnabled(false);
                btnConfirmNew.setEnabled(false);
                newSensorsTable.setEnabled(false);
            }else {
                dispose();
                new SaveImportedSensors(newSensors, sensorsForChange, importFile).execute();
            }
        }
    };

    private final ActionListener clickConfirmChanges = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnConfirmNew.isEnabled()){
                if (changedSensorsTable.getSelectedRow() >= 0) {
                    hideNewSensorInfo();
                    hideOldSensorInfo();
                    cancelSelection(CHANGED_SENSORS_TABLE);
                }
                removeFromChanges.setEnabled(false);
                btnConfirmChanges.setEnabled(false);
                changedSensorsTable.setEnabled(false);
            }else {
                dispose();
                new SaveImportedSensors(newSensors, sensorsForChange, importFile).execute();
            }
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(titleNewSensors, new Cell(0,0,2,false));
            this.add(new JScrollPane(newSensorsTable), new Cell(0,1,2,true));
            this.add(removeFromNew, new Cell(0,5,1,false));
            this.add(btnConfirmNew, new Cell(1,5,1,false));
            this.add(titleSensorsForChange, new Cell(0,6,2,false));
            this.add(new JScrollPane(changedSensorsTable), new Cell(0,7,2,true));
            this.add(removeFromChanges, new Cell(0,11,1,false));
            this.add(btnConfirmChanges, new Cell(1,11,1,false));
        }

        private class Cell extends GridBagConstraints{
            protected Cell(int x, int y, int width, boolean table){
                super();
                this.fill = BOTH;
                this.weightx = 1.0;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
                if (table){
                    this.weighty = 0.4;
                }else {
                    this.weighty = 0.05;
                }
            }
        }
    }
}