package backgroundTasks.tasks_for_import;

import constants.Strings;
import model.Calibrator;
import support.*;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportedCalibrators extends SwingWorker<Void, Void> {
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
        //Lists.saveCalibratorsListToFile(this.calibrators);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        //this.mainScreen.update(Lists.channels(), false, null, null);
        JOptionPane.showMessageDialog(this.mainScreen, Strings.IMPORT_SUCCESS, Strings.IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}
