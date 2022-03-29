package backgroundTasks;

import application.Application;
import model.Sensor;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveImportedSensors extends SwingWorker<Void, Void> {
    private static final Logger LOGGER = Logger.getLogger(SaveImportedSensors.class.getName());

    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final MainScreen mainScreen;
    private final ArrayList<Sensor>newSensors, sensorsForChange;
    private final LoadDialog loadDialog;
    private final File importFile;

    public SaveImportedSensors(ArrayList<Sensor>newSensors, ArrayList<Sensor> sensorsForChange, File file){
        super();
        this.mainScreen = Application.context.mainScreen;
        this.newSensors = newSensors;
        this.sensorsForChange = sensorsForChange;
        this.loadDialog = new LoadDialog(mainScreen);
        this.importFile = file;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        Application.context.sensorService.importData(this.newSensors, this.sensorsForChange);
        Application.context.channelService.changeSensorsInCurrentThread(this.sensorsForChange);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        if (this.importFile == null) {
            this.mainScreen.setChannelsList(Application.context.channelService.getAll());
            JOptionPane.showMessageDialog(this.mainScreen, IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
        }else {
            try {
                new Importer(this.importFile, 2).execute();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error: ", e);
            }
        }
    }
}