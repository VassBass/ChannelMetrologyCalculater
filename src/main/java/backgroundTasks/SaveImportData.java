package backgroundTasks;

import constants.Strings;
import support.*;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportData extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;
    private ArrayList<Channel> channels;
    private final ArrayList<Sensor>sensors;
    private ArrayList<Worker>persons;
    private ArrayList<Calibrator>calibrators;
    private ArrayList<String>departments, areas, processes, installations;

    public SaveImportData(MainScreen mainScreen, ArrayList<Sensor>sensors){
        super();

        this.mainScreen = mainScreen;

        this.sensors = sensors;

        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    public SaveImportData(MainScreen mainScreen,
                          ArrayList<Sensor>sensors, ArrayList<Channel>channels, ArrayList<Worker>persons, ArrayList<Calibrator>calibrators,
                          ArrayList<String>departments, ArrayList<String>areas, ArrayList<String>processes, ArrayList<String>installations){
        super();

        this.mainScreen = mainScreen;

        this.sensors = sensors;
        this.channels = channels;
        this.persons = persons;
        this.calibrators = calibrators;
        this.departments = departments;
        this.areas = areas;
        this.processes = processes;
        this.installations = installations;

        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (this.sensors != null) {
            Lists.saveSensorsListToFile(this.sensors);
        }
        if (this.channels != null) {
            Lists.saveChannelsListToFile(this.channels);
        }
        if (this.persons != null) {
            Lists.savePersonsListToFile(this.persons);
        }
        if (this.calibrators != null) {
            Lists.saveCalibratorsListToFile(this.calibrators);
        }
        if (this.departments != null) {
            Lists.saveDepartmentsListToFile(this.departments);
        }
        if (this.areas != null) {
            Lists.saveAreasListToFile(this.areas);
        }
        if (this.processes != null) {
            Lists.saveProcessesListToFile(this.processes);
        }
        if (this.installations != null) {
            Lists.saveInstallationsListToFile(this.installations);
        }
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        if (this.channels == null){
            this.channels = Lists.channels();
        }
        this.mainScreen.update(this.channels, false, null, null);
        JOptionPane.showMessageDialog(this.mainScreen, Strings.IMPORT_SUCCESS, Strings.IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}
