package service.sensor_types.list;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;

public class SensorTypesInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SensorTypesInitializer.class);

    private static final String MENU_ITEM_SENSOR_TYPES_LIST_TEXT = "Список типів ПВП";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public SensorTypesInitializer(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(ApplicationMenu.MENU_LISTS);

        JMenuItem list = new JMenuItem(MENU_ITEM_SENSOR_TYPES_LIST_TEXT);
        list.addActionListener(e -> new SensorTypesListExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(ApplicationMenu.MENU_LISTS, list);

        logger.info(("Initialization completed successfully"));
    }
}