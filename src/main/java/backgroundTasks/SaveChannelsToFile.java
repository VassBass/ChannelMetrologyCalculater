package backgroundTasks;

import support.Channel;
import support.Lists;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SaveChannelsToFile extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;
    private final ArrayList<Channel>channels;

    public SaveChannelsToFile(MainScreen mainScreen, ArrayList<Channel> channels){
        super();

        this.mainScreen = mainScreen;
        this.channels = channels;
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
        Lists.saveChannelsListToFile(this.channels);
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        this.mainScreen.update(this.channels, false, null, null);
    }
}
