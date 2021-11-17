package backgroundTasks.controllers;

import support.Channel;
import support.Lists;
import support.Sensor;
import ui.LoadDialog;
import ui.sensorsList.SensorsListDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class RemoveSensor extends SwingWorker<Void, Void> {
    private final SensorsListDialog dialog;
    private final LoadDialog loadDialog;

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
    protected Void doInBackground() throws Exception {
        ArrayList<Sensor>sensors = Lists.sensors();
        ArrayList<Channel>channels = Lists.channels();
        Sensor sensor = Objects.requireNonNull(sensors).get(this.dialog.mainTable.getSelectedRow());
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
        Lists.saveSensorsListToFile(sensors);
        return null;
    }

    @Override
    protected void done() {
        this.dialog.mainTable.update();
        this.dialog.updateMain();
        this.loadDialog.dispose();
    }
}
