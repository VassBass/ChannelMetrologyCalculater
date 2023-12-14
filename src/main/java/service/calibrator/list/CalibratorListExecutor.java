package service.calibrator.list;

import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.calibrator.list.ui.CalibratorListContext;
import service.calibrator.list.ui.swing.SwingCalibratorListDialog;

import javax.annotation.Nonnull;

public class CalibratorListExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorListExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public CalibratorListExecutor(@Nonnull ApplicationScreen applicationScreen,
                                  @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        CalibratorListConfigHolder configHolder = new PropertiesCalibratorListConfigHolder();
        CalibratorListContext context = new CalibratorListContext(repositoryFactory);
        SwingCalibratorListManager manager = new SwingCalibratorListManager(repositoryFactory, context);
        context.registerManager(manager);
        SwingCalibratorListDialog dialog = new SwingCalibratorListDialog(applicationScreen, repositoryFactory, configHolder, context);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
