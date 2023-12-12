package service.sensor_error.list;

import application.ApplicationMenu;
import application.ApplicationScreen;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Map;

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
        Map<String, String> labels = Labels.getRootLabels();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.get(RootLabelName.LISTS));

        JMenuItem list = new JMenuItem(labels.get(RootLabelName.SENSOR_ERRORS_LIST));
        list.addActionListener(e -> new SensorErrorListExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(labels.get(RootLabelName.LISTS), list);

        logger.info(Messages.Log.INIT_SUCCESS);
    }
}
