package service.channel.exchange.heroku;

import application.ApplicationMenu;
import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ServiceInitializer;
import service.channel.server.TestExecutor;

import javax.annotation.Nonnull;
import javax.swing.*;

public class HerokuExchangerInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(HerokuExchangerInitializer.class);

    private static final String MENU_ITEM_IMPORT_TEXT = "Імпорт данних КМХ";

    private final ApplicationScreen applicationScreen;

    public HerokuExchangerInitializer(@Nonnull ApplicationScreen applicationScreen) {
        this.applicationScreen = applicationScreen;
    }

    @Override
    public void init() {
        ApplicationMenu applicationMenu = applicationScreen.getMenu();
        applicationMenu.addMenuIfNotExist(ApplicationMenu.MENU_IMPORT);

        JMenuItem test = new JMenuItem(MENU_ITEM_IMPORT_TEXT);
        test.addActionListener(e -> new TestExecutor(applicationScreen).execute());

        applicationMenu.addMenuItem(ApplicationMenu.MENU_IMPORT, test);

        logger.info(("Initialization completed successfully"));
    }
}
