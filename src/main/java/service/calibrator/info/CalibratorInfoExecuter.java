package service.calibrator.info;

import application.ApplicationScreen;
import model.dto.Calibrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecuter;
import service.calibrator.info.ui.CalibratorInfoContext;
import service.calibrator.info.ui.swing.SwingCalibratorInfoDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CalibratorInfoExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorInfoExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final Calibrator calibrator;

    public CalibratorInfoExecuter(@Nonnull ApplicationScreen applicationScreen,
                                  @Nonnull RepositoryFactory repositoryFactory,
                                  @Nullable Calibrator calibrator) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.calibrator = calibrator;
    }

    @Override
    public void execute() {
        CalibratorInfoConfigHolder configHolder = new PropertiesCalibratorInfoConfigHolder();
        CalibratorInfoContext context = new CalibratorInfoContext(repositoryFactory);
        CalibratorInfoManager manager = new SwingCalibratorInfoManager(repositoryFactory, context);
        context.registerManager(manager);
        SwingCalibratorInfoDialog dialog = new SwingCalibratorInfoDialog(applicationScreen, configHolder, context, calibrator);
        dialog.showing();

        logger.info("Service is running");
    }
}
