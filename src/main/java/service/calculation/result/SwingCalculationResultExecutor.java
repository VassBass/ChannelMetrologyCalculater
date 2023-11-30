package service.calculation.result;

import application.ApplicationScreen;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import model.dto.Measurement;
import model.ui.DefaultDialog;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.protocol.Protocol;
import service.calculation.result.ui.SwingCalculationResultContext;
import service.calculation.result.ui.swing.SwingCalculationResultDialog;
import service.calculation.result.worker.CalculationWorker;
import service.calculation.result.worker.ConsumptionCalculationWorker;
import service.calculation.result.worker.DefaultCalculationWorker;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class SwingCalculationResultExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationResultExecutor.class);

    private static final String CALCULATION_ERROR = "calculationError";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final CalculationManager manager;
    private final Protocol protocol;

    public SwingCalculationResultExecutor(@Nonnull ApplicationScreen applicationScreen,
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
        LoadingDialog loadingDialog = new LoadingDialog(applicationScreen);
        loadingDialog.showing();

        new Worker(loadingDialog).execute();
    }


    private class Worker extends SwingWorker<Boolean, Void> {
        private final DefaultDialog loadDialog;

        private Worker(DefaultDialog loadDialog) {
            this.loadDialog = loadDialog;
        }

        @Override
        protected Boolean doInBackground() {
            String measurementName = protocol.getChannel().getMeasurementName();
            CalculationWorker worker;
            if (measurementName.equals(Measurement.CONSUMPTION)) {
                worker = new ConsumptionCalculationWorker(repositoryFactory);
            }else {
                worker = new DefaultCalculationWorker(repositoryFactory);
            }
            return worker.calculate(protocol);
        }

        @Override
        protected void done() {
            try {
                if (get()) {
                    SwingCalculationResultContext context = new SwingCalculationResultContext(protocol);
                    context.registerManager(manager);
                    SwingCalculationResultDialog dialog = new SwingCalculationResultDialog(applicationScreen, configHolder, manager, context, protocol);
                    manager.registerResultDialog(dialog);
                    dialog.showing();
                    logger.info(Messages.Log.SERVICE_RUNNING);
                    loadDialog.shutdown();
                    return;
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.warn(Messages.Log.EXCEPTION_THROWN, e);
            }
            String message = Messages.getMessages(SwingCalculationResultExecutor.class).get(CALCULATION_ERROR);
            JOptionPane.showMessageDialog(applicationScreen, message, Labels.getRootLabels().get(RootLabelName.ERROR), JOptionPane.ERROR_MESSAGE);
            loadDialog.shutdown();
        }
    }
}
