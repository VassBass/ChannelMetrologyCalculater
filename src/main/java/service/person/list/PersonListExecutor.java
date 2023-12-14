package service.person.list;

import application.ApplicationScreen;
import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.person.list.ui.PersonListContext;
import service.person.list.ui.swing.SwingPersonListDialog;

import javax.annotation.Nonnull;

public class PersonListExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(PersonListExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;

    public PersonListExecutor(@Nonnull ApplicationScreen applicationScreen,
                              @Nonnull RepositoryFactory repositoryFactory) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void execute() {
        PersonListContext context = new PersonListContext(repositoryFactory);
        SwingPersonListManager manager = new SwingPersonListManager(repositoryFactory, context);
        context.registerManager(manager);
        PersonListConfigHolder configHolder = new PropertiesPersonListConfigHolder();
        SwingPersonListDialog dialog = new SwingPersonListDialog(applicationScreen, context, configHolder);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
