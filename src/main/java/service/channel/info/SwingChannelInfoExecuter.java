package service.channel.info;

import model.dto.Channel;
import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.application.ApplicationScreen;
import service.channel.info.ui.swing.SwingChannelInfoDialog;
import service.channel.list.ChannelListManager;
import service.root.ServiceExecuter;
import util.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SwingChannelInfoExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelInfoExecuter.class);

    private final ChannelListManager channelListManager;
    private Channel channel;

    public SwingChannelInfoExecuter(ChannelListManager channelListManager){
        this.channelListManager = channelListManager;
    }

    public SwingChannelInfoExecuter(ChannelListManager channelListManager, Channel channel) {
        this.channelListManager = channelListManager;
        this.channel = channel;
    }

    @Override
    public void execute() {
        logger.info("Start of service execution");
        new Worker().execute();
    }

    private class Worker extends SwingWorker<Void, Void> {

        private final DialogWrapper loadingDialogWrapper;
        private SwingChannelInfoDialog dialog;

        private Worker() {
            ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
            LoadingDialog loadingDialog = LoadingDialog.getInstance();
            Point location = ScreenPoint.center(Objects.requireNonNull(applicationScreen), loadingDialog);
            loadingDialogWrapper = new DialogWrapper(applicationScreen, loadingDialog, location);
        }

        @Override
        protected Void doInBackground() {
            RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
            ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
            if (repositoryFactory != null && applicationScreen != null) {
                dialog = new SwingChannelInfoDialog(applicationScreen);
                ChannelInfoManager manager = new SwingChannelInfoManager(dialog, channelListManager, repositoryFactory);

                new SwingChannelInfoInitializer(dialog, manager, repositoryFactory, channel).init();

                dialog.showing();
            }
            return null;
        }

        @Override
        protected void done() {
            loadingDialogWrapper.shutdown();
            if (dialog != null) dialog.showing();
            logger.info("The service is running");
        }
    }
}
