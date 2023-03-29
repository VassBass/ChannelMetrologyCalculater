package service.calculation.result;

import application.ApplicationScreen;
import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import model.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecuter;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.dto.Protocol;
import service.calculation.result.ui.SwingCalculationResultContext;
import service.calculation.result.ui.swing.SwingCalculationResultDialog;
import service.calculation.result.worker.DefaultCalculationWorker;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class SwingCalculationResultExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationResultExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final CalculationManager manager;
    private final Protocol protocol;

    public SwingCalculationResultExecuter(@Nonnull ApplicationScreen applicationScreen,
                                          @Nonnull RepositoryFactory repositoryFactory,
                                          @Nonnull CalculationConfigHolder configHolder,
                                          @Nonnull CalculationManager manager,
                                          @Nonnull Protocol protocol) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
        this.manager = manager;
        this.protocol = protocol;
    }

    @Override
    public void execute() {
        LoadingDialog dialog = LoadingDialog.getInstance();
        DialogWrapper loadingDialog = new DialogWrapper(applicationScreen, dialog, ScreenPoint.center(applicationScreen, dialog));
        loadingDialog.showing();

        new Worker(loadingDialog).execute();
    }


    private class Worker extends SwingWorker<Boolean, Void> {
        private final UI loadDialog;

        private Worker(UI loadDialog) {
            this.loadDialog = loadDialog;
        }

        @Override
        protected Boolean doInBackground() {
            return new DefaultCalculationWorker(repositoryFactory).calculate(protocol);
        }

        @Override
        protected void done() {
            try {
                if (get()) {
                    SwingCalculationResultContext context = new SwingCalculationResultContext(protocol);
                    context.registerManager(manager);
                    SwingCalculationResultDialog dialog = new SwingCalculationResultDialog(applicationScreen, configHolder, manager, context);
                    manager.registerResultDialog(dialog);
                    dialog.showing();

                    logger.info("Service is running");
                    loadDialog.shutdown();
                    return;
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.warn("Exception was thrown", e);
            }
            String message = "Під час розрахунку виникла помилка. Спробуйте ще раз.";
            JOptionPane.showMessageDialog(applicationScreen, message, "Помилка", JOptionPane.ERROR_MESSAGE);
            loadDialog.shutdown();
        }
    }
}
