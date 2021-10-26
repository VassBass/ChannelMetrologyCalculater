package backgroundTasks;

import support.Channel;
import support.Lists;
import support.Sensor;
import support.Worker;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportData extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;
    private final ArrayList<Channel> channels;
    private final ArrayList<Sensor>sensors;
    private final ArrayList<Worker>persons;
    private final ArrayList<String>departments, areas, processes, installations;

    public SaveImportData(MainScreen mainScreen,
                          ArrayList<Sensor>sensors, ArrayList<Channel>channels, ArrayList<Worker>persons,
                          ArrayList<String>departments, ArrayList<String>areas, ArrayList<String>processes, ArrayList<String>installations){
        super();

        this.mainScreen = mainScreen;

        this.sensors = sensors;
        this.channels = channels;
        this.persons = persons;
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
        Lists.saveSensorsListToFile(this.sensors);
        Lists.saveChannelsListToFile(this.channels);
        Lists.savePersonsListToFile(this.persons);
        Lists.saveDepartmentsListToFile(this.departments);
        Lists.saveAreasListToFile(this.areas);
        Lists.saveProcessesListToFile(this.processes);
        Lists.saveInstallationsListToFile(this.installations);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.mainScreen.update(this.channels, false, null, null);
    }
}
