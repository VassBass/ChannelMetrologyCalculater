package service.sensor_types.list;

import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.sensor_types.list.ui.SensorTypesListContext;
import service.sensor_types.list.ui.swing.SwingSensorTypesListDialog;

import javax.annotation.Nonnull;

public class SensorTypesListExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SensorTypesListExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public SensorTypesListExecutor(@Nonnull ApplicationScreen applicationScreen,
                                   @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        PropertiesSensorTypesListConfigHolder configHolder = new PropertiesSensorTypesListConfigHolder();
        SensorTypesListContext context = new SensorTypesListContext(repositoryFactory);
        SwingSensorTypesListManager manager = new SwingSensorTypesListManager(repositoryFactory, context);
        context.registerManager(manager);
        SwingSensorTypesListDialog dialog = new SwingSensorTypesListDialog(applicationScreen, repositoryFactory, configHolder, context);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
