package service.calibrator.list.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.TitledComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.calibrator.list.CalibratorListManager;
import service.calibrator.list.ui.CalibratorListMeasurementPanel;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwingCalibratorListMeasurementPanel extends TitledComboBox implements CalibratorListMeasurementPanel {
    private static final String CALIBRATORS_FOR_MEASUREMENT = "calibratorsForMeasurement";

    public SwingCalibratorListMeasurementPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull CalibratorListManager manager) {
        super(
                false,
                Labels.getLabels(SwingCalibratorListMeasurementPanel.class).get(CALIBRATORS_FOR_MEASUREMENT) + Labels.COLON,
                Color.BLACK);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        List<String> measurementNames = new ArrayList<>();
        measurementNames.add(Labels.getRootLabels().get(RootLabelName.ALL));
        measurementNames.addAll(Arrays.asList(measurementRepository.getAllNames()));
        this.setList(measurementNames);

        this.addItemListener(e -> manager.changingOfMeasurement());
    }

    @Override
    public String getSelectedMeasurement() {
        return this.getSelectedString();
    }
}
