package backgroundTasks.data_import;

import application.Application;
import model.Calibrator;
import model.Channel;
import model.Sensor;
import model.Worker;
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
        if (this.sensors != null && !this.sensors.isEmpty()) {
            Application.context.sensorsController.rewriteAll(this.sensors);
        }
        if (this.channels != null && !this.channels.isEmpty()) {
            Application.context.channelsController.rewriteAll(this.channels);
        }
        if (this.persons != null && !this.persons.isEmpty()) {
            Application.context.personsController.rewriteAll(this.persons);
        }
        if (this.calibrators != null && !this.calibrators.isEmpty()) {
            Application.context.calibratorsController.rewriteAll(this.calibrators);
        }
        if (this.departments != null && !this.departments.isEmpty()) {
            Application.context.departmentsController.rewriteAll(this.departments);
        }
        if (this.areas != null && !this.areas.isEmpty()) {
            Application.context.areasController.rewriteAll(this.areas);
        }
        if (this.processes != null && !this.processes.isEmpty()) {
            Application.context.processesController.rewriteAll(this.processes);
        }
        if (this.installations != null && !this.installations.isEmpty()) {
            Application.context.installationsController.rewriteAll(this.installations);
        }
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.mainScreen.setChannelsList(Application.context.channelsController.getAll());
        JOptionPane.showMessageDialog(this.mainScreen, IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}