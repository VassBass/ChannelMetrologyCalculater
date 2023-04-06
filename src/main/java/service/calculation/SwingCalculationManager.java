package service.calculation;

import application.ApplicationScreen;
import model.OS;
import model.dto.Channel;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import service.calculation.condition.SwingCalculationControlConditionExecuter;
import service.calculation.input.SwingCalculationInputExecuter;
import service.calculation.persons.SwingCalculationPersonsExecuter;
import service.calculation.protocol.Protocol;
import service.calculation.protocol.exel.TemplateExelProtocolWrapper;
import service.calculation.result.SwingCalculationResultExecuter;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingCalculationManager implements CalculationManager {
    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final CalculationConfigHolder configHolder;
    private final Channel channel;
    private final Protocol protocol;

    private CalculationCollectDialog controlConditionDialog;
    private CalculationCollectDialog inputDialog;
    private CalculationCollectDialog resultDialog;
    private CalculationCollectDialog personsDialog;

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

        new SwingCalculationResultExecuter(applicationScreen, repositoryFactory, configHolder, this, protocol).execute();
    }

    @Override
    public void registerPersonDialog(@Nonnull CalculationCollectDialog dialog) {
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
                String message = "Виникла помилка при зміні інформації про канал. Будь ласка спробуйте ще раз.";
                JOptionPane.showMessageDialog((Component) personsDialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
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
                String message = "Виникла помилка при зміні інформації про канал. Будь ласка спробуйте ще раз.";
                JOptionPane.showMessageDialog((Component) personsDialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
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
                String message = "Виникла помилка при зміні інформації про канал. Будь ласка спробуйте ще раз.";
                JOptionPane.showMessageDialog((Component) personsDialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
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
