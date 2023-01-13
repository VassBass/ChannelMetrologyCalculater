package backgroundTasks;

import factory.ApplicationFactoryContext;
import factory.WindowFactory;
import service.ChannelSorter;
import ui.mainScreen.MainScreen;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static ui.mainScreen.searchPanel.SearchPanel.*;

public class SearchChannels extends SwingWorker<Void, Void> {
    private final ApplicationFactoryContext context = ApplicationFactoryContext.getInstance();
    private final DialogLoading loadDialog;

    private String field;
    private String valueString;
    private boolean valueBoolean;

    private final MainScreen mainScreen;
    private final ChannelSorter channelSorter = ChannelSorter.getInstance();

    public SearchChannels(){
        super();
        WindowFactory windowFactory = context.getFactory(WindowFactory.class);
        mainScreen = windowFactory.getMainScreen();
        loadDialog = windowFactory.create(DialogLoading.class);
    }

    public void startSearch(String field, String value){
        this.field = field;
        this.valueString = value;
        if (value != null) {
            EventQueue.invokeLater(() -> loadDialog.setVisible(true));
            this.execute();
        }
    }

    public void startSearch(String field, boolean value){
        this.field = field;
        this.valueBoolean = value;
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
        this.execute();
    }

    public void cancel(){
        this.field = null;
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
        this.execute();
    }

    @Override
    protected Void doInBackground() throws Exception {
        switch (this.field){
            case TEXT_NAME:
                mainScreen.setChannelsList(channelSorter.getAllForName(this.valueString));
                break;
            case TEXT_DATE:
                mainScreen.setChannelsList(channelSorter.getAllForDate(this.valueString));
                break;
            case TEXT_FREQUENCY:
                mainScreen.setChannelsList(channelSorter.getAllForFrequency(Double.parseDouble(this.valueString)));
                break;
            case TEXT_TECHNOLOGY_NUMBER:
                mainScreen.setChannelsList(channelSorter.getAllForTechnologyNumber(this.valueString));
                break;
            case TEXT_PROTOCOL_NUMBER:
                mainScreen.setChannelsList(channelSorter.getAllForProtocolNumber(this.valueString));
                break;
            case TEXT_REFERENCE:
                mainScreen.setChannelsList(channelSorter.getAllForReference(this.valueString));
                break;
            case TEXT_SUITABILITY:
                mainScreen.setChannelsList(channelSorter.getAllForSuitability(this.valueBoolean));
                break;
            case TEXT_MEASUREMENT_NAME:
                mainScreen.setChannelsList(channelSorter.getAllForMeasurementName(this.valueString));
                break;
            case TEXT_MEASUREMENT_VALUE:
                mainScreen.setChannelsList(channelSorter.getAllForMeasurementValue(this.valueString));
                break;
            case TEXT_DEPARTMENT:
                mainScreen.setChannelsList(channelSorter.getAllForDepartment(this.valueString));
                break;
            case TEXT_AREA:
                mainScreen.setChannelsList(channelSorter.getAllForArea(this.valueString));
                break;
            case TEXT_PROCESS:
                mainScreen.setChannelsList(channelSorter.getAllForProcess(this.valueString));
                break;
            case TEXT_INSTALLATION:
                mainScreen.setChannelsList(channelSorter.getAllForInstallation(this.valueString));
                break;
            case TEXT_SENSOR_NAME:
                mainScreen.setChannelsList(channelSorter.getAllForSensorName(this.valueString));
                break;
            case TEXT_SENSOR_TYPE:
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