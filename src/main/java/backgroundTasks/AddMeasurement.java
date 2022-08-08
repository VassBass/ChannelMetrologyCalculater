package backgroundTasks;

import model.Measurement;
import ui.measurementsList.MeasurementsListDialog;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;

public class AddMeasurement extends SwingWorker<Void, Void> {
    private final JDialog parent;
    private final MeasurementsListDialog dialog;
    private final LoadDialog loadDialog;
    private final Measurement measurement;

    public AddMeasurement(JDialog parent, MeasurementsListDialog dialog, Measurement measurement){
        super();
        this.parent = parent;
        this.dialog = dialog;
        this.measurement = measurement;
        this.loadDialog = new LoadDialog(parent);
    }

    public void start(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
        this.execute();
    }

    @Override
    protected Void doInBackground() throws Exception {
        //Application.context.measurementService.addInCurrentThread(measurement);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.parent.dispose();
        this.dialog.updateMeasurementsList(measurement.getName());
    }
}
