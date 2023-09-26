package service.channel.info;

import application.ApplicationScreen;
import model.dto.Channel;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.channel.info.ui.ChannelInfoSwingContext;
import service.channel.info.ui.swing.SwingChannelInfoDialog;
import service.channel.list.ChannelListManager;

import javax.annotation.Nonnull;
import javax.swing.*;

public class SwingChannelInfoExecuter implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelInfoExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final ChannelListManager channelListManager;
    private Channel channel;

    public SwingChannelInfoExecuter(@Nonnull ApplicationScreen applicationScreen,
                                    @Nonnull RepositoryFactory repositoryFactory,
                                    @Nonnull ChannelListManager channelListManager){
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.channelListManager = channelListManager;
    }

    public SwingChannelInfoExecuter registerChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    @Override
    public void execute() {
        logger.info("Start of service execution");
        new Worker().execute();
    }

    private class Worker extends SwingWorker<Void, Void> {

        private final LoadingDialog loadingDialog;
        private SwingChannelInfoDialog dialog;

        private Worker() {
            loadingDialog = new LoadingDialog(applicationScreen);
        }

        @Override
        protected Void doInBackground() {
            ChannelInfoConfigHolder configHolder = new PropertiesChannelInfoConfigHolder();

            ChannelInfoSwingContext context = new ChannelInfoSwingContext(repositoryFactory);
            SwingChannelInfoManager manager = new SwingChannelInfoManager(applicationScreen, repositoryFactory, channelListManager, context);
            context.registerManager(manager);

            dialog = new SwingChannelInfoDialog(applicationScreen, configHolder, context);
            manager.registerDialog(dialog);

            manager.setChannelInfo(channel);

            return null;
        }

        @Override
        protected void done() {
            loadingDialog.shutdown();
            dialog.showing();
            logger.info("The service is running");
        }
    }
}
