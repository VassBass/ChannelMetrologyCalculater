package service.calculation.input;

import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.input.ui.SwingCalculationInputContext;
import service.calculation.input.ui.swing.SwingCalculationInputDialog;
import service.calculation.protocol.Protocol;

import javax.annotation.Nonnull;

public class SwingCalculationInputExecuter implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationInputExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final CalculationManager manager;
    private final Protocol protocol;

    public SwingCalculationInputExecuter(@Nonnull ApplicationScreen applicationScreen,
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
        SwingCalculationInputContext context = new SwingCalculationInputContext(repositoryFactory, protocol);
        context.registerManager(manager);
        SwingCalculationInputManager inputManager = new SwingCalculationInputManager(context);
        context.registerManager(inputManager);
        SwingCalculationInputDialog inputDialog = new SwingCalculationInputDialog(applicationScreen, configHolder,  context);
        manager.registerInputDialog(inputDialog);
        manager.showInputDialog();
        logger.info("Service is running");
    }
}
