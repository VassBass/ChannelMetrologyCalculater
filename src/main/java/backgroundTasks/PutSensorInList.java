package backgroundTasks;

import model.Sensor;
import service.impl.ChannelServiceImpl;
import service.impl.ControlPointsValuesServiceImpl;
import service.impl.SensorServiceImpl;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;
import ui.sensorsList.SensorsListDialog;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class PutSensorInList extends SwingWorker<Boolean, Void> {
    private static final String SUCCESS = "Успіх";
    private static final String ALL = "Всі";

    private final SensorsListDialog mainDialog;
    private final SensorInfoDialog dialog;
    private final Sensor newSensor;
    private Sensor oldSensor;
    private final LoadDialog loadDialog;

    public PutSensorInList(SensorsListDialog mainDialog, SensorInfoDialog dialog, Sensor sensor){
        super();
        this.mainDialog = mainDialog;
        this.dialog = dialog;
        this.newSensor = sensor;
        this.loadDialog = new LoadDialog(dialog);
    }

    public void start(Sensor oldSensor){
        this.oldSensor = oldSensor;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.dispose();
                loadDialog.setVisible(true);
            }
        });
        this.execute();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        if (this.oldSensor == null) {
            return SensorServiceImpl.getInstance().add(this.newSensor);
        } else {
            SensorServiceImpl.getInstance().set(this.oldSensor, this.newSensor);
            if (!this.oldSensor.isMatch(this.newSensor, Sensor.RANGE, Sensor.VALUE)) {
                ChannelServiceImpl.getInstance().changeSensor(this.oldSensor, this.newSensor, Sensor.MEASUREMENT, Sensor.RANGE, Sensor.VALUE);
            }
            if (!this.oldSensor.getType().equals(this.newSensor.getType())){
                ControlPointsValuesServiceImpl.getInstance().changeSensorType(this.oldSensor.getType(), this.newSensor.getType());
            }
            return true;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.dialog.dispose();
        try {
            if (this.get()) {
                String m = this.oldSensor == null ?  "ПВП успішно додано до списку!" : "ПВП успішно змінено!";
                JOptionPane.showMessageDialog(MainScreen.getInstance(), m, SUCCESS, JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String measurement = mainDialog.getMeasurement().equals(ALL) ? null : mainDialog.getMeasurement();
        this.mainDialog.update(measurement);
    }
}