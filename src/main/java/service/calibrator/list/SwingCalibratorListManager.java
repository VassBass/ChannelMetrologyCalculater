package service.calibrator.list;

import model.dto.Calibrator;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.measurement.MeasurementRepository;
import service.calibrator.info.CalibratorInfoExecuter;
import service.calibrator.list.ui.CalibratorListContext;
import service.calibrator.list.ui.CalibratorListMeasurementPanel;
import service.calibrator.list.ui.CalibratorListTable;
import service.calibrator.list.ui.swing.SwingCalibratorListDialog;
import util.ObjectHelper;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwingCalibratorListManager implements CalibratorListManager {
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
        CalibratorListTable table = context.getElement(CalibratorListTable.class);
        String calibratorName = table.getSelectedCalibratorName();
        if (ObjectHelper.nonNull(calibratorName, dialog)) {
            String message = String.format("Ви впевнені що хочете видалити калібратор \"%s\"?", calibratorName);
            int result = JOptionPane.showConfirmDialog(dialog, message, "Видалення", JOptionPane.YES_NO_OPTION);
            if (result == 0) {
                CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
                if (calibratorRepository.removeByName(calibratorName)) {
                    JOptionPane.showMessageDialog(dialog, "Калібратор успішно видалено", "Успіх", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Виникла помилка. Спробуйте ще.", "Помилка", JOptionPane.ERROR_MESSAGE);
                }
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
                new CalibratorInfoExecuter(dialog, repositoryFactory, this, calibrator).execute();
            }
        }
    }

    @Override
    public void clickAdd() {
        if (Objects.nonNull(dialog)) new CalibratorInfoExecuter(dialog, repositoryFactory, this, null).execute();
    }

    @Override
    public void updateDialog() {
        dialog.refresh();
    }

    public void registerDialog(SwingCalibratorListDialog dialog) {
        this.dialog = dialog;
    }
}
