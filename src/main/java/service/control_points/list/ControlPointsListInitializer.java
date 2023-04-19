package service.control_points.list;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;

public class ControlPointsListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ControlPointsListInitializer.class);

    private static final String MENU_ITEM_CONTROL_POINTS_LIST_TEXT = "Контрольні точки вимірюваннь";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public ControlPointsListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(ApplicationMenu.MENU_LISTS);

        JMenuItem list = new JMenuItem(MENU_ITEM_CONTROL_POINTS_LIST_TEXT);
        list.addActionListener(e -> new ControlPointsListExecuter(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(ApplicationMenu.MENU_LISTS, list);

        logger.info(("Initialization completed successfully"));
    }
}
