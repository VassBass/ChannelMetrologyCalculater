package service.sensor_error.list;

import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.sensor_error.list.ui.SensorErrorListContext;
import service.sensor_error.list.ui.swing.SwingSensorErrorListDialog;

import javax.annotation.Nonnull;

public class SensorErrorListExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SensorErrorListExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public SensorErrorListExecutor(@Nonnull ApplicationScreen applicationScreen,
                                   @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        SensorErrorListConfigHolder configHolder = new PropertiesSensorErrorListConfigHolder();
        SensorErrorListContext context = new SensorErrorListContext(repositoryFactory);
        SwingSensorErrorListManager manager = new SwingSensorErrorListManager(repositoryFactory, context);
        context.registerManager(manager);
        SwingSensorErrorListDialog dialog = new SwingSensorErrorListDialog(applicationScreen, repositoryFactory, configHolder, context);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
