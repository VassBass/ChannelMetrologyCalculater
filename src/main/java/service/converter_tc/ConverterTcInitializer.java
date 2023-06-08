package service.converter_tc;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceInitializer;

import javax.annotation.Nonnull;
import javax.swing.*;

public class ConverterTcInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ConverterTcInitializer.class);

    private static final String MENU_HEADER_TEXT = "Інструменти";
    private static final String MENU_ITEM_CONVERTER_TC_TEXT = "Перетворювач величин ТО";

    private final ApplicationScreen applicationScreen;

    public ConverterTcInitializer(@Nonnull ApplicationScreen applicationScreen) {
        this.applicationScreen = applicationScreen;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(MENU_HEADER_TEXT);

        JMenuItem btnConverterTc = new JMenuItem(MENU_ITEM_CONVERTER_TC_TEXT);

        btnConverterTc.addActionListener(e ->
                new ConverterTcExecutor(applicationScreen).execute());
        applicationMenu.addMenuItem(MENU_HEADER_TEXT, btnConverterTc);

        logger.info(("Initialization completed successfully"));
    }
}
