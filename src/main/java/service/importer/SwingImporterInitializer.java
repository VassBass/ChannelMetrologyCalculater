package service.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import application.ApplicationScreen;
import service.importer.ui.SwingMenuImporter;
import service.root.ServiceInitializer;

public class SwingImporterInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingImporterInitializer.class);

    @Override
    public void init() {
        ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
        if (applicationScreen != null) {
            applicationScreen.addMenu(new SwingMenuImporter());
        }

        logger.info(("Initialization completed successfully"));
    }
}
