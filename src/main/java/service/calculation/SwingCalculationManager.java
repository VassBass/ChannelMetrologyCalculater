package service.calculation;

import application.ApplicationScreen;
import repository.RepositoryFactory;
import service.calculation.collect.CalculationCollectDialog;
import service.calculation.collect.condition.SwingCalculationControlConditionExecuter;
import service.calculation.dto.Protocol;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SwingCalculationManager implements CalculationManager {
    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final Protocol protocol;

    private CalculationCollectDialog controlConditionDialog;

    public SwingCalculationManager(@Nonnull ApplicationScreen applicationScreen,
                                   @Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull CalculationConfigHolder configHolder,
                                   @Nonnull Protocol protocol) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
        this.protocol = protocol;
    }

    @Override
    public void registerConditionDialog(@Nonnull CalculationCollectDialog dialog) {
        controlConditionDialog = dialog;
    }

    @Override
    public void showConditionDialog() {
        if (Objects.isNull(controlConditionDialog)) {
            new SwingCalculationControlConditionExecuter(applicationScreen, repositoryFactory, configHolder, this, protocol).execute();
        } else {
            controlConditionDialog.showing();
        }
    }

    @Override
    public void registerInputDialog(@Nonnull CalculationCollectDialog dialog) {

    }

    @Override
    public void showInputDialog() {
        if (Objects.nonNull(controlConditionDialog)) controlConditionDialog.hiding();
    }

    @Override
    public void disposeCalculation() {
        if (Objects.nonNull(controlConditionDialog)) controlConditionDialog.shutdown();
    }
}
