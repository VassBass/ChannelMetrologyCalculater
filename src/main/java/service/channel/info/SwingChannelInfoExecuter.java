package service.channel.info;

import model.dto.Channel;
import repository.RepositoryFactory;
import service.application.ApplicationScreen;
import service.channel.info.ui.swing.SwingChannelInfoDialog;
import service.root.ServiceExecuter;

import javax.swing.*;

public class SwingChannelInfoExecuter implements ServiceExecuter {

    private Channel channel;

    public SwingChannelInfoExecuter(){}

    public SwingChannelInfoExecuter(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void execute() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
        if (repositoryFactory != null && applicationScreen != null) {
            SwingChannelInfoDialog dialog = new SwingChannelInfoDialog(applicationScreen);
            ChannelInfoManager manager = new SwingChannelInfoManager(dialog, repositoryFactory);

            new SwingChannelInfoInitializer(dialog, manager, repositoryFactory, channel).init();

            dialog.showing();
        }
    }
}
