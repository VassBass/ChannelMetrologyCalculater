package backgroundTasks;

import constants.Sort;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import service.ChannelSorter;
import ui.mainScreen.MainScreen;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchChannels extends SwingWorker<Void, Void> {
    private final DialogLoading loadDialog;

    private int field;
    private String valueString;
    private boolean valueBoolean;

    private final MainScreen mainScreen = MainScreen.getInstance();
    private final ChannelSorter channelSorter = ChannelSorter.getInstance();
    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public SearchChannels(){
        super();
        this.loadDialog = new DialogLoading(MainScreen.getInstance());
    }

    public void startSearch(int field, String value){
        this.field = field;
        this.valueString = value;
        if (value != null) {
            EventQueue.invokeLater(() -> loadDialog.setVisible(true));
            this.execute();
        }
    }

    public void startSearch(int field, boolean value){
        this.field = field;
        this.valueBoolean = value;
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
        this.execute();
    }

    public void cancel(){
        this.field = -1;
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
        this.execute();
    }

    @Override
    protected Void doInBackground() throws Exception {
        switch (this.field){
            case Sort.NAME:
                mainScreen.setChannelsList(channelSorter.getAllForName(this.valueString));
                break;
            case Sort.DATE:
                mainScreen.setChannelsList(channelSorter.getAllForDate(this.valueString));
                break;
            case Sort.FREQUENCY:
                mainScreen.setChannelsList(channelSorter.getAllForFrequency(Double.parseDouble(this.valueString)));
                break;
            case Sort.TECHNOLOGY_NUMBER:
                mainScreen.setChannelsList(channelSorter.getAllForTechnologyNumber(this.valueString));
                break;
            case Sort.PROTOCOL_NUMBER:
                mainScreen.setChannelsList(channelSorter.getAllForProtocolNumber(this.valueString));
                break;
            case Sort.REFERENCE:
                mainScreen.setChannelsList(channelSorter.getAllForReference(this.valueString));
                break;
            case Sort.SUITABILITY:
                mainScreen.setChannelsList(channelSorter.getAllForSuitability(this.valueBoolean));
                break;
            case Sort.MEASUREMENT_NAME:
                mainScreen.setChannelsList(channelSorter.getAllForMeasurementName(this.valueString));
                break;
            case Sort.MEASUREMENT_VALUE:
                mainScreen.setChannelsList(channelSorter.getAllForMeasurementValue(this.valueString));
                break;
            case Sort.DEPARTMENT:
                mainScreen.setChannelsList(channelSorter.getAllForDepartment(this.valueString));
                break;
            case Sort.AREA:
                mainScreen.setChannelsList(channelSorter.getAllForArea(this.valueString));
                break;
            case Sort.PROCESS:
                mainScreen.setChannelsList(channelSorter.getAllForProcess(this.valueString));
                break;
            case Sort.INSTALLATION:
                mainScreen.setChannelsList(channelSorter.getAllForInstallation(this.valueString));
                break;
            case Sort.SENSOR_NAME:
                mainScreen.setChannelsList(channelSorter.getAllForSensorName(this.valueString));
                break;
            case Sort.SENSOR_TYPE:
                mainScreen.setChannelsList(channelSorter.getAllForSensorType(this.valueString));
                break;
             default:
                 mainScreen.setChannelsList(new ArrayList<>(channelRepository.getAll()));
                 break;
        }
        return null;
    }

    @Override
    protected void done() {
        EventQueue.invokeLater(loadDialog::dispose);
    }
}