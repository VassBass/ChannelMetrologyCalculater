package service.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import application.ApplicationScreen;
import service.importer.ui.SwingMenuImporter;
import service.root.ServiceInitializer;

import javax.annotation.Nonnull;

public class SwingImporterInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingImporterInitializer.class);

    private final ApplicationScreen applicationScreen;

    public SwingImporterInitializer(@Nonnull ApplicationScreen applicationScreen) {
        this.applicationScreen = applicationScreen;
    }

    @Override
    public void init() {
        applicationScreen.addMenu(new SwingMenuImporter());

        logger.info(("Initialization completed successfully"));
    }
}
