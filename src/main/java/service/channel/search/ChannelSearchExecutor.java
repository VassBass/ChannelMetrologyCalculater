package service.channel.search;

import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceExecutor;
import service.channel.list.ChannelListManager;
import service.channel.search.ui.ChannelSearchContext;
import service.channel.search.ui.swing.SwingChannelSearchDialog;

import javax.annotation.Nonnull;

public class ChannelSearchExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ChannelSearchExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final ChannelListManager parentManager;

    public ChannelSearchExecutor(@Nonnull ApplicationScreen applicationScreen, ChannelListManager parentManager) {
        this.applicationScreen = applicationScreen;
        this.parentManager = parentManager;
    }

    @Override
    public void execute() {
        ChannelSearchConfigHolder configHolder = new PropertiesChannelSearchConfigHolder();
        ChannelSearchContext context = new ChannelSearchContext();
        SwingChannelSearchManager manager = new SwingChannelSearchManager(parentManager, context);
        context.registerManager(manager);
        SwingChannelSearchDialog dialog = new SwingChannelSearchDialog(applicationScreen, configHolder, context);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
