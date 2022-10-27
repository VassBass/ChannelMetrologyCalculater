package backgroundTasks;

import model.Calibrator;
import repository.CalibratorRepository;
import repository.ChannelRepository;
import repository.impl.CalibratorRepositorySQLite;
import repository.impl.ChannelRepositorySQLite;
import ui.mainScreen.MainScreen;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveImportedCalibrators extends SwingWorker<Void, Void> {
    private static final Logger LOGGER = Logger.getLogger(SaveImportedCalibrators.class.getName());

    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final List<Calibrator> newCalibrators, calibratorsForChange;
    private final DialogLoading loadDialog;
    private final File importFile;

    private final CalibratorRepository calibratorRepository = CalibratorRepositorySQLite.getInstance();
    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public SaveImportedCalibrators(List<Calibrator>newCalibrators, List<Calibrator> calibratorsForChange, File file){
        super();
        this.newCalibrators = newCalibrators;
        this.calibratorsForChange = calibratorsForChange;
        this.loadDialog = new DialogLoading(MainScreen.getInstance());
        this.importFile = file;
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
    }

    @Override
    protected Void doInBackground() throws Exception {
        calibratorRepository.importData(this.newCalibrators, this.calibratorsForChange);
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
                new Importer(this.importFile, 1).execute();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }
}