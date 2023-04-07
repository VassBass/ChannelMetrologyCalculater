package service.calibrator.list;

import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecuter;
import service.calibrator.list.ui.CalibratorListContext;
import service.calibrator.list.ui.swing.SwingCalibratorListDialog;

import javax.annotation.Nonnull;

public class CalibratorListExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorListExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public CalibratorListExecuter(@Nonnull ApplicationScreen applicationScreen,
                                  @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        CalibratorListConfigHolder configHolder = new PropertiesCalibratorListConfigHolder();
        CalibratorListContext context = new CalibratorListContext(repositoryFactory);
        CalibratorListManager manager = new SwingCalibratorListManager(repositoryFactory, context);
        context.registerManager(manager);
        SwingCalibratorListDialog dialog = new SwingCalibratorListDialog(applicationScreen, repositoryFactory, configHolder, context);

        dialog.showing();
        logger.info("Service is running");
    }
}
