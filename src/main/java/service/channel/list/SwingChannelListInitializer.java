package service.channel.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.sensor.SensorRepository;
import service.application.ApplicationScreen;
import service.channel.list.ui.swing.*;
import service.root.ServiceInitializer;

import java.util.ArrayList;

public class SwingChannelListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelListInitializer.class);

    @Override
    public void init() {
        ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        if (applicationScreen != null && repositoryFactory != null) {
            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            ChannelListConfigHolder configHolder = new PropertiesChannelListConfigHolder();

            ChannelListManager manager = new DefaultChannelListManager(repositoryFactory, configHolder);
            ChannelListService service = new DefaultChannelListService(channelRepository, sensorRepository);

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
