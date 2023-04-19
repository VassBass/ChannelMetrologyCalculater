package service.calibrator.list;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;

public class CalibratorListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorListInitializer.class);

    private static final String MENU_ITEM_CALIBRATOR_LIST_TEXT = "Список калібраторів";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public CalibratorListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                    @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(ApplicationMenu.MENU_LISTS);

        JMenuItem list = new JMenuItem(MENU_ITEM_CALIBRATOR_LIST_TEXT);
        list.addActionListener(e -> new CalibratorListExecuter(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(ApplicationMenu.MENU_LISTS, list);

        logger.info(("Initialization completed successfully"));
    }
}
