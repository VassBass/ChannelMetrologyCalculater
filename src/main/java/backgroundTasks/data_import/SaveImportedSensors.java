package backgroundTasks.data_import;

import constants.Strings;
import model.Sensor;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportedSensors extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final ArrayList<Sensor>sensors;
    private final LoadDialog loadDialog;

    public SaveImportedSensors(MainScreen mainScreen, ArrayList<Sensor>sensors){
        super();
        this.mainScreen = mainScreen;
        this.sensors = sensors;
        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        /*ArrayList<Channel>channels = Lists.channels();
        for (Sensor sensor : this.sensors){
            for (int c = 0; c< Objects.requireNonNull(channels).size(); c++){
                Channel channel = channels.get(c);
                if (channel.getSensor().getName().equals(sensor.getName())){
                    if (!Comparator.sensorsMatch(channel.getSensor(), sensor)){
                        channel.setSensor(sensor);
                        channels.set(c, channel);
                    }
                }
            }
        }
        Lists.saveChannelsListToFile(channels);
        Lists.saveSensorsListToFile(this.sensors);*/
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        //this.mainScreen.update(Lists.channels(), false, null, null);
        JOptionPane.showMessageDialog(this.mainScreen, Strings.IMPORT_SUCCESS, Strings.IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}
