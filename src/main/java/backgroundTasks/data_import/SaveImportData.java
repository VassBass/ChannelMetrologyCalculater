package backgroundTasks.data_import;

import application.Application;
import model.Calibrator;
import model.Channel;
import model.Person;
import model.Sensor;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportData extends SwingWorker<Void, Void> {
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";
    private static final String IMPORT = "Імпорт";

    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;
    private ArrayList<Channel> channels;
    private final ArrayList<Sensor>sensors;
    private ArrayList<Person>persons;
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
                          ArrayList<Sensor>sensors, ArrayList<Channel>channels, ArrayList<Person>persons, ArrayList<Calibrator>calibrators,
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
        if (this.sensors != null && !this.sensors.isEmpty()) {
            Application.context.sensorService.rewriteInCurrentThread(this.sensors);
        }
        if (this.channels != null && !this.channels.isEmpty()) {
            Application.context.channelService.rewriteInCurrentThread(this.channels);
        }
        if (this.persons != null && !this.persons.isEmpty()) {
            Application.context.personService.rewriteInCurrentThread(this.persons);
        }
        if (this.calibrators != null && !this.calibrators.isEmpty()) {
            Application.context.calibratorService.rewriteInCurrentThread(this.calibrators);
        }
        if (this.departments != null && !this.departments.isEmpty()) {
            Application.context.departmentService.rewriteInCurrentThread(this.departments);
        }
        if (this.areas != null && !this.areas.isEmpty()) {
            Application.context.areaService.rewriteInCurrentThread(this.areas);
        }
        if (this.processes != null && !this.processes.isEmpty()) {
            Application.context.processService.rewriteInCurrentThread(this.processes);
        }
        if (this.installations != null && !this.installations.isEmpty()) {
            Application.context.installationService.rewriteInCurrentThread(this.installations);
        }
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        Application.context.channelSorter.setOff();
        System.out.println(Application.context);
        this.mainScreen.setChannelsList(Application.context.channelService.getAll());
        JOptionPane.showMessageDialog(this.mainScreen, IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}