package backgroundTasks.controllers;

import model.Calibrator;
import support.Lists;
import ui.LoadDialog;
import ui.calibratorsList.CalibratorsListDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RemoveCalibrator extends SwingWorker<Void, Void> {
    private final CalibratorsListDialog parent;
    private final Calibrator calibrator;
    private final LoadDialog loadDialog;

    public RemoveCalibrator(CalibratorsListDialog parent, Calibrator calibrator){
        super();
        this.parent = parent;
        this.calibrator = calibrator;

        this.loadDialog = new LoadDialog(parent);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        ArrayList<Calibrator> calibrators = Lists.calibrators();
        if (calibrators != null) {
            for (int x = 0; x < calibrators.size(); x++) {
                if (calibrators.get(x).getName().equals(this.calibrator.getName())) {
                    calibrators.remove(x);
                    break;
                }
            }

            Lists.saveCalibratorsListToFile(calibrators);
        }
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.parent.mainTable.update();
    }
}
