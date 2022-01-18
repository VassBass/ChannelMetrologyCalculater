package backgroundTasks.data_import;

import application.Application;
import model.Calibrator;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportedCalibrators extends SwingWorker<Void, Void> {
    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final MainScreen mainScreen;
    private final ArrayList<Calibrator> calibrators;
    private final LoadDialog loadDialog;

    public SaveImportedCalibrators(MainScreen mainScreen, ArrayList<Calibrator>calibrators){
        super();
        this.mainScreen = mainScreen;
        this.calibrators = calibrators;
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
        Application.context.calibratorsController.rewriteAll(this.calibrators);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.mainScreen.setChannelsList(Application.context.channelsController.getAll());
        JOptionPane.showMessageDialog(this.mainScreen, IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}