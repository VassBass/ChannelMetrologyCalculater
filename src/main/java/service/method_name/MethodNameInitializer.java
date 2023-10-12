package service.method_name;

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

public class MethodNameInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MethodNameInitializer.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public MethodNameInitializer(@Nonnull ApplicationScreen applicationScreen,
                                 @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        Labels labels = Labels.getInstance();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.lists);

        JMenuItem list = new JMenuItem(labels.calculationMethods);
        list.addActionListener(e -> new MethodNameExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(labels.lists, list);

        logger.info(Messages.Log.INIT_SUCCESS);
    }
}
