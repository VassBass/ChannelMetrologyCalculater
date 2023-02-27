package service.channel.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.application.ApplicationScreen;
import service.channel.list.ui.swing.*;
import service.repository.RepositoryImplementationFactory;
import service.repository.repos.channel.ChannelRepository;
import service.root.ServiceInitializer;

import java.util.ArrayList;

public class SwingChannelListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelListInitializer.class);

    @Override
    public void init() {
        ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
        RepositoryImplementationFactory repositoryFactory =
                RepositoryImplementationFactory.getInstance();
        if (applicationScreen != null && repositoryFactory != null) {
            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
            ChannelListConfigHolder configHolder = new PropertiesChannelListConfigHolder();
            ChannelListManager manager = new DefaultChannelListManager(channelRepository, configHolder);
            ChannelListService service = new DefaultChannelListService(channelRepository);

            SwingChannelListInfoTable infoTable = new SwingChannelListInfoTable(service);
            SwingChannelListSearchPanel searchPanel = new SwingChannelListSearchPanel(manager);
            SwingChannelListButtonsPanel buttonsPanel = new SwingChannelListButtonsPanel(manager);
            SwingChannelListTable channelsTable = new SwingChannelListTable(manager, service);
            channelsTable.setChannelList(new ArrayList<>(channelRepository.getAll()));

            manager.init(infoTable, searchPanel, channelsTable);
            SwingChannelListPanel swingPanel = new SwingChannelListPanel(infoTable, searchPanel, buttonsPanel, channelsTable);
            applicationScreen.setContentPane(swingPanel);
            applicationScreen.addMenu(new SwingMenuChannelList(manager));

            logger.info(("Initialization completed successfully"));
        }
    }
}
