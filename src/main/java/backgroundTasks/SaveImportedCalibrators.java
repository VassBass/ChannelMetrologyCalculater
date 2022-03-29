package backgroundTasks;

import application.Application;
import model.Calibrator;
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

    private final MainScreen mainScreen;
    private final ArrayList<Calibrator>newCalibrators, calibratorsForChange;
    private final LoadDialog loadDialog;
    private final File importFile;

    public SaveImportedCalibrators(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator> calibratorsForChange, File file){
        super();
        this.mainScreen = Application.context.mainScreen;
        this.newCalibrators = newCalibrators;
        this.calibratorsForChange = calibratorsForChange;
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
        Application.context.calibratorService.importData(this.newCalibrators, this.calibratorsForChange);
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
                new Importer(this.importFile, 1).execute();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }
}