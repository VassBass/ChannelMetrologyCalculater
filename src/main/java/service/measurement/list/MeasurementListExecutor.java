package service.measurement.list;

import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.measurement.list.ui.MeasurementListContext;
import service.measurement.list.ui.swing.SwingMeasurementListDialog;

import javax.annotation.Nonnull;

public class MeasurementListExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementListExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public MeasurementListExecutor(@Nonnull ApplicationScreen applicationScreen,
                                   @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        MeasurementListConfigHolder configHolder = new PropertiesMeasurementListConfigHolder();
        MeasurementListContext context = new MeasurementListContext(repositoryFactory);
        SwingMeasurementListManager manager = new SwingMeasurementListManager(repositoryFactory, context);
        context.registerManager(manager);
        SwingMeasurementListDialog dialog = new SwingMeasurementListDialog(applicationScreen, repositoryFactory, configHolder, context);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
