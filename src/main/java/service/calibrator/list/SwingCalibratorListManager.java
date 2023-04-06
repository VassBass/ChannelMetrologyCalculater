package service.calibrator.list;

import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.measurement.MeasurementRepository;
import service.calibrator.list.ui.CalibratorListContext;
import service.calibrator.list.ui.CalibratorListMeasurementPanel;
import service.calibrator.list.ui.CalibratorListTable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SwingCalibratorListManager implements CalibratorListManager {
    private final RepositoryFactory repositoryFactory;
    private final CalibratorListContext context;

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

    }

    @Override
    public void clickDetails() {
    }

    @Override
    public void clickAdd() {

    }
}
