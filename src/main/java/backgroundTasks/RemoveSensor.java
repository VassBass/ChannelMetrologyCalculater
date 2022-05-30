package backgroundTasks;

import application.Application;
import model.Sensor;
import ui.model.LoadDialog;
import ui.sensorsList.SensorsListDialog;

import javax.swing.*;
import java.awt.*;

public class RemoveSensor extends SwingWorker<Boolean, Void> {
    private static final String ALL = "Всі";
    private String lastSensorMessageGenerator(String measurement){
        return "Неможливо видалити останній ПВП для вимірювання параметру \"" + measurement + "\"";
    }

    private static String LAST_SENSOR_MESSAGE;
    private static final String ERROR = "Помилка";
    private static final String ERROR_MESSAGE = "Помилка при видаленні ПВП!";

    private final SensorsListDialog dialog;
    private final LoadDialog loadDialog;

    public RemoveSensor(SensorsListDialog dialog){
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
        Sensor sensor = this.dialog.getSensor();
        if (Application.context.sensorService.isLastInMeasurement(sensor)) {
            LAST_SENSOR_MESSAGE = this.lastSensorMessageGenerator(sensor.getMeasurement());
            return false;
        }else {
            Application.context.sensorService.removeInCurrentThread(sensor);
            Application.context.channelService.removeBySensorInCurrentThread(sensor);
            Application.context.controlPointsValuesService.removeAllInCurrentThread(sensor.getType());
            return true;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                String measurement = dialog.getMeasurement().equals(ALL) ? null : dialog.getMeasurement();
                this.dialog.update(measurement);
                if (Application.context.channelSorter.isOn()){
                    this.dialog.updateMain(Application.context.channelSorter.getCurrent());
                }else {
                    this.dialog.updateMain(Application.context.channelService.getAll());
                }
            }else {
                JOptionPane.showMessageDialog(dialog, LAST_SENSOR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, ERROR_MESSAGE, ERROR,JOptionPane.ERROR_MESSAGE);
        }
    }
}