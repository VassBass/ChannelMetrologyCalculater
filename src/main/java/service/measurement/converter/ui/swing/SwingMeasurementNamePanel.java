package service.measurement.converter.ui.swing;

import model.ui.DefaultComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.measurement.converter.ui.MeasurementNamePanel;

import java.util.Arrays;
import java.util.List;

public class SwingMeasurementNamePanel extends DefaultComboBox implements MeasurementNamePanel {

    public SwingMeasurementNamePanel(RepositoryFactory repositoryFactory) {
        super(false);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        List<String> measurementNames = Arrays.asList(measurementRepository.getAllNames());
        this.setList(measurementNames);
    }

    @Override
    public String getMeasurementName() {
        return this.getSelectedString();
    }
}
