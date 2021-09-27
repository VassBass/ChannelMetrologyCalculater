package backgroundTasks;

import support.Channel;
import support.Lists;
import ui.LoadDialog;
import ui.channelInfo.DialogChannel;
import ui.channelInfo.Dialog_channelExist;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PutChannelInList extends SwingWorker<Void, Void> {

    private boolean success = true;

    private final JDialog loadDialog;
    private final DialogChannel channelDialog;
    private final MainScreen mainScreen;

    private ArrayList<Channel>channelList;

    public PutChannelInList(final DialogChannel channelDialog, MainScreen mainScreen){
        super();
        this.channelDialog = channelDialog;
        this.mainScreen = mainScreen;

        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    channelDialog.setVisible(false);
                    loadDialog.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        Channel oldChannel = this.channelDialog.oldChannel;
        Channel newChannel = this.channelDialog.getChannel();
        this.channelList = Lists.channels();

        if (this.channelList != null) {
            if (oldChannel == null) {
                for (Channel channel : this.channelList) {
                    if (newChannel.getCode().equals(channel.getCode())) {
                        this.success = false;
                        return null;
                    }
                }
                this.channelList.add(newChannel);
            } else {
                int index = -1;
                for (int x = 0; x < this.channelList.size(); x++) {
                    if (oldChannel.getCode().equals(this.channelList.get(x).getCode())) {
                        index = x;
                        break;
                    }
                }
                for (int x = 0; x < this.channelList.size(); x++) {
                    if (newChannel.getCode().equals(this.channelList.get(x).getCode()) && x != index) {
                        this.success = false;
                        return null;
                    }
                }
                this.channelList.set(index, newChannel);
            }
        }
        Lists.saveChannelsListToFile(this.channelList);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        if (this.success){
            this.channelDialog.dispose();
            this.mainScreen.update(this.channelList, false, null, null);
        }else {
            Dialog_channelExist channelExist = new Dialog_channelExist(this.channelDialog);
            channelExist.setVisible(true);
        }
    }
}
