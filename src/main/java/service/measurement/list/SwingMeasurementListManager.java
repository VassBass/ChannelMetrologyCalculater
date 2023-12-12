package service.measurement.list;

import localization.Labels;
import localization.Messages;
import localization.RootLabelName;
import localization.RootMessageName;
import model.dto.Measurement;
import model.dto.MeasurementTransformFactor;
import model.ui.LoadingDialog;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.sensor.SensorRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.measurement.info.MeasurementInfoExecutor;
import service.measurement.list.ui.MeasurementListContext;
import service.measurement.list.ui.MeasurementListFactorTable;
import service.measurement.list.ui.MeasurementListNamePanel;
import service.measurement.list.ui.MeasurementListValueTable;
import service.measurement.list.ui.swing.SwingMeasurementListDialog;
import util.ObjectHelper;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwingMeasurementListManager implements MeasurementListManager {
    private static final String DELETE_LAST_ERROR = "deleteLastError";
    private static final String DELETE_QUESTION = "deleteQuestion";

    private final RepositoryFactory repositoryFactory;
    private final MeasurementListContext context;
    private SwingMeasurementListDialog dialog;

    private final Map<String, String> messages;
    private final Map<String, String> labels;

    public SwingMeasurementListManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull MeasurementListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
        messages = Messages.getMessages(SwingMeasurementListManager.class);
        labels = Labels.getRootLabels();
    }

    public void registerDialog(SwingMeasurementListDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void changeMeasurementName() {
        dialog.refresh();
    }

    @Override
    public void selectMeasurementValue() {
        MeasurementListValueTable valueTable = context.getElement(MeasurementListValueTable.class);

        String value = valueTable.getSelectedValue();
        if (Objects.nonNull(value)) {
            MeasurementListFactorTable factorTable = context.getElement(MeasurementListFactorTable.class);
            MeasurementFactorRepository factorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);

            Map<String, Double> valueList = factorRepository.getBySource(value).stream()
                    .collect(Collectors.toMap(MeasurementTransformFactor::getTransformTo, MeasurementTransformFactor::getTransformFactor));
            factorTable.setFactorList(value, valueList);
        }
    }

    @Override
    public void clickClose() {
        dialog.shutdown();
    }

    @Override
    public void clickChange() {
        MeasurementListValueTable valueTable = context.getElement(MeasurementListValueTable.class);

        String selectedValue = valueTable.getSelectedValue();
        if (Objects.nonNull(selectedValue)) {
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
            Measurement oldMeasurement = measurementRepository.getByValue(selectedValue);
            new MeasurementInfoExecutor(dialog, repositoryFactory, oldMeasurement).execute();
        }
    }

    @Override
    public void clickAdd() {
        new MeasurementInfoExecutor(dialog, repositoryFactory, null).execute();
    }

    @SuppressWarnings("all")
    @Override
    public void clickRemove() {
        MeasurementListValueTable valueTable = context.getElement(MeasurementListValueTable.class);

        String selectedValue = valueTable.getSelectedValue();
        if (Objects.nonNull(selectedValue)) {
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

            if (measurementRepository.isLastInMeasurement(selectedValue)) {
                MeasurementListNamePanel namePanel = context.getElement(MeasurementListNamePanel.class);

                String measurementName = namePanel.getSelectedName();
                String message = String.format(messages.get(DELETE_LAST_ERROR), measurementName);
                JOptionPane.showMessageDialog(dialog, message, labels.get(RootLabelName.ERROR), JOptionPane.ERROR_MESSAGE);
            } else {
                String message = String.format(messages.get(DELETE_QUESTION), selectedValue);
                int result = JOptionPane.showConfirmDialog(dialog, message, labels.get(RootLabelName.DELETING), JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    Measurement measurement = measurementRepository.getByValue(selectedValue);
                    Measurement measurementToChange = measurementRepository.getAnyNotEquals(measurement);

                    if (ObjectHelper.nonNull(measurement, measurementToChange)) {
                        LoadingDialog loadingDialog = new LoadingDialog(dialog);
                        loadingDialog.showing();
                        new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() {
                                MeasurementFactorRepository factorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);
                                SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
                                ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
                                CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
                                SensorErrorRepository sensorErrorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);

                                measurementRepository.remove(measurement);
                                factorRepository.removeBySource(selectedValue);
                                factorRepository.removeByResult(selectedValue);
                                sensorRepository.changeMeasurementValue(measurement.getValue(),measurementToChange.getValue());
                                channelRepository.changeMeasurementValue(measurement.getValue(),measurementToChange.getValue());
                                calibratorRepository.changeMeasurementValue(measurement.getValue(),measurementToChange.getValue());
                                sensorErrorRepository.changeMeasurementValue(measurement.getValue(),measurementToChange.getValue());

                                return null;
                            }

                            @Override
                            protected void done() {
                                loadingDialog.shutdown();
                                JOptionPane.showMessageDialog(
                                        dialog,
                                        Messages.getRootMessages().get(RootMessageName.DELETING_SUCCESS),
                                        labels.get(RootLabelName.SUCCESS),
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                                dialog.refresh();
                            }
                        }.execute();
                    }
                }

            }
        }
    }
}
