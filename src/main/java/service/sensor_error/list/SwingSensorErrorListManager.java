package service.sensor_error.list;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
import model.dto.SensorError;
import repository.RepositoryFactory;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.sensor_error.info.SensorErrorInfoExecutor;
import service.sensor_error.list.ui.SensorErrorListContext;
import service.sensor_error.list.ui.SensorErrorListMeasurementPanel;
import service.sensor_error.list.ui.SensorErrorListTable;
import service.sensor_error.list.ui.swing.SwingSensorErrorListDialog;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class SwingSensorErrorListManager implements SensorErrorListManager {

    private final RepositoryFactory repositoryFactory;
    private final SensorErrorListContext context;

    private SwingSensorErrorListDialog dialog;

    private final Map<String, String> labels;

    public SwingSensorErrorListManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull SensorErrorListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
        labels = Labels.getRootLabels();
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
        if (measurementName.equalsIgnoreCase(labels.get(RootLabelName.ALL_ALT))) {
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
            new SensorErrorInfoExecutor(dialog, repositoryFactory, this, error).execute();
        }
    }

    @Override
    public void clickAdd() {
        new SensorErrorInfoExecutor(dialog, repositoryFactory, this, null).execute();
    }

    @Override
    public void clickRemove() {
        Map<String, String> messages = Messages.getRootMessages();

        SensorErrorListTable table = context.getElement(SensorErrorListTable.class);
        String id = table.getSelectedId();
        if (Objects.nonNull(id)) {
            SensorErrorRepository repository = repositoryFactory.getImplementation(SensorErrorRepository.class);
            if (repository.removeById(id)) {
                JOptionPane.showMessageDialog(
                        dialog,
                        messages.get(RootMessageName.DELETING_SUCCESS),
                        labels.get(RootLabelName.SUCCESS),
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        dialog,
                        messages.get(RootMessageName.ERROR_TRY_AGAIN),
                        labels.get(RootLabelName.ERROR),
                        JOptionPane.ERROR_MESSAGE
                );
            }
            dialog.refresh();
        }
    }

    @Override
    public void refreshDialog() {
        dialog.refresh();
    }
}
