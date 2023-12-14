package service.calibrator.list;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
import model.dto.Calibrator;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.measurement.MeasurementRepository;
import service.calibrator.info.CalibratorInfoExecutor;
import service.calibrator.list.ui.CalibratorListContext;
import service.calibrator.list.ui.CalibratorListMeasurementPanel;
import service.calibrator.list.ui.CalibratorListTable;
import service.calibrator.list.ui.swing.SwingCalibratorListDialog;
import util.ObjectHelper;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class SwingCalibratorListManager implements CalibratorListManager {
    private static final String DELETE_QUESTION = "deleteQuestion";
    private static final String DELETE_SUCCESS = "deleteSuccess";

    private final RepositoryFactory repositoryFactory;
    private final CalibratorListContext context;
    private SwingCalibratorListDialog dialog;

    public SwingCalibratorListManager(@Nonnull RepositoryFactory repositoryFactory,
                                      @Nonnull CalibratorListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
    }

    @Override
    public void changingOfMeasurement() {
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
        CalibratorListMeasurementPanel measurementPanel = context.getElement(CalibratorListMeasurementPanel.class);
        CalibratorListTable table = context.getElement(CalibratorListTable.class);

        List<String> allMeasurements = Arrays.asList(measurementRepository.getAllNames());
        String measurement = measurementPanel.getSelectedMeasurement();
        if (allMeasurements.contains(measurement)) {
            table.setCalibratorList(calibratorRepository.getAll().stream()
                    .filter(c -> c.getMeasurementName().equals(measurement))
                    .collect(Collectors.toList()));
        } else {
            table.setCalibratorList(new ArrayList<>(calibratorRepository.getAll()));
        }
    }

    @Override
    public void clickDelete() {
        Map<String, String> messages = Messages.getMessages(SwingCalibratorListManager.class);
        Map<String, String> labels = Labels.getRootLabels();

        CalibratorListTable table = context.getElement(CalibratorListTable.class);
        String calibratorName = table.getSelectedCalibratorName();
        if (ObjectHelper.nonNull(calibratorName, dialog)) {
            String message = String.format(messages.get(DELETE_QUESTION), calibratorName);
            int result = JOptionPane.showConfirmDialog(dialog, message, labels.get(RootLabelName.DELETING), JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
                if (calibratorRepository.removeByName(calibratorName)) {
                    JOptionPane.showMessageDialog(dialog, messages.get(DELETE_SUCCESS), labels.get(RootLabelName.SUCCESS), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, Messages.getRootMessages().get(RootMessageName.ERROR_TRY_AGAIN), labels.get(RootLabelName.ERROR), JOptionPane.ERROR_MESSAGE);
                }
                dialog.refresh();
            }
        }
    }

    @Override
    public void clickDetails() {
        CalibratorListTable table = context.getElement(CalibratorListTable.class);
        String calibratorName = table.getSelectedCalibratorName();
        if (Objects.nonNull(calibratorName)) {
            CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
            Calibrator calibrator = calibratorRepository.get(calibratorName);
            if (ObjectHelper.nonNull(calibrator, dialog)) {
                new CalibratorInfoExecutor(dialog, repositoryFactory, this, calibrator).execute();
            }
        }
    }

    @Override
    public void clickAdd() {
        if (Objects.nonNull(dialog)) new CalibratorInfoExecutor(dialog, repositoryFactory, this, null).execute();
    }

    @Override
    public void updateDialog() {
        dialog.refresh();
    }

    public void registerDialog(SwingCalibratorListDialog dialog) {
        this.dialog = dialog;
    }
}
