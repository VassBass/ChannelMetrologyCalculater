package service.calculation.input;

import application.ApplicationScreen;
import model.dto.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecuter;
import service.calculation.CalculationCollectDialog;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.input.ui.SwingCalculationInputContext;
import service.calculation.input.ui.swing.SwingCalculationInputDialog;

import javax.annotation.Nonnull;

public class SwingCalculationInputExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationInputExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final CalculationManager manager;
    private final Channel channel;

    public SwingCalculationInputExecuter(@Nonnull ApplicationScreen applicationScreen,
                                         @Nonnull RepositoryFactory repositoryFactory,
                                         @Nonnull CalculationConfigHolder configHolder,
                                         @Nonnull CalculationManager manager,
                                         @Nonnull Channel channel) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
        this.manager = manager;
        this.channel = channel;
    }

    @Override
    public void execute() {
        SwingCalculationInputContext context = new SwingCalculationInputContext(repositoryFactory, channel);
        context.registerManager(manager);
        CalculationCollectDialog inputDialog = new SwingCalculationInputDialog(applicationScreen, configHolder,  context);
        manager.registerInputDialog(inputDialog);
        manager.showInputDialog();
        logger.info("Service is running");
    }
}
