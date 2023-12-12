package service.control_points.list;

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

public class ControlPointsListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ControlPointsListInitializer.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public ControlPointsListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                      @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        Map<String, String> labels = Labels.getRootLabels();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.get(RootLabelName.LISTS));

        JMenuItem list = new JMenuItem(labels.get(RootLabelName.CONTROL_POINTS));
        list.addActionListener(e -> new ControlPointsListExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(labels.get(RootLabelName.LISTS), list);

        logger.info(Messages.Log.INIT_SUCCESS);
    }
}
