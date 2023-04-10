package service.sensor_error.list.ui.swing;

import model.ui.DefaultComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.sensor_error.list.SensorErrorListManager;
import service.sensor_error.list.ui.SensorErrorListMeasurementPanel;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwingSensorErrorListMeasurementPanel extends DefaultComboBox implements SensorErrorListMeasurementPanel {

    public SwingSensorErrorListMeasurementPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull SensorErrorListManager manager) {
        super(false);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        List<String> measurementsNameList = new ArrayList<>();
        measurementsNameList.add("Всі");
        measurementsNameList.addAll(Arrays.asList(measurementRepository.getAllNames()));
        this.setList(measurementsNameList);
        this.addItemListener(e -> manager.changingOfMeasurementName());
    }

    @Override
    public String getMeasurementName() {
        return this.getSelectedString();
    }
}
