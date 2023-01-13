package backgroundTasks;

import model.Channel;
import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.channel.ChannelRepositorySQLite;
import ui.channelInfo.DialogChannelExists;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class CheckChannel extends SwingWorker<Optional<Channel>, Void> {
    private final Window parent;
    private final String code;
    private final DialogLoading loadWindow;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public CheckChannel(Window parent, String code){
        super();
        this.parent = parent;
        this.code = code;
        this.loadWindow = new DialogLoading(parent);
        EventQueue.invokeLater(() -> loadWindow.setVisible(true));
    }

    public void start(){
        this.execute();
    }

    @Override
    protected Optional<Channel> doInBackground() throws Exception {
        return channelRepository.get(this.code);
    }

    @Override
    protected void done() {
        this.loadWindow.dispose();
        try {
            if (this.get().isPresent()){
                Channel channel = this.get().get();
                EventQueue.invokeLater(() -> new DialogChannelExists(parent, channel).setVisible(true));
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
