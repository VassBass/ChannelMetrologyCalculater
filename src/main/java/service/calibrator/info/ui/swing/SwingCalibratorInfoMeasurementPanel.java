package service.calibrator.info.ui.swing;

import model.ui.DefaultComboBox;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import service.calibrator.info.CalibratorInfoManager;
import service.calibrator.info.ui.CalibratorInfoMeasurementPanel;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SwingCalibratorInfoMeasurementPanel extends DefaultPanel implements CalibratorInfoMeasurementPanel {
    private final RepositoryFactory repositoryFactory;
    private final CalibratorInfoManager manager;

    private final DefaultComboBox measurementName;
    private final DefaultComboBox measurementValue;

    public SwingCalibratorInfoMeasurementPanel(@Nonnull RepositoryFactory repositoryFactory, @Nonnull CalibratorInfoManager manager) {
        super();
        this.repositoryFactory = repositoryFactory;
        this.manager = manager;
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        measurementName = new DefaultComboBox(false);
        measurementValue = new DefaultComboBox(false);

        measurementName.setList(Arrays.asList(measurementRepository.getAllNames()));
        updateMeasurementValues();

        measurementName.addItemListener(e -> updateMeasurementValues());
        measurementValue.addItemListener(e -> manager.changingMeasurementValue());

        this.add(measurementName, new CellBuilder().x(0).build());
        this.add(measurementValue, new CellBuilder().x(1).build());
    }

    private void updateMeasurementValues() {
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        String measurementName = this.measurementName.getSelectedString();
        measurementValue.setList(Arrays.asList(measurementRepository.getValues(measurementName)));
    }

    @Override
    public String getMeasurementName() {
        return measurementName.getSelectedString();
    }

    @Override
    public String getMeasurementValue() {
        return measurementValue.getSelectedString();
    }

    @Override
    public void setMeasurementName(String name) {
        measurementName.setSelectedItem(name);
        updateMeasurementValues();
    }

    @Override
    public void setMeasurementValue(String value) {
        measurementValue.setSelectedItem(value);
    }
}
