package backgroundTasks;

import model.Channel;
import model.Sensor;
import service.impl.ChannelServiceImpl;
import service.impl.SensorServiceImpl;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportedChannels extends SwingWorker<Void, Void> {
    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final ArrayList<Channel>newChannels, channelsForChange;
    private final ArrayList<Sensor>newSensors, sensorsForChange;
    private final LoadDialog loadDialog;

    public SaveImportedChannels(ArrayList<Channel>newChannels, ArrayList<Channel> channelsForChange,
                                ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange){
        super();
        this.newChannels = newChannels;
        this.channelsForChange = channelsForChange;
        this.newSensors = newSensors;
        this.sensorsForChange = sensorsForChange;
        this.loadDialog = new LoadDialog(MainScreen.getInstance());
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        SensorServiceImpl.getInstance().importData(this.newSensors, this.sensorsForChange);
        ChannelServiceImpl.getInstance().changeSensors(this.sensorsForChange);
        ChannelServiceImpl.getInstance().importData(this.newChannels, this.channelsForChange);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        MainScreen.getInstance().setChannelsList(new ArrayList<>(ChannelServiceImpl.getInstance().getAll()));
        JOptionPane.showMessageDialog(MainScreen.getInstance(), IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}