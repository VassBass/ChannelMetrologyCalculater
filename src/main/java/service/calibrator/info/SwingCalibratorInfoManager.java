package service.calibrator.info;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
import model.dto.Calibrator;
import model.dto.builder.CalibratorBuilder;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import service.calibrator.info.ui.*;
import service.calibrator.info.ui.swing.SwingCalibratorInfoDialog;
import service.calibrator.list.CalibratorListManager;
import util.DoubleHelper;
import util.ObjectHelper;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Map;
import java.util.Objects;

public class SwingCalibratorInfoManager implements CalibratorInfoManager {
    private static final String CALIBRATOR_DELETE_QUESTION = "calibratorDeleteQuestion";
    private static final String CALIBRATOR_EXISTS = "calibratorExists";

    private final RepositoryFactory repositoryFactory;
    private final CalibratorInfoContext context;
    private final CalibratorListManager parentManager;
    private final Calibrator calibrator;
    private SwingCalibratorInfoDialog dialog;

    private final Map<String, String> messages;
    private final Map<String, String> rootMessages;
    private final Map<String, String> labels;

    public SwingCalibratorInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                      @Nonnull CalibratorInfoContext context,
                                      @Nonnull CalibratorListManager parentManager,
                                      @Nullable Calibrator calibrator) {
        messages = Messages.getMessages(SwingCalibratorInfoManager.class);
        rootMessages = Messages.getRootMessages();
        labels = Labels.getRootLabels();

        this.repositoryFactory = repositoryFactory;
        this.context = context;
        this.parentManager = parentManager;
        this.calibrator = calibrator;
    }

    @Override
    public void changingMeasurementValue() {
        CalibratorInfoMeasurementPanel measurementPanel = context.getElement(CalibratorInfoMeasurementPanel.class);
        CalibratorInfoRangePanel rangePanel = context.getElement(CalibratorInfoRangePanel.class);
        rangePanel.setMeasurementValue(measurementPanel.getMeasurementValue());
    }

    @Override
    public void copyTypeToNameField() {
        CalibratorInfoTypePanel typePanel = context.getElement(CalibratorInfoTypePanel.class);
        CalibratorInfoNamePanel namePanel = context.getElement(CalibratorInfoNamePanel.class);
        String currentName = namePanel.getCalibratorName();
        String type = typePanel.getType();
        namePanel.setCalibratorName(currentName + type);
    }

    @Override
    public void saveCalibrator() {
        CalibratorRepository repository = repositoryFactory.getImplementation(CalibratorRepository.class);
        CalibratorInfoTypePanel typePanel = context.getElement(CalibratorInfoTypePanel.class);
        CalibratorInfoNamePanel namePanel = context.getElement(CalibratorInfoNamePanel.class);
        CalibratorInfoMeasurementPanel measurementPanel = context.getElement(CalibratorInfoMeasurementPanel.class);
        CalibratorInfoRangePanel rangePanel = context.getElement(CalibratorInfoRangePanel.class);
        CalibratorInfoNumberPanel numberPanel = context.getElement(CalibratorInfoNumberPanel.class);
        CalibratorInfoErrorFormulaPanel errorFormulaPanel = context.getElement(CalibratorInfoErrorFormulaPanel.class);
        CalibratorInfoCertificatePanel certificatePanel = context.getElement(CalibratorInfoCertificatePanel.class);

        String type = typePanel.getType();
        String name = namePanel.getCalibratorName();
        String measurementName = measurementPanel.getMeasurementName();
        String measurementValue = measurementPanel.getMeasurementValue();
        double rangeMin = rangePanel.getRangeMin();
        double rangeMax = rangePanel.getRangeMax();
        String number = numberPanel.getNumber();
        String errorFormula = errorFormulaPanel.getErrorFormula();
        Calibrator.Certificate certificate = certificatePanel.getCertificate();

        if (StringHelper.nonEmpty(type, name, errorFormula) &
                DoubleHelper.nonNaN(rangeMin, rangeMax) &
                Objects.nonNull(certificate)) {
            Calibrator calibrator = new CalibratorBuilder(name)
                    .setType(type)
                    .setMeasurementName(measurementName)
                    .setMeasurementValue(measurementValue)
                    .setRange(rangeMin, rangeMax)
                    .setNumber(number)
                    .setErrorFormula(errorFormula)
                    .setCertificate(certificate)
                    .build();
            if (Objects.isNull(this.calibrator)) {
                if (repository.isExists(name)) {
                    existAction();
                } else {
                    if (repository.add(calibrator)) {
                        successAction();
                    } else {
                        errorAction();
                    }
                }
            } else {
                if (repository.isExist(this.calibrator.getName(), name)) {
                    existAction();
                } else {
                    if (repository.set(this.calibrator, calibrator)) {
                        successAction();
                    } else {
                        errorAction();
                    }
                }
            }
        } else {
            if (Objects.nonNull(dialog)) dialog.refresh();
        }
    }

    @Override
    public void clickCloseDialog() {
        if (Objects.nonNull(dialog)) dialog.shutdown();
    }

    @Override
    public void clickRemove() {
        if (ObjectHelper.nonNull(calibrator, dialog)) {
            String name = calibrator.getName();
            String title = labels.get(RootLabelName.DELETING);
            String messageTemplate = messages.get(CALIBRATOR_DELETE_QUESTION);
            String message = String.format(messageTemplate, name);
            int result = JOptionPane.showConfirmDialog(dialog, message, title, JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
                if (calibratorRepository.removeByName(name)) {
                    successAction();
                    dialog.shutdown();
                    parentManager.updateDialog();
                } else {
                    errorAction();
                }
            }
        } else {
            clickCloseDialog();
        }
    }

    private void existAction() {
        if (Objects.nonNull(dialog)) {
            String message = messages.get(CALIBRATOR_EXISTS);
            String title = labels.get(RootLabelName.ERROR);
            JOptionPane.showMessageDialog(dialog, message, title, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void errorAction() {
        if (Objects.nonNull(dialog)) {
            String message = rootMessages.get(RootMessageName.ERROR_TRY_AGAIN);
            String title = labels.get(RootLabelName.ERROR);
            JOptionPane.showMessageDialog(dialog, message, title, JOptionPane.ERROR_MESSAGE);
            parentManager.updateDialog();
        }
    }

    private void successAction() {
        if (Objects.nonNull(dialog)) {
            String message = rootMessages.get(RootMessageName.OPERATION_SUCCESS);
            String title = labels.get(RootLabelName.SUCCESS);
            JOptionPane.showMessageDialog(dialog, message, title, JOptionPane.INFORMATION_MESSAGE);
            dialog.shutdown();
            parentManager.updateDialog();
        }
    }

    public void registerDialog(SwingCalibratorInfoDialog dialog) {
        this.dialog = dialog;
    }
}
