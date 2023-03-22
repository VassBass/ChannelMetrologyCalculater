package service.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import application.ApplicationScreen;
import repository.RepositoryFactory;
import service.importer.ui.SwingMenuImporter;
import service.ServiceInitializer;

import javax.annotation.Nonnull;

public class SwingImporterInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingImporterInitializer.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public SwingImporterInitializer(@Nonnull ApplicationScreen applicationScreen, @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        applicationScreen.addMenu(new SwingMenuImporter(applicationScreen, repositoryFactory));

        logger.info(("Initialization completed successfully"));
    }
}
