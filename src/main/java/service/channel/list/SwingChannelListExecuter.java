package service.channel.list;

import application.ApplicationMenu;
import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.channel.list.ui.ChannelListSwingContext;
import service.channel.list.ui.swing.SwingChannelListPanel;
import service.channel.list.ui.swing.SwingMenuChannelList;
import service.ServiceExecutor;

public class SwingChannelListExecuter implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelListExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public SwingChannelListExecuter(ApplicationScreen applicationScreen, RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        ChannelListConfigHolder configHolder = new PropertiesChannelListConfigHolder();
        ChannelListService service = new DefaultChannelListService(repositoryFactory);
        ChannelListSwingContext context = new ChannelListSwingContext(service);
        ChannelListManager manager = new SwingChannelListManager(applicationScreen, repositoryFactory, configHolder, context);
        context.registerManager(manager);

        SwingChannelListPanel panel = new SwingChannelListPanel(context);
        applicationScreen.setContentPane(panel);

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(new SwingMenuChannelList(manager));

        manager.revaluateChannelTable();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
