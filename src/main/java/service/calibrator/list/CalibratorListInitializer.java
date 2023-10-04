package service.calibrator.list;

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

public class CalibratorListInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorListInitializer.class);
    private final Labels labels;
    private final Messages messages;

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public CalibratorListInitializer(@Nonnull ApplicationScreen applicationScreen,
                                    @Nonnull RepositoryFactory repositoryFactory) {
        labels = Labels.getInstance();
        messages = Messages.getInstance();

        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.lists);

        JMenuItem list = new JMenuItem(labels.calibratorsList);
        list.addActionListener(e -> new CalibratorListExecuter(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(labels.lists, list);

        logger.info((messages.init_success));
    }
}
