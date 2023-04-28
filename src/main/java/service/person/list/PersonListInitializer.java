package service.person.list;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;
import service.sensor_error.list.SensorErrorListExecutor;
import service.sensor_error.list.SensorErrorListInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;

public class PersonListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(PersonListInitializer.class);

    private static final String MENU_ITEM_PERSON_LIST_TEXT = "Список персоналу";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public PersonListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                 @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(ApplicationMenu.MENU_LISTS);

        JMenuItem list = new JMenuItem(MENU_ITEM_PERSON_LIST_TEXT);
        list.addActionListener(e -> new PersonListExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(ApplicationMenu.MENU_LISTS, list);

        logger.info(("Initialization completed successfully"));
    }
}
