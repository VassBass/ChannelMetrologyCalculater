package service.calculation;

import model.dto.Channel;
import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.calculation.collect.condition.ui.PropertiesCalculationConfigHolder;
import service.root.ServiceExecuter;
import util.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class CalculationExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(CalculationExecuter.class);

    private final Frame owner;
    private final RepositoryFactory repositoryFactory;
    private final Channel channel;

    public CalculationExecuter(Frame owner, RepositoryFactory repositoryFactory, Channel channel) {
        this.owner = owner;
        this.repositoryFactory = repositoryFactory;
        this.channel = channel;
    }

    @Override
    public void execute() {
        LoadingDialog loadingDialog = LoadingDialog.getInstance();
        Point location = ScreenPoint.center(owner, loadingDialog);
        DialogWrapper loadingDialogWrapper = new DialogWrapper(owner, loadingDialog, location);
        loadingDialogWrapper.showing();

        new SwingWorker<Boolean, Void>() {
            private CalculationManager manager;

            @Override
            protected Boolean doInBackground() {
                CalculationConfigHolder configHolder = new PropertiesCalculationConfigHolder();
                manager = new SwingCalculationManager(owner, channel, configHolder, repositoryFactory);
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
