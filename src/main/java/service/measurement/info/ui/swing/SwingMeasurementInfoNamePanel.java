package service.measurement.info.ui.swing;

import model.dto.Measurement;
import model.ui.DefaultComboBox;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.measurement.info.MeasurementInfoManager;
import service.measurement.info.ui.MeasurementInfoNamePanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public class SwingMeasurementInfoNamePanel extends DefaultComboBox implements MeasurementInfoNamePanel {

    public SwingMeasurementInfoNamePanel(@Nonnull RepositoryFactory repositoryFactory,
                                         @Nonnull MeasurementInfoManager manager,
                                         @Nullable Measurement oldMeasurement) {
        super(false);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        this.setList(Arrays.asList(measurementRepository.getAllNames()));

        if (Objects.nonNull(oldMeasurement)) {
            this.setSelectedItem(oldMeasurement.getName());
            this.setEnabled(false);
        }

        this.addItemListener(e -> manager.changeName());
    }

    @Override
    public String getMeasurementName() {
        return this.getSelectedString();
    }
}
