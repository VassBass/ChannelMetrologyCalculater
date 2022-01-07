package backgroundTasks.tasks_for_import;

import constants.Strings;
import model.Channel;
import model.Sensor;
import ui.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportedChannels extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final ArrayList<Channel>channels;
    private final ArrayList<Sensor> sensors;
    private final LoadDialog loadDialog;

    public SaveImportedChannels(MainScreen mainScreen, ArrayList<Channel>channels, ArrayList<Sensor>sensors){
        super();
        this.mainScreen = mainScreen;
        this.channels = channels;
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
        ArrayList<Sensor>newSensorsList = new ArrayList<>();
        for (Channel channel : channels){
            Sensor sensor = channel.getSensor();
            boolean exist = false;
            for (Sensor newSensor : newSensorsList){
                if (newSensor.getName().equals(sensor.getName())){
                    exist = true;
                    break;
                }
            }
            if (!exist){
                newSensorsList.add(sensor);
            }
        }

        ArrayList<Sensor>importedSensors = new ArrayList<>();
        for (Sensor sensor : newSensorsList){
            for (Sensor imp : this.sensors){
                if (sensor.getName().equals(imp.getName())){
                    importedSensors.add(imp);
                }
            }
        }

        /*ArrayList<Sensor>oldSensorsList = Lists.sensors();
        for (Sensor imp : importedSensors){
            boolean exist = false;
            for (int o = 0; o< Objects.requireNonNull(oldSensorsList).size(); o++){
                Sensor old = oldSensorsList.get(o);
                if (imp.getName().equals(old.getName())){
                    oldSensorsList.add(imp);
                    exist = true;
                    break;
                }
            }
            if (!exist){
                oldSensorsList.add(imp);
            }
        }

        Lists.saveSensorsListToFile(oldSensorsList);
        Lists.saveChannelsListToFile(this.channels);*/
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        //this.mainScreen.update(Lists.channels(), false, null, null);
        JOptionPane.showMessageDialog(this.mainScreen, Strings.IMPORT_SUCCESS, Strings.IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}
