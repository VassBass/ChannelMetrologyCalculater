package service.measurement.info;

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
import service.measurement.info.ui.MeasurementInfoContext;
import service.measurement.info.ui.MeasurementInfoFactorsPanel;
import service.measurement.info.ui.MeasurementInfoNamePanel;
import service.measurement.info.ui.MeasurementInfoValuePanel;
import service.measurement.info.ui.swing.SwingMeasurementInfoDialog;
import service.measurement.list.ui.swing.SwingMeasurementListDialog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingMeasurementInfoManager implements MeasurementInfoManager {

    private final RepositoryFactory repositoryFactory;
    private final MeasurementInfoContext context;
    private final Measurement oldMeasurement;
    private final SwingMeasurementListDialog parentDialog;
    private SwingMeasurementInfoDialog dialog;

    public SwingMeasurementInfoManager(@Nonnull RepositoryFactory repositoryFactory,
                                       @Nonnull MeasurementInfoContext context,
                                       @Nonnull SwingMeasurementListDialog parentDialog,
                                       @Nullable Measurement oldMeasurement) {
        this.repositoryFactory = repositoryFactory;
        this.context = context;
        this.oldMeasurement = oldMeasurement;
        this.parentDialog = parentDialog;
    }

    public void registerDialog(@Nonnull SwingMeasurementInfoDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void changeValue() {
        MeasurementInfoFactorsPanel factorsPanel = context.getElement(MeasurementInfoFactorsPanel.class);
        MeasurementInfoValuePanel valuePanel = context.getElement(MeasurementInfoValuePanel.class);

        String input = valuePanel.getMeasurementValue();
        factorsPanel.setFactorInput(input);
        dialog.refresh();
    }

    @Override
    public void changeName() {
        MeasurementInfoFactorsPanel factorsPanel = context.getElement(MeasurementInfoFactorsPanel.class);

        MeasurementInfoNamePanel namePanel = context.getElement(MeasurementInfoNamePanel.class);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);

        String measurementName = namePanel.getMeasurementName();
        factorsPanel.setFactorOutputList(measurementRepository.getMeasurementsByName(measurementName).stream()
                .map(m -> new MeasurementTransformFactor(0, EMPTY, m.getValue(), 1D))
                .collect(Collectors.toList()));
    }

    @Override
    public void clickCancel() {
        dialog.shutdown();
    }

    @Override
    public void clickClear() {
        if (Objects.nonNull(oldMeasurement)) {
            MeasurementInfoValuePanel valuePanel = context.getElement(MeasurementInfoValuePanel.class);
            MeasurementInfoFactorsPanel factorsPanel = context.getElement(MeasurementInfoFactorsPanel.class);
            MeasurementFactorRepository factorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);

            valuePanel.setMeasurementValue(oldMeasurement.getValue());
            factorsPanel.setFactorInput(oldMeasurement.getValue());
            factorsPanel.setFactorOutputList(new ArrayList<>(factorRepository.getBySource(oldMeasurement.getValue())));
            dialog.refresh();
        }
    }

    @Override
    public void clickSave() {
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        MeasurementFactorRepository measurementFactorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);
        MeasurementInfoValuePanel valuePanel = context.getElement(MeasurementInfoValuePanel.class);
        MeasurementInfoFactorsPanel factorsPanel = context.getElement(MeasurementInfoFactorsPanel.class);

        String value = valuePanel.getMeasurementValue();
        Map<String, Double> factors = factorsPanel.getFactorList();

        if (value.isEmpty()) {
            String message = "Назва величини не може бути пустою!";
            JOptionPane.showMessageDialog(dialog, message, "Помилковий ввод", JOptionPane.ERROR_MESSAGE);
        } else if (Objects.isNull(factors)) {
            dialog.refresh();
        } else {
            if (Objects.isNull(oldMeasurement)) {
                MeasurementInfoNamePanel namePanel = context.getElement(MeasurementInfoNamePanel.class);
                String name = namePanel.getMeasurementName();

                if (measurementRepository.exists(value)) {
                    showExistMessage();
                } else {
                    LoadingDialog loadingDialog = new LoadingDialog(dialog);
                    loadingDialog.showing();
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            Measurement measurement = new Measurement(name, value);
                            measurementRepository.add(measurement);
                            for (Map.Entry<String, Double> entry : factorsPanel.getFactorList().entrySet()) {
                                double val = entry.getValue();
                                double reversedVal = val / Math.pow(val, 2);

                                measurementFactorRepository.add(value, entry.getKey(), val);
                                measurementFactorRepository.add(entry.getKey(), value, reversedVal);
                            }

                            return null;
                        }

                        @Override
                        protected void done() {
                            showSaveSuccessMessage();
                            dialog.shutdown();
                            parentDialog.refresh();
                        }
                    }.execute();
                }
            } else {
                String oldValue = oldMeasurement.getValue();
                if (measurementRepository.exists(oldValue, value)) {
                    showExistMessage();
                } else {
                    LoadingDialog loadingDialog = new LoadingDialog(dialog);
                    loadingDialog.showing();
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
                            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
                            CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
                            SensorErrorRepository sensorErrorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);

                            Measurement measurement = new Measurement(oldMeasurement.getName(), value);

                            measurementRepository.set(oldMeasurement, measurement);
                            changeFactorsValues();
                            measurementFactorRepository.changeAllResults(oldValue, value);
                            measurementFactorRepository.changeAllSources(oldValue, value);
                            sensorRepository.changeMeasurementValue(oldValue, value);
                            channelRepository.changeMeasurementValue(oldValue, value);
                            calibratorRepository.changeMeasurementValue(oldValue, value);
                            sensorErrorRepository.changeMeasurementValue(oldValue, value);

                            return null;
                        }

                        private void changeFactorsValues() {
                            Map<String, Double> factorList = factorsPanel.getFactorList();

                            Collection<MeasurementTransformFactor> all = measurementFactorRepository.getAll();
                            for (MeasurementTransformFactor factor : all) {
                                double f;
                                if (factor.getTransformFrom().equals(oldValue)) {
                                    f = factorList.get(factor.getTransformTo());
                                } else if (factor.getTransformTo().equals(oldValue)) {
                                    double val = factorList.get(factor.getTransformFrom());
                                    f = val / (Math.pow(val, 2));
                                } else continue;

                                factor.setTransformFactor(f);
                            }

                            measurementFactorRepository.rewrite(all);
                        }

                        @Override
                        protected void done() {
                            loadingDialog.shutdown();
                            showSaveSuccessMessage();
                            dialog.shutdown();
                            parentDialog.refresh();
                        }
                    }.execute();
                }
            }
        }
    }

    private void showExistMessage() {
        String message = "Величина з такою назвою вже існує в базі";
        JOptionPane.showMessageDialog(dialog, message, "Помилковий ввод", JOptionPane.ERROR_MESSAGE);
    }

    private void showSaveSuccessMessage() {
        String message = "Дані успішно збережено";
        JOptionPane.showMessageDialog(dialog, message, "Успіх", JOptionPane.INFORMATION_MESSAGE);
    }
}
