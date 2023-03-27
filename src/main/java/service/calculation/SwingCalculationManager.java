package service.calculation;

import application.ApplicationScreen;
import model.dto.Channel;
import repository.RepositoryFactory;
import service.calculation.condition.SwingCalculationControlConditionExecuter;
import service.calculation.input.SwingCalculationInputExecuter;
import service.calculation.dto.Protocol;
import service.calculation.result.SwingCalculationResultExecuter;

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
    private CalculationCollectDialog resultDialog;

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
        if (Objects.nonNull(inputDialog)) inputDialog.hiding();

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
        if (Objects.nonNull(resultDialog)) resultDialog.hiding();

        if (Objects.nonNull(controlConditionDialog) && controlConditionDialog.isVisible()) {
            if (controlConditionDialog.fillProtocol(protocol)) {
                controlConditionDialog.hiding();
            } else {
                controlConditionDialog.refresh();
                return;
            }
        }


        if (Objects.isNull(inputDialog)) {
            new SwingCalculationInputExecuter(applicationScreen, repositoryFactory, configHolder, this, channel).execute();
        } else {
            inputDialog.showing();
        }
    }

    @Override
    public void registerResultDialog(@Nonnull CalculationCollectDialog dialog) {
        resultDialog = dialog;
    }

    @Override
    public void showResultDialog() {
        if (Objects.nonNull(inputDialog) && inputDialog.isVisible()) {
            if (inputDialog.fillProtocol(protocol)) {
                inputDialog.hiding();
            }else {
                inputDialog.refresh();
                return;
            }
        }

        if (Objects.isNull(resultDialog)) {
            new SwingCalculationResultExecuter().execute();
        } else {
            resultDialog.showing();
        }
    }

    @Override
    public void disposeCalculation() {
        if (Objects.nonNull(controlConditionDialog)) controlConditionDialog.shutdown();
        if (Objects.nonNull(inputDialog)) inputDialog.shutdown();
        if (Objects.nonNull(resultDialog)) resultDialog.shutdown();
    }
}
