package backgroundTasks.controllers;

import model.Channel;
import support.Lists;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class RemoveChannels extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private ArrayList<Channel>channelsList_toRemove = null;
    private ArrayList<Channel>channelsListAfterDone;
    private Channel channel = null;

    public RemoveChannels(MainScreen mainScreen, ArrayList<Channel>channels){
        super();
        this.mainScreen = mainScreen;
        this.channelsList_toRemove = channels;

        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    public RemoveChannels(MainScreen mainScreen, Channel channel){
        super();
        this.mainScreen = mainScreen;
        this.channel = channel;

        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (this.channel == null){
            this.removeChannelsArray();
        }else {
            this.removeChannel();
        }
        Lists.saveChannelsListToFile(this.channelsListAfterDone);
        return null;
    }

    @Override
    protected void done() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    loadDialog.dispose();
                    mainScreen.update(channelsListAfterDone, false, null, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void removeChannel(){
        this.channelsListAfterDone = Lists.channels();
        if (this.channelsListAfterDone != null) {
            for (int x = 0; x < this.channelsListAfterDone.size(); x++) {
                if (this.channel.getCode().equals(this.channelsListAfterDone.get(x).getCode())) {
                    this.channelsListAfterDone.remove(x);
                    break;
                }
            }
        }
    }

    private void removeChannelsArray(){
        this.channelsListAfterDone = Lists.channels();
        ArrayList<Integer>indexes = new ArrayList<>();
        if (this.channelsListAfterDone != null) {
            for (int x = 0; x < this.channelsListAfterDone.size(); x++) {
                for (Channel channel : this.channelsList_toRemove) {
                    if (this.channelsListAfterDone.get(x).getCode().equals(channel.getCode())) {
                        indexes.add(x);
                        break;
                    }
                }
            }
            Collections.reverse(indexes);
            for (int i : indexes) {
                this.channelsListAfterDone.remove(i);
            }
        }
    }
}
