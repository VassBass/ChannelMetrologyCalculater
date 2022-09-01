package backgroundTasks;

import model.Channel;
import model.Sensor;
import repository.ChannelRepository;
import repository.SensorRepository;
import repository.impl.ChannelRepositorySQLite;
import repository.impl.SensorRepositorySQLite;
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

    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();
    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public SaveImportedChannels(ArrayList<Channel>newChannels, ArrayList<Channel> channelsForChange,
                                ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange){
        super();
        this.newChannels = newChannels;
        this.channelsForChange = channelsForChange;
        this.newSensors = newSensors;
        this.sensorsForChange = sensorsForChange;
        this.loadDialog = new LoadDialog(MainScreen.getInstance());
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
    }

    @Override
    protected Void doInBackground() throws Exception {
        sensorRepository.importData(this.newSensors, this.sensorsForChange);
        channelRepository.changeSensors(this.sensorsForChange);
        channelRepository.importData(this.newChannels, this.channelsForChange);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
        JOptionPane.showMessageDialog(MainScreen.getInstance(), IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}