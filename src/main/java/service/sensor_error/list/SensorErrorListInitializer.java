package service.sensor_error.list;

import application.ApplicationMenu;
import application.ApplicationScreen;
import localization.label.Labels;
import localization.message.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;

public class SensorErrorListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SensorErrorListInitializer.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public SensorErrorListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                     @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        Labels labels = Labels.getInstance();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.lists);

        JMenuItem list = new JMenuItem(labels.sensorsErrorList);
        list.addActionListener(e -> new SensorErrorListExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(labels.lists, list);

        logger.info(Messages.Log.INIT_SUCCESS);
    }
}
