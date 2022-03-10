package backgroundTasks;

import application.Application;
import model.Sensor;
import ui.model.LoadDialog;
import ui.sensorsList.SensorsListDialog;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class PutSensorInList extends SwingWorker<Boolean, Void> {
    private static final String SUCCESS = "Успіх";

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

    public void start(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
        this.execute();
    }

    public void start(Sensor oldSensor){
        this.oldSensor = oldSensor;
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
        if (this.oldSensor == null) {
            return Application.context.sensorService.add(this.newSensor) != null;
        } else {
            Application.context.sensorService.setInCurrentThread(this.oldSensor, this.newSensor);
            Application.context.channelService.changeSensorInCurrentThread(this.oldSensor, this.newSensor);
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
                JOptionPane.showMessageDialog(Application.context.mainScreen, m, SUCCESS, JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        mainDialog.update();
    }
}