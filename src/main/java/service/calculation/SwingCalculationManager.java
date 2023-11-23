package service.calculation;

import application.ApplicationScreen;
import localization.label.Labels;
import localization.message.Messages;
import model.OS;
import model.dto.Channel;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import service.calculation.condition.SwingCalculationControlConditionExecuter;
import service.calculation.condition.ui.swing.SwingCalculationControlConditionDialog;
import service.calculation.input.SwingCalculationInputExecuter;
import service.calculation.input.ui.swing.SwingCalculationInputDialog;
import service.calculation.persons.SwingCalculationPersonsExecuter;
import service.calculation.persons.ui.swing.SwingCalculationPersonsDialog;
import service.calculation.protocol.Protocol;
import service.calculation.protocol.exel.TemplateExelProtocolWrapper;
import service.calculation.result.SwingCalculationResultExecuter;
import service.calculation.result.ui.swing.SwingCalculationResultDialog;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingCalculationManager implements CalculationManager {
    private final Labels labels;
    private final Messages messages;

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final Channel channel;
    private final Protocol protocol;

    private SwingCalculationControlConditionDialog controlConditionDialog;
    private SwingCalculationInputDialog inputDialog;
    private SwingCalculationResultDialog resultDialog;
    private SwingCalculationPersonsDialog personsDialog;

    public SwingCalculationManager(@Nonnull ApplicationScreen applicationScreen,
                                   @Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull CalculationConfigHolder configHolder,
                                   @Nonnull Channel channel) {
        labels = Labels.getInstance();
        messages = Messages.getInstance();

        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.configHolder = configHolder;
        this.channel = channel;
        this.protocol = new Protocol(channel);
    }

    @Override
    public void registerConditionDialog(@Nonnull SwingCalculationControlConditionDialog dialog) {
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
    public void registerInputDialog(@Nonnull SwingCalculationInputDialog dialog) {
        inputDialog = dialog;
    }

    @Override
    public void showInputDialog() {
        if (Objects.nonNull(resultDialog)) resultDialog.shutdown();
        if (Objects.nonNull(personsDialog)) personsDialog.shutdown();

        if (Objects.nonNull(controlConditionDialog) && controlConditionDialog.isVisible()) {
            if (controlConditionDialog.fillProtocol(protocol)) {
                controlConditionDialog.hiding();
            } else {
                controlConditionDialog.refresh();
                return;
            }
        }

        if (Objects.isNull(inputDialog)) {
            new SwingCalculationInputExecuter(applicationScreen, repositoryFactory, configHolder, this, protocol).execute();
        } else {
            inputDialog.showing();
        }
    }

    @Override
    public void registerResultDialog(@Nonnull SwingCalculationResultDialog dialog) {
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

        new SwingCalculationResultExecuter(applicationScreen, repositoryFactory, configHolder, this, protocol).execute();
    }

    @Override
    public void registerPersonDialog(@Nonnull SwingCalculationPersonsDialog dialog) {
        personsDialog = dialog;
    }

    @Override
    public void showPersonDialog() {
        if (Objects.nonNull(resultDialog)) {
            if (resultDialog.fillProtocol(protocol)) {
                resultDialog.shutdown();
            } else return;
        }

        new SwingCalculationPersonsExecuter(applicationScreen, repositoryFactory, configHolder, this, protocol ).execute();
    }

    @Override
    public void printProtocol() {
        if (Objects.nonNull(personsDialog)) {
            personsDialog.fillProtocol(protocol);
            if (saveChangedChannel(protocol)) {
                OS os = protocol.getOs();
                new TemplateExelProtocolWrapper(repositoryFactory, configHolder, os).wrap(protocol).print();
                disposeCalculation();
            } else {
                JOptionPane.showMessageDialog(personsDialog, messages.modifyChannel_error, labels.error, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void openProtocol() {
        if (Objects.nonNull(personsDialog)) {
            personsDialog.fillProtocol(protocol);
            if (saveChangedChannel(protocol)) {
                OS os = protocol.getOs();
                new TemplateExelProtocolWrapper(repositoryFactory, configHolder, os).wrap(protocol).open();
                disposeCalculation();
            } else {
                JOptionPane.showMessageDialog(personsDialog, messages.modifyChannel_error, labels.error, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void endCalculation() {
        if (Objects.nonNull(personsDialog)) {
            personsDialog.fillProtocol(protocol);
            if (saveChangedChannel(protocol)) {
                OS os = protocol.getOs();
                new TemplateExelProtocolWrapper(repositoryFactory, configHolder, os).wrap(protocol).save();
                disposeCalculation();
            } else {
                JOptionPane.showMessageDialog(personsDialog, messages.modifyChannel_error, labels.error, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void disposeCalculation() {
        if (Objects.nonNull(controlConditionDialog)) controlConditionDialog.shutdown();
        if (Objects.nonNull(inputDialog)) inputDialog.shutdown();
        if (Objects.nonNull(resultDialog)) resultDialog.shutdown();
        if (Objects.nonNull(personsDialog)) personsDialog.shutdown();
    }

    private boolean saveChangedChannel(Protocol protocol) {
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);

        channel.setDate(protocol.getDate());
        channel.setNumberOfProtocol(protocol.getNumber());
        String reference = protocol.getReferenceNumber();
        channel.setReference(Objects.isNull(reference) ? EMPTY : reference);
        channel.setSuitability(Objects.isNull(reference));

        return channelRepository.set(channel, channel);
    }
}
