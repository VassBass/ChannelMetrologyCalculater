package service.method_name;

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
        Map<String, String> labels = Labels.getRootLabels();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.get(RootLabelName.LISTS));

        JMenuItem list = new JMenuItem(labels.get(RootLabelName.CALCULATION_METHODS));
        list.addActionListener(e -> new MethodNameExecutor(applicationScreen, repositoryFactory).execute());

        applicationMenu.addMenuItem(labels.get(RootLabelName.LISTS), list);

        logger.info(Messages.Log.INIT_SUCCESS);
    }
}
