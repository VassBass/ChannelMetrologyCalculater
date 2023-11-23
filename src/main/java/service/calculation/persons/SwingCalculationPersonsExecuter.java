package service.calculation.persons;

import application.ApplicationScreen;
import localization.message.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.protocol.Protocol;
import service.calculation.persons.ui.SwingCalculationPersonsContext;
import service.calculation.persons.ui.swing.SwingCalculationPersonsDialog;

import javax.annotation.Nonnull;

public class SwingCalculationPersonsExecuter implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationPersonsExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final CalculationManager manager;
    private final Protocol protocol;

    public SwingCalculationPersonsExecuter(@Nonnull ApplicationScreen applicationScreen,
                                           @Nonnull RepositoryFactory repositoryFactory,
                                           @Nonnull CalculationConfigHolder configHolder,
                                           @Nonnull CalculationManager manager,
                                           @Nonnull Protocol protocol) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
        this.manager = manager;
        this.protocol = protocol;
    }

    @Override
    public void execute() {
        SwingCalculationPersonsContext context = new SwingCalculationPersonsContext(repositoryFactory, protocol);
        context.registerManager(manager);
        SwingCalculationPersonsDialog dialog = new SwingCalculationPersonsDialog(applicationScreen, configHolder, manager, context, protocol);
        manager.registerPersonDialog(dialog);
        dialog.showing();
        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
