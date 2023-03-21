package service.calculation;

import model.dto.Channel;
import repository.RepositoryFactory;
import service.calculation.collect.condition.SwingCalculationControlConditionInitializer;
import service.calculation.collect.condition.ui.swing.SwingCalculationControlConditionDialog;

import javax.annotation.Nonnull;
import java.awt.*;

public class SwingCalculationManager implements CalculationManager {
    private final Frame owner;
    private final Channel channel;
    private final CalculationConfigHolder configHolder;
    private final RepositoryFactory repositoryFactory;

    private SwingCalculationControlConditionDialog controlConditionDialog;

    public SwingCalculationManager(@Nonnull Frame owner,
                                   @Nonnull Channel channel,
                                   CalculationConfigHolder configHolder,
                                   RepositoryFactory repositoryFactory) {
        this.owner = owner;
        this.channel = channel;
        this.configHolder = configHolder;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void showConditionDialog() {
        if (controlConditionDialog == null){
            controlConditionDialog = new SwingCalculationControlConditionDialog(owner, channel.getName());
            new SwingCalculationControlConditionInitializer(
                    configHolder, controlConditionDialog, repositoryFactory, this, channel).init();
        }
        controlConditionDialog.showing();
    }

    @Override
    public void showInputDialog() {
        if (controlConditionDialog != null) controlConditionDialog.hiding();
    }

    @Override
    public void disposeCalculation() {
        if (controlConditionDialog != null) controlConditionDialog.shutdown();
    }
}
