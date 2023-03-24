package service.calculation;

import application.ApplicationScreen;
import model.dto.Channel;
import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecuter;
import service.calculation.dto.Protocol;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class CalculationExecuter implements ServiceExecuter {
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
        LoadingDialog loadingDialog = LoadingDialog.getInstance();
        Point location = ScreenPoint.center(applicationScreen, loadingDialog);
        DialogWrapper loadingDialogWrapper = new DialogWrapper(applicationScreen, loadingDialog, location);
        loadingDialogWrapper.showing();

        new SwingWorker<Boolean, Void>() {
            private CalculationManager manager;

            @Override
            protected Boolean doInBackground() {
                CalculationConfigHolder configHolder = new PropertiesCalculationConfigHolder();
                Protocol protocol = new Protocol(channel);
                manager = new SwingCalculationManager(applicationScreen, repositoryFactory, configHolder, protocol);
                return true;
            }

            @Override
            protected void done() {
                loadingDialogWrapper.shutdown();
                try {
                    if (get()) manager.showConditionDialog();
                } catch (InterruptedException | ExecutionException e) {
                    logger.warn("Exception was thrown.", e);
                }
            }
        }.execute();
    }
}
