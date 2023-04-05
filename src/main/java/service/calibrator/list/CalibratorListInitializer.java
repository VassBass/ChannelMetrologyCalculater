package service.calibrator.list;

import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;

public class CalibratorListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorListInitializer.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public CalibratorListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                    @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        applicationScreen.addMenu(new SwingMenuCalibratorList(applicationScreen, repositoryFactory));
        logger.info(("Initialization completed successfully"));
    }
}
