package service.channel.info;

import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import service.application.ApplicationScreen;
import service.channel.info.ui.swing.*;
import service.root.ServiceExecuter;

public class SwingChannelInfoExecuter implements ServiceExecuter {
    @Override
    public void execute() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
        if (repositoryFactory != null && applicationScreen != null) {
            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);

            SwingChannelInfoDialog dialog = new SwingChannelInfoDialog(applicationScreen);
            ChannelInfoManager manager = new SwingChannelInfoManager(dialog, repositoryFactory);

            new SwingChannelInfoInitializer(dialog, manager, channelRepository).init();

            dialog.showing();
        }
    }
}
