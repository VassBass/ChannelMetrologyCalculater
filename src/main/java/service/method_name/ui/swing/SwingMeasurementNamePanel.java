package service.method_name.ui.swing;

import model.ui.DefaultComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.method_name.ui.MeasurementNamePanel;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SwingMeasurementNamePanel extends DefaultComboBox implements MeasurementNamePanel {

    public SwingMeasurementNamePanel(@Nonnull RepositoryFactory repositoryFactory) {
        super(false);
        MeasurementRepository repository = repositoryFactory.getImplementation(MeasurementRepository.class);

        this.setList(Arrays.asList(repository.getAllNames()));
    }

    @Override
    public String getMeasurementName() {
        return getSelectedString();
    }
}
