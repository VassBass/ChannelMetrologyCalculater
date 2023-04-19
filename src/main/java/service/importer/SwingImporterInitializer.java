package service.importer;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;
import service.importer.updater.from_v5.to_v6.From_v5_to_v6_ImporterExecuter;

import javax.annotation.Nonnull;
import javax.swing.*;

public class SwingImporterInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingImporterInitializer.class);

    private static final String MENU_HEADER_TEXT = "Імпорт";
    private static final String MENU_ITEM_FROM_v5_4_TO_v6_0_TEXT = "Імпортувати з версії 5.4";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public SwingImporterInitializer(@Nonnull ApplicationScreen applicationScreen, @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(MENU_HEADER_TEXT);

        JMenuItem btnFrom_v5_4_to_v6_0 = new JMenuItem(MENU_ITEM_FROM_v5_4_TO_v6_0_TEXT);

        btnFrom_v5_4_to_v6_0.addActionListener(e ->
                new From_v5_to_v6_ImporterExecuter(applicationScreen, repositoryFactory).execute());
        applicationMenu.addMenuItem(MENU_HEADER_TEXT, btnFrom_v5_4_to_v6_0);

        logger.info(("Initialization completed successfully"));
    }
}
