package service.measurement.converter;

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

public class ConverterInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ConverterInitializer.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public ConverterInitializer(@Nonnull ApplicationScreen applicationScreen,
                                @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        Map<String, String> labels = Labels.getRootLabels();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.get(RootLabelName.TOOLS));

        JMenuItem btnConverter = new JMenuItem(labels.get(RootLabelName.CONVERTER));

        btnConverter.addActionListener(e ->
                new ConverterExecutor(applicationScreen, repositoryFactory).execute());
        applicationMenu.addMenuItem(labels.get(RootLabelName.TOOLS), btnConverter);

        logger.info((Messages.Log.INIT_SUCCESS));
    }
}
