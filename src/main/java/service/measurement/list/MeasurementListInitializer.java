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

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public MeasurementListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        Labels labels = Labels.getInstance();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.lists);

        JMenuItem list = new JMenuItem(labels.measurementsList);
        list.addActionListener(e -> new MeasurementListExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(labels.lists, list);

        logger.info(Messages.Log.INIT_SUCCESS);
    }
}
