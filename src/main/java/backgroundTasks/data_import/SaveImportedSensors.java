package backgroundTasks.data_import;

import application.Application;
import model.Channel;
import model.Sensor;
import support.Comparator;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportedSensors extends SwingWorker<Void, Void> {
    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

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
        ArrayList<Channel>channels = Application.context.channelsController.getAll();
        for (Sensor sensor : this.sensors){
            for (int c = 0; c< channels.size(); c++){
                Channel channel = channels.get(c);
                if (channel.getSensor().getName().equals(sensor.getName())){
                    if (!Comparator.sensorsMatch(channel.getSensor(), sensor)){
                        channel.setSensor(sensor);
                        channels.set(c, channel);
                    }
                }
            }
        }
        Application.context.sensorsController.rewriteAll(this.sensors);
        Application.context.channelsController.rewriteAll(channels);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.mainScreen.setChannelsList(Application.context.channelsController.getAll());
        JOptionPane.showMessageDialog(this.mainScreen, IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}