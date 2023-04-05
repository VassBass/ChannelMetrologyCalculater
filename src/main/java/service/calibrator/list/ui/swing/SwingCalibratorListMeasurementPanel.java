package service.calibrator.list.ui.swing;

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
    private static final String TITLE_TEXT = "Калібратори для вимірювання:";
    private static final String ALL_TEXT = "Все";

    public SwingCalibratorListMeasurementPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull CalibratorListManager manager) {
        super(false, TITLE_TEXT, Color.BLACK);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        List<String> measurementNames = new ArrayList<>();
        measurementNames.add(ALL_TEXT);
        measurementNames.addAll(Arrays.asList(measurementRepository.getAllNames()));
        this.setList(measurementNames);

        this.addItemListener(e -> manager.changingOfMeasurement());
    }

    @Override
    public String getSelectedMeasurement() {
        return this.getSelectedString();
    }
}
