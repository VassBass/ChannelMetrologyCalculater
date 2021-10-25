package backgroundTasks;

import constants.Strings;
import support.Channel;
import support.Lists;
import support.Sensor;
import ui.LoadDialog;
import ui.sensorsList.SensorsListDialog;
import ui.sensorsList.sensorInfo.SensorInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class PutSensorInList extends SwingWorker<Boolean, Void> {
    private final SensorsListDialog mainDialog;
    private final SensorInfoDialog dialog;
    private final Sensor newSensor, oldSensor;
    private final LoadDialog loadDialog;


    public PutSensorInList(SensorsListDialog mainDialog, SensorInfoDialog dialog, Sensor newSensor, Sensor oldSensor){
        super();
        this.mainDialog = mainDialog;
        this.dialog = dialog;
        this.newSensor = newSensor;
        this.oldSensor = oldSensor;
        this.loadDialog = new LoadDialog(dialog);
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
        ArrayList<Sensor>sensors = Lists.sensors();
        if (this.oldSensor == null){
            for (Sensor sensor : Objects.requireNonNull(sensors)){
                if (sensor.getName().equals(this.newSensor.getName())){
                    return false;
                }
            }
            sensors.add(this.newSensor);
        }else {
            int oldIndex = -1;
            for (int x = 0; x < Objects.requireNonNull(sensors).size(); x++){
                if (sensors.get(x).getName().equals(this.oldSensor.getName())){
                    oldIndex = x;
                    break;
                }
            }
            for (int x=0;x<sensors.size();x++){
                if (sensors.get(x).getName().equals(newSensor.getName()) && x != oldIndex){
                    return false;
                }
            }
            sensors.set(oldIndex, this.newSensor);

            ArrayList<Channel>channels = Lists.channels();
            for (int x = 0; x< Objects.requireNonNull(channels).size(); x++){
                if (channels.get(x).getSensor().getName().equals(this.oldSensor.getName())){
                    channels.get(x).getSensor().setType(this.newSensor.getType());
                    channels.get(x).getSensor().setName(this.newSensor.getName());
                    channels.get(x).getSensor().setMeasurement(this.newSensor.getMeasurement());
                    channels.get(x).getSensor().setRange(this.newSensor.getRangeMin(), this.newSensor.getRangeMax());
                    channels.get(x).getSensor().setValue(this.newSensor.getValue());
                    channels.get(x).getSensor().setErrorFormula(this.newSensor.getErrorFormula());
                }
            }

            Lists.saveChannelsListToFile(channels);
        }
        Lists.saveSensorsListToFile(sensors);
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
                JOptionPane.showMessageDialog(this.mainDialog, m, Strings.SUCCESS, JOptionPane.INFORMATION_MESSAGE);
                mainDialog.mainTable.update();
            }else {
                String m = "ПВП з такою назвою вже існує в списку!";
                JOptionPane.showMessageDialog(this.mainDialog, m, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String er = "Відбулася помилка! Спробуйте будь-ласка ще раз.";
            JOptionPane.showMessageDialog(dialog, er, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
