package backgroundTasks.data_import;

import application.Application;
import model.Channel;
import model.Sensor;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveImportedChannels extends SwingWorker<Void, Void> {
    private static final String IMPORT = "Імпорт";
    private static final String IMPORT_SUCCESS = "Імпорт виконаний успішно";

    private final MainScreen mainScreen;
    private final ArrayList<Channel>newChannels, channelsForChange;
    private final ArrayList<Sensor>newSensors, sensorsForChange;
    private final LoadDialog loadDialog;

    public SaveImportedChannels(ArrayList<Channel>newChannels, ArrayList<Channel> channelsForChange,
                                ArrayList<Sensor>newSensors, ArrayList<Sensor>sensorsForChange){
        super();
        this.mainScreen = Application.context.mainScreen;
        this.newChannels = newChannels;
        this.channelsForChange = channelsForChange;
        this.newSensors = newSensors;
        this.sensorsForChange = sensorsForChange;
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
        Application.context.sensorsController.importData(this.newSensors, this.sensorsForChange);
        Application.context.channelsController.changeSensors(this.sensorsForChange);
        Application.context.channelsController.importData(this.newChannels, this.channelsForChange);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.mainScreen.setChannelsList(Application.context.channelsController.getAll());
        JOptionPane.showMessageDialog(this.mainScreen, IMPORT_SUCCESS, IMPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}