package service.sensor_types.list.ui.swing;

import model.ui.DefaultComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.sensor_types.list.SensorTypesListManager;
import service.sensor_types.list.ui.SensorTypesListMeasurementPanel;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SwingSensorTypesListMeasurementPanel extends DefaultComboBox implements SensorTypesListMeasurementPanel {

    public SwingSensorTypesListMeasurementPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull SensorTypesListManager manager) {
        super(false);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        this.setList(Arrays.asList(measurementRepository.getAllNames()));
        this.addItemListener(e -> manager.changeMeasurement());
    }

    @Override
    public String getSelectedMeasurementName() {
        return this.getSelectedString();
    }
}
