package service.calculation;

import application.ApplicationScreen;
import model.dto.Channel;
import repository.RepositoryFactory;
import service.calculation.collect.CalculationCollectDialog;
import service.calculation.collect.condition.SwingCalculationControlConditionExecuter;
import service.calculation.collect.input.SwingCalculationInputExecuter;
import service.calculation.dto.Protocol;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SwingCalculationManager implements CalculationManager {
    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final Channel channel;
    private final Protocol protocol;

    private CalculationCollectDialog controlConditionDialog;
    private CalculationCollectDialog inputDialog;

    public SwingCalculationManager(@Nonnull ApplicationScreen applicationScreen,
                                   @Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull CalculationConfigHolder configHolder,
                                   @Nonnull Channel channel) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
        this.channel = channel;
        this.protocol = new Protocol(channel);
    }

    @Override
    public void registerConditionDialog(@Nonnull CalculationCollectDialog dialog) {
        controlConditionDialog = dialog;
    }

    @Override
    public void showConditionDialog() {
        if (Objects.isNull(controlConditionDialog)) {
            new SwingCalculationControlConditionExecuter(applicationScreen, repositoryFactory, configHolder, this, channel).execute();
        } else {
            controlConditionDialog.showing();
        }
    }

    @Override
    public void registerInputDialog(@Nonnull CalculationCollectDialog dialog) {
        inputDialog = dialog;
    }

    @Override
    public void showInputDialog() {
        if (Objects.nonNull(controlConditionDialog) && controlConditionDialog.fillProtocol(protocol)) {
            controlConditionDialog.showing();
            if (Objects.isNull(inputDialog)) {
                new SwingCalculationInputExecuter().execute();
            } else {
                inputDialog.showing();
            }
        }
    }

    @Override
    public void disposeCalculation() {
        if (Objects.nonNull(controlConditionDialog)) controlConditionDialog.shutdown();
        if (Objects.nonNull(inputDialog)) inputDialog.shutdown();
    }
}
