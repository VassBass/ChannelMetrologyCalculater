package service.sensor_error.list;

import model.dto.SensorError;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.sensor_error.info.SensorErrorInfoExecuter;
import service.sensor_error.list.ui.SensorErrorListContext;
import service.sensor_error.list.ui.SensorErrorListMeasurementPanel;
import service.sensor_error.list.ui.SensorErrorListTable;
import service.sensor_error.list.ui.swing.SwingSensorErrorListDialog;

import javax.annotation.Nonnull;
import javax.swing.*;
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
        dialog.shutdown();
    }

    @Override
    public void clickChange() {
        SensorErrorListTable table = context.getElement(SensorErrorListTable.class);
        String id = table.getSelectedId();
        if (Objects.nonNull(id)) {
            SensorErrorRepository repository = repositoryFactory.getImplementation(SensorErrorRepository.class);
            SensorError error = repository.getById(id);
            new SensorErrorInfoExecuter(dialog, repositoryFactory, this, error).execute();
        }
    }

    @Override
    public void clickAdd() {
        new SensorErrorInfoExecuter(dialog, repositoryFactory, this, null).execute();
    }

    @Override
    public void clickRemove() {
        SensorErrorListTable table = context.getElement(SensorErrorListTable.class);
        String id = table.getSelectedId();
        if (Objects.nonNull(id)) {
            SensorErrorRepository repository = repositoryFactory.getImplementation(SensorErrorRepository.class);
            String message;
            if (repository.removeById(id)) {
                message = "Видалення пройшло успішно";
                JOptionPane.showMessageDialog(dialog, message, "Успіх", JOptionPane.INFORMATION_MESSAGE);
            } else {
                message = "При видаленні виникла помилка";
                JOptionPane.showMessageDialog(dialog, message, "Успіх", JOptionPane.ERROR_MESSAGE);
            }
            dialog.refresh();
        }
    }

    @Override
    public void refreshDialog() {
        dialog.refresh();
    }
}
