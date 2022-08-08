package backgroundTasks;

import model.Calibrator;
import service.impl.CalibratorServiceImpl;
import service.impl.ChannelServiceImpl;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveImportedCalibrators extends SwingWorker<Void, Void> {
    private static final Logger LOGGER = Logger.getLogger(SaveImportedCalibrators.class.getName());

    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final ArrayList<Calibrator>newCalibrators, calibratorsForChange;
    private final LoadDialog loadDialog;
    private final File importFile;

    public SaveImportedCalibrators(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator> calibratorsForChange, File file){
        super();
        this.newCalibrators = newCalibrators;
        this.calibratorsForChange = calibratorsForChange;
        this.loadDialog = new LoadDialog(MainScreen.getInstance());
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
        CalibratorServiceImpl.getInstance().importData(this.newCalibrators, this.calibratorsForChange);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        if (this.importFile == null) {
            MainScreen.getInstance().setChannelsList(new ArrayList<>(ChannelServiceImpl.getInstance().getAll()));
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