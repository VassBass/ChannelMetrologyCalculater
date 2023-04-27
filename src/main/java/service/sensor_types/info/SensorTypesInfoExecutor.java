package service.sensor_types.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.sensor_types.info.ui.SensorTypesInfoContext;
import service.sensor_types.info.ui.swing.SwingSensorTypesInfoDialog;
import service.sensor_types.list.ui.swing.SwingSensorTypesListDialog;

import javax.annotation.Nonnull;

public class SensorTypesInfoExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SensorTypesInfoExecutor.class);

    private final SwingSensorTypesListDialog parentDialog;
    private final RepositoryFactory repositoryFactory;
    private final String oldType;

    public SensorTypesInfoExecutor(@Nonnull SwingSensorTypesListDialog parentDialog,
                                   @Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull String oldType) {
        this.parentDialog = parentDialog;
        this.repositoryFactory = repositoryFactory;
        this.oldType = oldType;
    }

    @Override
    public void execute() {
        SensorTypesInfoConfigHolder configHolder = new PropertiesSensorTypesInfoConfigHolder();
        SensorTypesInfoContext context = new SensorTypesInfoContext(oldType);
        SwingSensorTypesInfoManager manager = new SwingSensorTypesInfoManager(repositoryFactory, parentDialog, context, oldType);
        context.registerManager(manager);
        SwingSensorTypesInfoDialog dialog = new SwingSensorTypesInfoDialog(parentDialog, oldType, configHolder, context);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info("Service is running");
    }
}
