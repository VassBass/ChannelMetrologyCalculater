package service.measurement.list.ui.swing;

import model.ui.DefaultComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.measurement.list.MeasurementListManager;
import service.measurement.list.ui.MeasurementListNamePanel;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SwingMeasurementListNamePanel extends DefaultComboBox implements MeasurementListNamePanel {

    public SwingMeasurementListNamePanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull MeasurementListManager manager) {
        super(false);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        this.setList(Arrays.asList(measurementRepository.getAllNames()));
        this.addItemListener(e -> manager.changeMeasurementName());
    }

    @Override
    public String getSelectedName() {
        return this.getSelectedString();
    }
}
