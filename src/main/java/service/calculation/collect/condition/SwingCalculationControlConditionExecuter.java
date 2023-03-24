package service.calculation.collect.condition;

import application.ApplicationScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecuter;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.collect.condition.ui.SwingCalculationControlConditionContext;
import service.calculation.collect.condition.ui.swing.SwingCalculationControlConditionDialog;
import service.calculation.dto.Protocol;

import javax.annotation.Nonnull;

public class SwingCalculationControlConditionExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationControlConditionExecuter.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final CalculationManager manager;
    private final Protocol protocol;

    public SwingCalculationControlConditionExecuter(@Nonnull ApplicationScreen applicationScreen,
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
        SwingCalculationControlConditionContext context = new SwingCalculationControlConditionContext();
        context.registerManager(manager);

        SwingCalculationControlConditionDialog dialog = new SwingCalculationControlConditionDialog(
                applicationScreen, repositoryFactory, configHolder, context, protocol
        );
        manager.registerConditionDialog(dialog);
        manager.showConditionDialog();

        logger.info("Service is running");
    }
}
