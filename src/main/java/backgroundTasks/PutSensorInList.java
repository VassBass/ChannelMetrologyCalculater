package backgroundTasks;

import application.Application;
import model.Sensor;
import ui.model.LoadDialog;
import ui.sensorsList.SensorsListDialog;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import java.awt.*;

public class PutSensorInList extends SwingWorker<Void, Void> {
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
    protected Void doInBackground() throws Exception {
        if (this.oldSensor == null) {
            Application.context.sensorsController.add(this.newSensor);
        } else {
            Application.context.sensorsController.set(this.oldSensor, this.newSensor);
            Application.context.channelsController.changeSensor(this.oldSensor, this.newSensor);
        }
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.dialog.dispose();
        String m;
        if (this.oldSensor == null){
            m = "ПВП успішно додано до списку!";
        }else {
            m = "ПВП успішно змінено!";
        }
        mainDialog.mainTable.update();
        JOptionPane.showMessageDialog(this.mainDialog, m, SUCCESS, JOptionPane.INFORMATION_MESSAGE);
    }
}