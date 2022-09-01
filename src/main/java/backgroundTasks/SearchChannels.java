package backgroundTasks;

import constants.Sort;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import service.ChannelSorter;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchChannels extends SwingWorker<Void, Void> {
    private final LoadDialog loadDialog;

    private int field;
    private String valueString;
    private boolean valueBoolean;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public SearchChannels(){
        super();
        this.loadDialog = new LoadDialog(MainScreen.getInstance());
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

    @Override
    protected Void doInBackground() throws Exception {
        switch (this.field){
            case Sort.NAME:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForName(this.valueString));
                break;
            case Sort.DATE:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForDate(this.valueString));
                break;
            case Sort.FREQUENCY:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForFrequency(Double.parseDouble(this.valueString)));
                break;
            case Sort.TECHNOLOGY_NUMBER:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForTechnologyNumber(this.valueString));
                break;
            case Sort.PROTOCOL_NUMBER:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForProtocolNumber(this.valueString));
                break;
            case Sort.REFERENCE:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForReference(this.valueString));
                break;
            case Sort.SUITABILITY:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForSuitability(this.valueBoolean));
                break;
            case Sort.MEASUREMENT_NAME:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForMeasurementName(this.valueString));
                break;
            case Sort.MEASUREMENT_VALUE:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForMeasurementValue(this.valueString));
                break;
            case Sort.DEPARTMENT:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForDepartment(this.valueString));
                break;
            case Sort.AREA:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForArea(this.valueString));
                break;
            case Sort.PROCESS:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForProcess(this.valueString));
                break;
            case Sort.INSTALLATION:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForInstallation(this.valueString));
                break;
            case Sort.SENSOR_NAME:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForSensorName(this.valueString));
                break;
            case Sort.SENSOR_TYPE:
                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getAllForSensorType(this.valueString));
                break;
             default:
                 MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
                 break;
        }
        return null;
    }

    @Override
    protected void done() {
        EventQueue.invokeLater(loadDialog::dispose);
    }
}