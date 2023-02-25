package service.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.application.ApplicationScreen;
import service.channel.ui.swing.*;
import service.repository.RepositoryImplementationFactory;
import service.repository.repos.channel.ChannelRepository;
import service.root.ServiceInitializer;

import java.util.ArrayList;

public class SwingChannelInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelInitializer.class);

    @Override
    public void init() {
        ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
        RepositoryImplementationFactory repositoryFactory =
                RepositoryImplementationFactory.getInstance();
        if (applicationScreen != null && repositoryFactory != null) {
            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
            ChannelConfigHolder configHolder = new PropertiesChannelConfigHolder();
            ChannelManager manager = new DefaultChannelManager(channelRepository, configHolder);
            ChannelService service = new DefaultChannelService(channelRepository);

            SwingChannelInfoTable infoTable = new SwingChannelInfoTable(service);
            SwingSearchPanel searchPanel = new SwingSearchPanel(manager);
            SwingChannelButtonsPanel buttonsPanel = new SwingChannelButtonsPanel(manager);
            SwingChannelsTable channelsTable = new SwingChannelsTable(manager, service);
            channelsTable.setChannelList(new ArrayList<>(channelRepository.getAll()));

            manager.init(infoTable, searchPanel, channelsTable);
            SwingPanel swingPanel = new SwingPanel(infoTable, searchPanel, buttonsPanel, channelsTable);
            applicationScreen.setContentPane(swingPanel);
            applicationScreen.addMenu(new SwingMenuChannel(manager));

            logger.info(("Initialization completed successfully"));
        }
    }
}
