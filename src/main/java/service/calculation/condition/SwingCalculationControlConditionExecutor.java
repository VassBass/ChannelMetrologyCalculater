package service.calculation.condition;

import application.ApplicationScreen;
import localization.Messages;
import model.dto.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.ServiceExecutor;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.condition.ui.SwingCalculationControlConditionContext;
import service.calculation.condition.ui.swing.SwingCalculationControlConditionDialog;

import javax.annotation.Nonnull;

public class SwingCalculationControlConditionExecutor implements ServiceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationControlConditionExecutor.class);

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final CalculationManager manager;
    private final Channel channel;

    public SwingCalculationControlConditionExecutor(@Nonnull ApplicationScreen applicationScreen,
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
        SwingCalculationControlConditionContext context = new SwingCalculationControlConditionContext();
        context.registerManager(manager);

        SwingCalculationControlConditionDialog dialog = new SwingCalculationControlConditionDialog(
                applicationScreen, repositoryFactory, configHolder, context, channel
        );
        manager.registerConditionDialog(dialog);
        manager.showConditionDialog();

        logger.info(Messages.Log.SERVICE_RUNNING);
    }
}
