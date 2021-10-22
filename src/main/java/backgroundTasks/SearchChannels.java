package backgroundTasks;

import constants.ChannelConstants;
import converters.VariableConverter;
import support.Channel;
import support.Lists;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchChannels extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private final ChannelConstants field;
    private final String value;

    private final ArrayList<Channel>arrayAfterSearch = new ArrayList<>();

    public SearchChannels(MainScreen mainScreen, ChannelConstants field, String value){
        super();
        this.mainScreen = mainScreen;
        this.field = field;
        this.value = value;

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
        ArrayList<Channel>channels = Lists.channels();
        if (channels != null) {
            for (Channel channel : channels) {
                switch (this.field) {
                    case CODE:
                        if (channel.getCode().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case NAME:
                        if (channel.getName().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case MEASUREMENT:
                        if (channel.getMeasurement().getName().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case DEPARTMENT:
                        if (channel.getDepartment().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case AREA:
                        if (channel.getArea().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case PROCESS:
                        if (channel.getProcess().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case INSTALLATION:
                        if (channel.getInstallation().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case FULL_PATH:
                        if (channel.getFullPath().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case THIS_DATE:
                        if (VariableConverter.dateToString(channel.getDate()).equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case NEXT_DATE:
                        if (VariableConverter.dateToString(channel.getNextDate()).equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case TECHNOLOGY_NUMBER:
                        if (channel.getTechnologyNumber().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case PROTOCOL_NUMBER:
                        if (channel.getNumberOfProtocol().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                    case SENSOR:
                        if (channel.getSensor().getType().equals(this.value)) {
                            this.arrayAfterSearch.add(channel);
                        }
                        break;
                }
            }
        }
        return null;
    }

    @Override
    protected void done() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.dispose();
                mainScreen.update(arrayAfterSearch, true, ChannelConstants.getStringFromConstant(field), value);
            }
        });
    }
}
