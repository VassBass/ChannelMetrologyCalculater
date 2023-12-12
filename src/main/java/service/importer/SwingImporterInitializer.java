package service.importer;

import application.ApplicationMenu;
import application.ApplicationScreen;
import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;
import service.importer.updater.from_v5.to_v6.From_v5_to_v6_ImporterExecutor;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Map;

public class SwingImporterInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingImporterInitializer.class);

    private static final String IMPORT_FROM_V5_4 = "importFromV5_4";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public SwingImporterInitializer(@Nonnull ApplicationScreen applicationScreen, @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        Map<String, String> labels = Labels.getRootLabels();

        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(labels.get(RootLabelName.IMPORTING));

        JMenuItem btnFrom_v5_4_to_v6_0 = new JMenuItem(Labels.getLabels(SwingImporterInitializer.class).get(IMPORT_FROM_V5_4));

        btnFrom_v5_4_to_v6_0.addActionListener(e ->
                new From_v5_to_v6_ImporterExecutor(applicationScreen, repositoryFactory).execute());
        applicationMenu.addMenuItem(labels.get(RootLabelName.IMPORTING), btnFrom_v5_4_to_v6_0);

        logger.info(Messages.Log.INIT_SUCCESS);
    }
}
