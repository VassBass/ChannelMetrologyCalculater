package service.measurement.converter;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;

public class ConverterInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ConverterInitializer.class);

    private static final String MENU_HEADER_TEXT = "Інструменти";
    private static final String MENU_ITEM_CONVERTER_TEXT = "Перетворювач величин";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public ConverterInitializer(@Nonnull ApplicationScreen applicationScreen,
                                @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(MENU_HEADER_TEXT);

        JMenuItem btnConverter = new JMenuItem(MENU_ITEM_CONVERTER_TEXT);

        btnConverter.addActionListener(e ->
                new ConverterExecutor(applicationScreen, repositoryFactory).execute());
        applicationMenu.addMenuItem(MENU_HEADER_TEXT, btnConverter);

        logger.info(("Initialization completed successfully"));
    }
}
