package backgroundTasks;

import application.Application;
import model.Sensor;
import ui.model.LoadDialog;
import ui.sensorsList.SensorsListDialog;

import javax.swing.*;
import java.awt.*;

public class RemoveSensor extends SwingWorker<Boolean, Void> {
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
        int index = this.dialog.mainTable.getSelectedRow();
        Sensor sensor = Application.context.sensorsController.get(index);
        if (Application.context.sensorsController.isLastInMeasurement(sensor)) {
            LAST_SENSOR_MESSAGE = this.lastSensorMessageGenerator(sensor.getMeasurement());
            return false;
        }else {
            Application.context.sensorsController.remove(sensor);
            Application.context.channelsController.removeBySensor(sensor);
            return true;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                this.dialog.mainTable.update();
                if (Application.context.channelSorter.isOn()){
                    this.dialog.updateMain(Application.context.channelSorter.getCurrent());
                }else {
                    this.dialog.updateMain(Application.context.channelsController.getAll());
                }
            }else {
                JOptionPane.showMessageDialog(dialog, LAST_SENSOR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, ERROR_MESSAGE, ERROR,JOptionPane.ERROR_MESSAGE);
        }
    }
}