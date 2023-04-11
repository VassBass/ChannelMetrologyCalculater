package service.sensor_error.info;

import model.dto.SensorError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecuter;
import service.sensor_error.info.ui.SensorErrorInfoContext;
import service.sensor_error.info.ui.swing.SwingSensorErrorInfoDialog;
import service.sensor_error.list.SensorErrorListManager;
import service.sensor_error.list.ui.swing.SwingSensorErrorListDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SensorErrorInfoExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(SensorErrorInfoExecuter.class);

    private final SwingSensorErrorListDialog parentDialog;
    private final RepositoryFactory repositoryFactory;
    private final SensorErrorListManager parentManager;
    private final SensorError oldError;

    public SensorErrorInfoExecuter(@Nonnull SwingSensorErrorListDialog parentDialog,
                                   @Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull SensorErrorListManager parentManager,
                                   @Nullable SensorError oldError) {
        this.parentDialog = parentDialog;
        this.repositoryFactory = repositoryFactory;
        this.parentManager = parentManager;
        this.oldError = oldError;
    }

    @Override
    public void execute() {
        SensorErrorInfoConfigHolder configHolder = new PropertiesSensorErrorInfoConfigHolder();
        SensorErrorInfoContext context = new SensorErrorInfoContext(repositoryFactory, oldError);
        SwingSensorErrorInfoManager manager = new SwingSensorErrorInfoManager(repositoryFactory, parentManager, context, oldError);
        context.registerManager(manager);
        SwingSensorErrorInfoDialog dialog = new SwingSensorErrorInfoDialog(parentDialog, configHolder, context);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info("Service is running");
    }
}
