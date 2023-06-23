package service.method_name.ui.swing;

import model.ui.DefaultComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.method_name.MethodNameManager;
import service.method_name.ui.MeasurementNamePanel;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SwingMeasurementNamePanel extends DefaultComboBox implements MeasurementNamePanel {

    public SwingMeasurementNamePanel(@Nonnull RepositoryFactory repositoryFactory,
                                     @Nonnull MethodNameManager manager) {
        super(false);
        MeasurementRepository repository = repositoryFactory.getImplementation(MeasurementRepository.class);

        this.setList(Arrays.asList(repository.getAllNames()));
        this.addItemListener(e -> manager.changeMeasurementName());
    }

    @Override
    public String getMeasurementName() {
        return getSelectedString();
    }
}
