package service.measurement.list;

import model.dto.Measurement;
import model.dto.MeasurementTransformFactor;
import model.ui.DialogWrapper;
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
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwingMeasurementListManager implements MeasurementListManager {

    private final RepositoryFactory repositoryFactory;
    private final MeasurementListContext context;
    private SwingMeasurementListDialog dialog;

    public SwingMeasurementListManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull MeasurementListContext context) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
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

    @Override
    public void clickRemove() {
        MeasurementListValueTable valueTable = context.getElement(MeasurementListValueTable.class);

        String selectedValue = valueTable.getSelectedValue();
        if (Objects.nonNull(selectedValue)) {
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

            if (measurementRepository.isLastInMeasurement(selectedValue)) {
                MeasurementListNamePanel namePanel = context.getElement(MeasurementListNamePanel.class);

                String measurementName = namePanel.getSelectedName();
                String message = String.format("Не можна видалити останню вимірювальну величину для вимірюваннь типу \"%s\"!", measurementName);
                JOptionPane.showMessageDialog(dialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
            } else {
                String message = String.format("Ви впевнені що хочете видалити вимірювальну величину \"%s\"?" +
                        " Всі записи в базі данних, де фігурує ця величина будуть змінені на іншу для данного типу вимірюваннь", selectedValue);
                int result = JOptionPane.showConfirmDialog(dialog, message, "Видалення", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    Measurement measurement = measurementRepository.getByValue(selectedValue);
                    Measurement measurementToChange = measurementRepository.getAnyNotEquals(measurement);

                    if (ObjectHelper.nonNull(measurement, measurementToChange)) {
                        final DialogWrapper loadingDialog;
                        LoadingDialog lDialog = LoadingDialog.getInstance();
                        loadingDialog = new DialogWrapper(dialog, lDialog, ScreenPoint.center(dialog, lDialog));
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
                                String message = "Видалення пройшло успішно";
                                JOptionPane.showMessageDialog(dialog, message, "Успіх", JOptionPane.INFORMATION_MESSAGE);
                                dialog.refresh();
                            }
                        }.execute();
                    }
                }

            }
        }
    }
}
