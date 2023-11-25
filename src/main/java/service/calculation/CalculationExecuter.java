package service.calculation;

import application.ApplicationScreen;
import model.dto.Channel;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class CalculationExecuter implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CalculationExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final Channel channel;

    public CalculationExecuter(@Nonnull ApplicationScreen applicationScreen,
                               @Nonnull RepositoryFactory repositoryFactory,
                               @Nonnull Channel channel) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.channel = channel;
    }

    @Override
    public void execute() {
        LoadingDialog loadingDialog = new LoadingDialog(applicationScreen);
        loadingDialog.showing();

        new SwingWorker<Boolean, Void>() {
            private CalculationManager manager;

            @Override
            protected Boolean doInBackground() {
                CalculationConfigHolder configHolder = new PropertiesCalculationConfigHolder();
                manager = new SwingCalculationManager(applicationScreen, repositoryFactory, configHolder, channel);
                return true;
            }

            @Override
            protected void done() {
                loadingDialog.shutdown();
                try {
                    if (get()) manager.showConditionDialog();
                } catch (InterruptedException | ExecutionException e) {
                    logger.warn(Messages.Log.EXCEPTION_THROWN, e);
                }
            }
        }.execute();
    }
}
