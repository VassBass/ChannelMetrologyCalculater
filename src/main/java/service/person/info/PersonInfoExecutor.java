package service.person.info;

import model.dto.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.person.info.ui.PersonInfoContext;
import service.person.info.ui.swing.SwingPersonInfoDialog;
import service.person.list.ui.swing.SwingPersonListDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PersonInfoExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(PersonInfoExecutor.class);

    private final SwingPersonListDialog parentDialog;
    private final RepositoryFactory repositoryFactory;
    private final Person oldPerson;

    public PersonInfoExecutor(@Nonnull SwingPersonListDialog parentDialog,
                              @Nonnull RepositoryFactory repositoryFactory,
                              @Nullable Person oldPerson) {
        this.parentDialog = parentDialog;
        this.repositoryFactory = repositoryFactory;
        this.oldPerson = oldPerson;
    }

    @Override
    public void execute() {
        PersonInfoConfigHolder configHolder = new PropertiesPersonInfoConfigHolder();
        PersonInfoContext context = new PersonInfoContext(repositoryFactory);
        SwingPersonInfoManager manager = new SwingPersonInfoManager(repositoryFactory, parentDialog, context, oldPerson);
        context.registerManager(manager);
        SwingPersonInfoDialog dialog = new SwingPersonInfoDialog(parentDialog, configHolder, context, oldPerson);
        manager.registerDialog(dialog);

        dialog.showing();
        logger.info("Service is running!");
    }
}
