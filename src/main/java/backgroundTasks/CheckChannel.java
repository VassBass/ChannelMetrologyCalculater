package backgroundTasks;

import application.Application;
import model.Channel;
import ui.channelInfo.ChannelExistsDialog;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;

public class CheckChannel extends SwingWorker<Channel, Void> {
    private final JDialog parentDialog;
    private final JFrame parentFrame;
    private final String code;
    private final LoadDialog loadWindow;

    public CheckChannel(JDialog parent, String code){
        super();
        this.parentDialog = parent;
        this.parentFrame = null;
        this.code = code;
        this.loadWindow = new LoadDialog(parent);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadWindow.setVisible(true);
            }
        });
    }

    public CheckChannel(JFrame parent, String code){
        super();
        this.parentFrame = parent;
        this.parentDialog = null;
        this.code = code;
        this.loadWindow = new LoadDialog(parent);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadWindow.setVisible(true);
            }
        });
    }

    public void start(){
        Window parent = this.parentDialog == null ? this.parentFrame : this.parentDialog;
        if (!Application.isBusy(parent)){
            Application.setBusy(true);
            this.execute();
        }
    }

    @Override
    protected Channel doInBackground() throws Exception {
        return Application.context.channelService.get(this.code);
    }

    @Override
    protected void done() {
        Application.setBusy(false);
        this.loadWindow.dispose();
        Component parent = parentDialog == null ? parentFrame : parentDialog;
        try {
            final Channel channel = this.get();
            if (channel != null){
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (parentDialog == null) {
                            new ChannelExistsDialog(parentFrame, channel).setVisible(true);
                        }else {
                            new ChannelExistsDialog(parentDialog, channel).setVisible(true);
                        }
                    }
                });
            }else {
                JOptionPane.showMessageDialog(parent,
                        "Канал з данним кодом відсутній в списку", "Пошук",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (Exception ex){
            String m = "Виникла помилка, спробуйте будь-ласка ще раз";
            JOptionPane.showMessageDialog(parent, m, "Пошук",JOptionPane.ERROR_MESSAGE);
        }
    }
}
