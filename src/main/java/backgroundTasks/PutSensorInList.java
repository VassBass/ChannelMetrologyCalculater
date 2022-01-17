package backgroundTasks;

import application.Application;
import model.Sensor;
import ui.model.LoadDialog;
import ui.sensorsList.SensorsListDialog;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import java.awt.*;

public class PutSensorInList extends SwingWorker<Boolean, Void> {
    private static final String SUCCESS = "Успіх";
    private static final String ERROR = "Помилка";

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
    }

    public void start(Sensor oldSensor){
        this.oldSensor = oldSensor;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    /*return:   true - ПИП успешно изменен/добавлен
                false - ПИП с таким названием(newSensor.name) уже существует в списке
     */
    @Override
    protected Boolean doInBackground() throws Exception {
        if (this.oldSensor == null){
            Application.context.sensorsController.add(this.newSensor);
        }else {
            Application.context.sensorsController.set(this.oldSensor, this.newSensor);
            Application.context.channelsController.changeSensor(this.oldSensor, this.newSensor);
        }
        return true;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                this.dialog.dispose();
                String m;
                if (this.oldSensor == null){
                    m = "ПВП успішно додано до списку!";
                }else {
                    m = "ПВП успішно змінено!";
                }
                JOptionPane.showMessageDialog(this.mainDialog, m, SUCCESS, JOptionPane.INFORMATION_MESSAGE);
                mainDialog.mainTable.update();
            }else {
                String m = "ПВП з такою назвою вже існує в списку!";
                JOptionPane.showMessageDialog(this.mainDialog, m, ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String er = "Відбулася помилка! Спробуйте будь-ласка ще раз.";
            JOptionPane.showMessageDialog(dialog, er, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}