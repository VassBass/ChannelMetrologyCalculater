package backgroundTasks;

import application.Application;
import constants.Sort;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;

public class SearchChannels extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private int field;
    private String valueString;
    private boolean valueBoolean;

    public SearchChannels(){
        super();
        this.mainScreen = Application.context.mainScreen;
        this.loadDialog = new LoadDialog(mainScreen);
    }

    public void startSearch(int field, String value){
        this.field = field;
        this.valueString = value;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
        this.execute();
    }

    public void startSearch(int field, boolean value){
        this.field = field;
        this.valueBoolean = value;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
        this.execute();
    }

    @Override
    protected Void doInBackground() throws Exception {
        switch (this.field){
            case Sort.NAME:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForName(this.valueString));
                break;
            case Sort.DATE:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForDate(this.valueString));
                break;
            case Sort.FREQUENCY:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForFrequency(Double.parseDouble(this.valueString)));
                break;
            case Sort.TECHNOLOGY_NUMBER:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForTechnologyNumber(this.valueString));
                break;
            case Sort.PROTOCOL_NUMBER:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForProtocolNumber(this.valueString));
                break;
            case Sort.REFERENCE:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForReference(this.valueString));
                break;
            case Sort.SUITABILITY:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForSuitability(this.valueBoolean));
                break;
            case Sort.MEASUREMENT_NAME:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForMeasurementName(this.valueString));
                break;
            case Sort.MEASUREMENT_VALUE:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForMeasurementValue(this.valueString));
                break;
            case Sort.DEPARTMENT:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForDepartment(this.valueString));
                break;
            case Sort.AREA:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForArea(this.valueString));
                break;
            case Sort.PROCESS:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForProcess(this.valueString));
                break;
            case Sort.INSTALLATION:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForInstallation(this.valueString));
                break;
            case Sort.SENSOR_NAME:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForSensorName(this.valueString));
                break;
            case Sort.SENSOR_TYPE:
                this.mainScreen.setChannelsList(Application.context.channelSorter.getAllForSensorType(this.valueString));
                break;
             default:
                 this.mainScreen.setChannelsList(Application.context.channelsController.getAll());
                 break;
        }
        return null;
    }

    @Override
    protected void done() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.dispose();
            }
        });
    }
}
