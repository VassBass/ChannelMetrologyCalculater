package backgroundTasks;

import application.Application;
import model.Measurement;
import ui.measurementsList.MeasurementsListDialog;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;

public class RemoveMeasurement extends SwingWorker<Boolean, Void> {
    private String lastSensorMessageGenerator(String measurement){
        return "Неможливо видалити останню величину для вимірювання параметру \"" + measurement + "\"";
    }

    private static String LAST_MEASUREMENT_MESSAGE;
    private static final String ERROR = "Помилка";
    private static final String ERROR_MESSAGE = "Помилка при видаленні величини!";

    private final MeasurementsListDialog dialog;
    private final LoadDialog loadDialog;
    private Measurement measurement;

    public RemoveMeasurement(MeasurementsListDialog dialog){
        super();
        this.dialog = dialog;
        this.loadDialog = new LoadDialog(dialog);
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
    protected Boolean doInBackground() throws Exception {
        String measurementValue = this.dialog.getSelectedMeasurementValue();
        measurement = Application.context.measurementService.get(measurementValue);
        if (Application.context.measurementService.isLastInMeasurement(measurementValue)) {
            LAST_MEASUREMENT_MESSAGE = this.lastSensorMessageGenerator(measurement.getName());
            return false;
        }else {
            Application.context.measurementService.delete(measurement);
            Application.context.channelService.removeByMeasurementValueInCurrentThread(measurementValue);
            Application.context.calibratorService.removeByMeasurementValueInCurrentThread(measurementValue);
            Application.context.sensorService.removeMeasurementValueInCurrentThread(measurementValue);
            return true;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                this.dialog.updateMeasurementsList(measurement.getName());
                if (Application.context.channelSorter.isOn()){
                    this.dialog.updateMain(Application.context.channelSorter.getCurrent());
                }else {
                    this.dialog.updateMain(Application.context.channelService.getAll());
                }
            }else {
                JOptionPane.showMessageDialog(dialog, LAST_MEASUREMENT_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, ERROR_MESSAGE, ERROR,JOptionPane.ERROR_MESSAGE);
        }
    }
}
