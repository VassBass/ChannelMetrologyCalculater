package backgroundTasks.controllers;

import constants.Strings;
import model.Channel;
import model.Sensor;
import ui.LoadDialog;
import ui.sensorsList.SensorsListDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class RemoveSensor extends SwingWorker<Boolean, Void> {
    private final SensorsListDialog dialog;
    private final LoadDialog loadDialog;
    private String measurement;

    public RemoveSensor(SensorsListDialog dialog){
        super();
        this.dialog = dialog;
        this.loadDialog = new LoadDialog(dialog);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        /*ArrayList<Sensor>sensors = Lists.sensors();
        ArrayList<Channel>channels = Lists.channels();
        Sensor sensor = Objects.requireNonNull(sensors).get(this.dialog.mainTable.getSelectedRow());

        this.measurement = sensor.getMeasurement();
        int check = 0;
        for (Sensor s : sensors){
            if (s.getMeasurement().equals(this.measurement)){
                check++;
            }
        }
        if (check == 1) return false;

        ArrayList<Integer>indexes = new ArrayList<>();
        for (int x = 0; x< Objects.requireNonNull(channels).size(); x++){
            if (channels.get(x).getSensor().getName().equals(sensor.getName())){
                indexes.add(x);
            }
        }

        Collections.reverse(indexes);
        for (int i : indexes){
            channels.remove(i);
        }
        sensors.remove(this.dialog.mainTable.getSelectedRow());

        Lists.saveChannelsListToFile(channels);
        Lists.saveSensorsListToFile(sensors);*/
        return true;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                this.dialog.mainTable.update();
                this.dialog.updateMain();
            }else {
                JOptionPane.showMessageDialog(dialog, "Неможливо видалити останній ПВП для вимірювання параметру \"" + this.measurement + "\"",
                        Strings.ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Помилка при видаленні ПВП!", Strings.ERROR,JOptionPane.ERROR_MESSAGE);
        }
    }
}
