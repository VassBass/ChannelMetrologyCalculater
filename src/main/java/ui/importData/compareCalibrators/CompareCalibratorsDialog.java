package ui.importData.compareCalibrators;

import backgroundTasks.SaveImportedCalibrators;
import model.Calibrator;
import ui.importData.compareCalibrators.complexElements.CalibratorInfoWindow;
import ui.importData.compareCalibrators.complexElements.ChangedCalibratorsTable;
import ui.importData.compareCalibrators.complexElements.NewCalibratorsTable;
import ui.mainScreen.MainScreen;
import ui.model.ButtonCell;
import ui.model.DefaultButton;
import ui.model.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

import static ui.UI_Constants.*;

public class CompareCalibratorsDialog extends JDialog {
    private static final String IMPORT = "Імпорт калібраторів";
    private static final String IMPORTED_SENSOR = "Імпортуємий калібратор";
    private static final String OLD_SENSOR = "Калібратор з поточного списку";
    private static final String NEW_SENSORS = "Нові калібратори";
    private static final String SENSORS_FOR_CHANGE = "Калібратор на заміну";
    private static final String REMOVE = "Видалити";
    private static final String CONFIRM = "Підтвердити";

    public final boolean NEW_SENSORS_TABLE = true;
    public final boolean CHANGED_SENSORS_TABLE = false;

    private final List<Calibrator> newCalibrators, calibratorsForChange, changedCalibrators;
    private File importFile = null;

    private JWindow newCalibratorInfo, oldCalibratorInfo;

    private ButtonCell titleNewCalibrators, titleCalibratorsForChange;
    private Table<Calibrator> newCalibratorsTable, changedCalibratorsTable;
    private JButton removeFromNew, removeFromChanges, btnConfirmNew, btnConfirmChanges;

    public CompareCalibratorsDialog(List<Calibrator>newCalibratorsList, List<Calibrator>calibratorsForChange, List<Calibrator>changedCalibratorsList){
        super(MainScreen.getInstance(), IMPORT, true);
        this.newCalibrators = newCalibratorsList;
        this.calibratorsForChange = calibratorsForChange;
        this.changedCalibrators = changedCalibratorsList;

        this.createElements();
        this.setReactions();
        this.build();
    }

    public CompareCalibratorsDialog(List<Calibrator>newCalibratorsList, List<Calibrator>calibratorsForChange, List<Calibrator>changedCalibratorsList, File file){
        super(MainScreen.getInstance(), IMPORT, true);
        this.newCalibrators = newCalibratorsList;
        this.calibratorsForChange = calibratorsForChange;
        this.changedCalibrators = changedCalibratorsList;
        this.importFile = file;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements(){
        this.titleNewCalibrators = new ButtonCell(true, NEW_SENSORS);
        this.titleCalibratorsForChange = new ButtonCell(true, SENSORS_FOR_CHANGE);
        this.newCalibratorsTable = new NewCalibratorsTable(CompareCalibratorsDialog.this, this.newCalibrators);
        this.changedCalibratorsTable = new ChangedCalibratorsTable(CompareCalibratorsDialog.this,this.calibratorsForChange, this.changedCalibrators);
        this.removeFromNew = new DefaultButton(REMOVE);
        this.removeFromNew.setEnabled(false);
        this.removeFromChanges = new DefaultButton(REMOVE);
        this.removeFromChanges.setEnabled(false);
        this.btnConfirmNew = new DefaultButton(CONFIRM);
        this.btnConfirmChanges = new DefaultButton(CONFIRM);

        if (this.newCalibrators.isEmpty()){
            this.newCalibratorsTable.setEnabled(false);
            this.removeFromNew.setEnabled(false);
            this.btnConfirmNew.setEnabled(false);
        }
        if (this.calibratorsForChange.isEmpty()){
            this.changedCalibratorsTable.setEnabled(false);
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
        this.setLocation(POINT_CENTER(MainScreen.getInstance(), this));
        this.setContentPane(new CompareCalibratorsDialog.MainPanel());
    }

    public void showNewCalibratorInfo(Calibrator calibrator){
        if (this.newCalibratorInfo != null) this.newCalibratorInfo.dispose();

        this.newCalibratorInfo = new CalibratorInfoWindow(IMPORTED_SENSOR,CompareCalibratorsDialog.this,calibrator);
        this.newCalibratorInfo.setLocation(LEFT_FROM_PARENT(CompareCalibratorsDialog.this, this.newCalibratorInfo));
        this.newCalibratorInfo.setVisible(true);
    }

    private void hideNewCalibratorInfo(){
        if (this.newCalibratorInfo != null) this.newCalibratorInfo.dispose();
    }

    public void showOldCalibratorInfo(Calibrator calibrator){
        if (this.oldCalibratorInfo != null) this.oldCalibratorInfo.dispose();

        this.oldCalibratorInfo = new CalibratorInfoWindow(OLD_SENSOR,CompareCalibratorsDialog.this,calibrator);
        this.oldCalibratorInfo.setLocation(RIGHT_FROM_PARENT(CompareCalibratorsDialog.this));
        this.oldCalibratorInfo.setVisible(true);
    }

    public void hideOldCalibratorInfo(){
        if (this.oldCalibratorInfo != null) this.oldCalibratorInfo.dispose();
    }

    public void cancelSelection(boolean table){
        if (table == NEW_SENSORS_TABLE){
            this.newCalibratorsTable.clearSelection();
            this.removeFromNew.setEnabled(false);
            if (this.changedCalibratorsTable.getSelectedRow() >= 0) this.removeFromChanges.setEnabled(true);
        }else {
            this.changedCalibratorsTable.clearSelection();
            this.removeFromChanges.setEnabled(false);
            if (this.newCalibratorsTable.getSelectedRow() >= 0) this.removeFromNew.setEnabled(true);
        }
    }

    private final WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            int result = JOptionPane.showConfirmDialog(CompareCalibratorsDialog.this,
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
            if (newCalibratorInfo != null && newCalibratorInfo.isVisible()){
                newCalibratorInfo.setLocation(LEFT_FROM_PARENT(CompareCalibratorsDialog.this, newCalibratorInfo));
            }
            if (oldCalibratorInfo != null && oldCalibratorInfo.isVisible()){
                oldCalibratorInfo.setLocation(RIGHT_FROM_PARENT(CompareCalibratorsDialog.this));
            }
        }
    };

    private final ActionListener clickRemoveFromNew = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            newCalibrators.remove(newCalibratorsTable.getSelectedRow());
            hideNewCalibratorInfo();
            cancelSelection(NEW_SENSORS_TABLE);
            newCalibratorsTable.setList(newCalibrators);
        }
    };

    private final ActionListener clickRemoveFromChanged = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = changedCalibratorsTable.getSelectedRow();
            calibratorsForChange.remove(index);
            changedCalibrators.remove(index);
            hideNewCalibratorInfo();
            hideOldCalibratorInfo();
            cancelSelection(CHANGED_SENSORS_TABLE);
            changedCalibratorsTable.setList(calibratorsForChange);
        }
    };

    private final ActionListener clickConfirmNew = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnConfirmChanges.isEnabled()){
                if (newCalibratorsTable.getSelectedRow() >= 0) {
                    hideNewCalibratorInfo();
                    cancelSelection(NEW_SENSORS_TABLE);
                }
                removeFromNew.setEnabled(false);
                btnConfirmNew.setEnabled(false);
                newCalibratorsTable.setEnabled(false);
            }else {
                dispose();
                new SaveImportedCalibrators(newCalibrators, calibratorsForChange, importFile).execute();
            }
        }
    };

    private final ActionListener clickConfirmChanges = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnConfirmNew.isEnabled()){
                if (changedCalibratorsTable.getSelectedRow() >= 0) {
                    hideNewCalibratorInfo();
                    hideOldCalibratorInfo();
                    cancelSelection(CHANGED_SENSORS_TABLE);
                }
                removeFromChanges.setEnabled(false);
                btnConfirmChanges.setEnabled(false);
                changedCalibratorsTable.setEnabled(false);
            }else {
                dispose();
                new SaveImportedCalibrators(newCalibrators, calibratorsForChange, importFile).execute();
            }
        }
    };

    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(titleNewCalibrators, new CompareCalibratorsDialog.MainPanel.Cell(0,0,2,false));
            this.add(new JScrollPane(newCalibratorsTable), new CompareCalibratorsDialog.MainPanel.Cell(0,1,2,true));
            this.add(removeFromNew, new CompareCalibratorsDialog.MainPanel.Cell(0,5,1,false));
            this.add(btnConfirmNew, new CompareCalibratorsDialog.MainPanel.Cell(1,5,1,false));
            this.add(titleCalibratorsForChange, new CompareCalibratorsDialog.MainPanel.Cell(0,6,2,false));
            this.add(new JScrollPane(changedCalibratorsTable), new CompareCalibratorsDialog.MainPanel.Cell(0,7,2,true));
            this.add(removeFromChanges, new CompareCalibratorsDialog.MainPanel.Cell(0,11,1,false));
            this.add(btnConfirmChanges, new CompareCalibratorsDialog.MainPanel.Cell(1,11,1,false));
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