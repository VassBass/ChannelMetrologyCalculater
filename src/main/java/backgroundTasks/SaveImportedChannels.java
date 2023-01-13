package backgroundTasks;

import model.Channel;
import model.Sensor;
import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.sensor.SensorRepository;
import service.repository.repos.channel.ChannelRepositorySQLite;
import service.repository.repos.sensor.SensorRepositorySQLite;
import ui.mainScreen.MainScreen;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SaveImportedChannels extends SwingWorker<Void, Void> {
    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final List<Channel> newChannels, channelsForChange;
    private final List<Sensor>newSensors, sensorsForChange;
    private final DialogLoading loadDialog;

    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();
    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public SaveImportedChannels(List<Channel>newChannels, List<Channel> channelsForChange,
                                List<Sensor>newSensors, List<Sensor>sensorsForChange){
        super();
        this.newChannels = newChannels;
        this.channelsForChange = channelsForChange;
        this.newSensors = newSensors;
        this.sensorsForChange = sensorsForChange;
        this.loadDialog = new DialogLoading(MainScreen.getInstance());
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