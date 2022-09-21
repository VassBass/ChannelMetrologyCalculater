package backgroundTasks;

import model.Sensor;
import repository.ChannelRepository;
import repository.SensorRepository;
import repository.impl.ChannelRepositorySQLite;
import repository.impl.SensorRepositorySQLite;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveImportedSensors extends SwingWorker<Void, Void> {
    private static final Logger LOGGER = Logger.getLogger(SaveImportedSensors.class.getName());

    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final List<Sensor> newSensors, sensorsForChange;
    private final LoadDialog loadDialog;
    private final File importFile;

    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();
    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public SaveImportedSensors(List<Sensor>newSensors, List<Sensor> sensorsForChange, File file){
        super();
        this.newSensors = newSensors;
        this.sensorsForChange = sensorsForChange;
        this.loadDialog = new LoadDialog(MainScreen.getInstance());
        this.importFile = file;
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
    }

    @Override
    protected Void doInBackground() throws Exception {
        sensorRepository.importData(this.newSensors, this.sensorsForChange);
        channelRepository.changeSensors(this.sensorsForChange);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        if (this.importFile == null) {
            MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
            JOptionPane.showMessageDialog(MainScreen.getInstance(), IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
        }else {
            try {
                new Importer(this.importFile, 2).execute();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error: ", e);
            }
        }
    }
}