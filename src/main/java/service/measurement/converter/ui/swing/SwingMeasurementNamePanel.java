package service.measurement.converter.ui.swing;

import model.ui.DefaultComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.measurement.converter.ConverterManager;
import service.measurement.converter.ui.MeasurementNamePanel;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class SwingMeasurementNamePanel extends DefaultComboBox implements MeasurementNamePanel {

    public SwingMeasurementNamePanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull ConverterManager manager) {
        super(false);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        List<String> measurementNames = Arrays.asList(measurementRepository.getAllNames());
        this.setList(measurementNames);
        this.addItemListener(e -> manager.updateMeasurementValues());
    }

    @Override
    public String getMeasurementName() {
        return this.getSelectedString();
    }
}
