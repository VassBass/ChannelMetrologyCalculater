package service.calibrator.info;

import model.dto.Calibrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.calibrator.info.ui.CalibratorInfoContext;
import service.calibrator.info.ui.swing.SwingCalibratorInfoDialog;
import service.calibrator.list.CalibratorListManager;
import service.calibrator.list.ui.swing.SwingCalibratorListDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CalibratorInfoExecuter implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorInfoExecuter.class);

    private final SwingCalibratorListDialog calibratorListDialog;
    private final RepositoryFactory repositoryFactory;
    private final CalibratorListManager parentManager;
    private final Calibrator calibrator;

    public CalibratorInfoExecuter(@Nonnull SwingCalibratorListDialog calibratorListDialog,
                                  @Nonnull RepositoryFactory repositoryFactory,
                                  @Nonnull CalibratorListManager parentManager,
                                  @Nullable Calibrator calibrator) {
        this.calibratorListDialog = calibratorListDialog;
        this.repositoryFactory = repositoryFactory;
        this.parentManager = parentManager;
        this.calibrator = calibrator;
    }

    @Override
    public void execute() {
        CalibratorInfoConfigHolder configHolder = new PropertiesCalibratorInfoConfigHolder();
        CalibratorInfoContext context = new CalibratorInfoContext(repositoryFactory);
        SwingCalibratorInfoManager manager = new SwingCalibratorInfoManager(repositoryFactory, context, parentManager, calibrator);
        context.registerManager(manager);
        SwingCalibratorInfoDialog dialog = new SwingCalibratorInfoDialog(calibratorListDialog, configHolder, context, calibrator);
        manager.registerDialog(dialog);
        dialog.showing();

        logger.info("Service is running");
    }
}
