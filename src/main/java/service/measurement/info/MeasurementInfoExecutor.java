package service.measurement.info;

import model.dto.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.measurement.info.ui.MeasurementInfoContext;
import service.measurement.info.ui.swing.SwingMeasurementInfoDialog;
import service.measurement.list.ui.swing.SwingMeasurementListDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class MeasurementInfoExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementInfoExecutor.class);

    private final SwingMeasurementListDialog parentDialog;
    private final RepositoryFactory repositoryFactory;
    private final Measurement oldMeasurement;

    public MeasurementInfoExecutor(@Nonnull SwingMeasurementListDialog parentDialog,
                                   @Nonnull RepositoryFactory repositoryFactory,
                                   @Nullable Measurement oldMeasurement) {
        this.parentDialog = parentDialog;
        this.repositoryFactory = repositoryFactory;
        this.oldMeasurement = oldMeasurement;
    }

    @Override
    public void execute() {
        MeasurementInfoConfigHolder configHolder = new PropertiesMeasurementInfoConfigHolder();
        MeasurementInfoContext context = new MeasurementInfoContext(repositoryFactory, oldMeasurement);
        SwingMeasurementInfoManager manager = new SwingMeasurementInfoManager(repositoryFactory, context, parentDialog, oldMeasurement);
        context.registerManager(manager);
        SwingMeasurementInfoDialog dialog = new SwingMeasurementInfoDialog(parentDialog, context, configHolder);
        manager.registerDialog(dialog);
        if (Objects.isNull(oldMeasurement)) manager.changeName();

        dialog.showing();
        logger.info("Service is running");
    }
}
