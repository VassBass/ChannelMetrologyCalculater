package service.sensor_error.list;

import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.sensor_error.list.ui.SensorErrorListContext;
import service.sensor_error.list.ui.SensorErrorListMeasurementPanel;
import service.sensor_error.list.ui.SensorErrorListTable;
import service.sensor_error.list.ui.swing.SwingSensorErrorListDialog;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwingSensorErrorListManager implements SensorErrorListManager {

    private final RepositoryFactory repositoryFactory;
    private final SensorErrorListContext context;

    private SwingSensorErrorListDialog dialog;

    public SwingSensorErrorListManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull SensorErrorListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
    }

    public void registerDialog(@Nonnull SwingSensorErrorListDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void changingOfMeasurementName() {
        SensorErrorRepository sensorErrorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);

        SensorErrorListMeasurementPanel measurementPanel = context.getElement(SensorErrorListMeasurementPanel.class);
        SensorErrorListTable table = context.getElement(SensorErrorListTable.class);
        String measurementName = measurementPanel.getMeasurementName();
        if (measurementName.equalsIgnoreCase("Всі")) {
            table.setSensorErrorsList(new ArrayList<>(sensorErrorRepository.getAll()));
        } else {
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
            List<String> measurementValues = Arrays.asList(measurementRepository.getValues(measurementName));
            table.setSensorErrorsList(sensorErrorRepository.getAll().stream()
                    .filter(se -> measurementValues.contains(se.getMeasurementValue()))
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public void clickClose() {
        if (Objects.nonNull(dialog)) dialog.shutdown();
    }

    @Override
    public void clickChange() {

    }

    @Override
    public void clickAdd() {

    }
}
