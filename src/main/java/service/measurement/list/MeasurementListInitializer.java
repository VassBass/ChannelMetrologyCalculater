package service.measurement.list;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;

public class MeasurementListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementListInitializer.class);

    private static final String MENU_ITEM_MEASUREMENT_LIST_TEXT = "Список вимірювальних величин";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public MeasurementListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(ApplicationMenu.MENU_LISTS);

        JMenuItem list = new JMenuItem(MENU_ITEM_MEASUREMENT_LIST_TEXT);
        list.addActionListener(e -> new MeasurementListExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(ApplicationMenu.MENU_LISTS, list);

        logger.info(("Initialization completed successfully"));
    }
}
