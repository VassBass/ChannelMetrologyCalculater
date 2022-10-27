package backgroundTasks;

import model.Measurement;
import ui.measurementsList.MeasurementsListDialog;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;

public class ChangeMeasurementValue extends SwingWorker<Void, Void> {
    private final JDialog parent;
    private final MeasurementsListDialog dialog;
    private final DialogLoading loadDialog;
    private final Measurement oldMeasurement, newMeasurement;

    public ChangeMeasurementValue(JDialog parent, MeasurementsListDialog dialog, Measurement oldMeasurement, Measurement newMeasurement){
        super();
        this.parent = parent;
        this.dialog = dialog;
        this.oldMeasurement = oldMeasurement;
        this.newMeasurement = newMeasurement;
        this.loadDialog = new DialogLoading(parent);
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
        //Application.context.measurementService.changeInCurrentThread(oldMeasurement, newMeasurement);
        //Application.context.channelService.changeMeasurementValueInCurrentThread(oldMeasurement.getValue(), newMeasurement.getValue());
        //Application.context.calibratorService.changeMeasurementValueInCurrentThread(oldMeasurement.getValue(), newMeasurement.getValue());
        //Application.context.sensorService.changeMeasurementValueInCurrentThread(oldMeasurement.getValue(), newMeasurement.getValue());
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.parent.dispose();
        this.dialog.updateMeasurementsList(newMeasurement.getName());
    }
}
