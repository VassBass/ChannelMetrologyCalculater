package backgroundTasks;

import model.Measurement;
import service.ChannelSorter;
import service.impl.CalibratorServiceImpl;
import service.impl.ChannelServiceImpl;
import service.impl.MeasurementServiceImpl;
import service.impl.SensorServiceImpl;
import ui.measurementsList.MeasurementsListDialog;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
        measurement = MeasurementServiceImpl.getInstance().get(measurementValue);
        if (MeasurementServiceImpl.getInstance().isLastInMeasurement(measurementValue)) {
            LAST_MEASUREMENT_MESSAGE = this.lastSensorMessageGenerator(measurement.getName());
            return false;
        }else {
            MeasurementServiceImpl.getInstance().remove(measurement);
            ChannelServiceImpl.getInstance().removeByMeasurementValue(measurementValue);
            CalibratorServiceImpl.getInstance().removeByMeasurementValue(measurementValue);
            SensorServiceImpl.getInstance().removeMeasurementValue(measurementValue);
            return true;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                this.dialog.updateMeasurementsList(measurement.getName());
                if (ChannelSorter.getInstance().isOn()){
                    this.dialog.updateMain(ChannelSorter.getInstance().getCurrent());
                }else {
                    this.dialog.updateMain(new ArrayList<>(ChannelServiceImpl.getInstance().getAll()));
                }
            }else {
                JOptionPane.showMessageDialog(dialog, LAST_MEASUREMENT_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, ERROR_MESSAGE, ERROR,JOptionPane.ERROR_MESSAGE);
        }
    }
}
