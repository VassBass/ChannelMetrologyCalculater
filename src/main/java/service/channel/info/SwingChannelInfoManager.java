package service.channel.info;

import application.ApplicationScreen;
import model.dto.Channel;
import model.dto.MeasurementTransformFactor;
import model.dto.Sensor;
import model.dto.SensorError;
import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.sensor.SensorRepository;
import repository.repos.sensor_error.SensorErrorRepository;
import service.channel.info.ui.*;
import service.channel.info.ui.swing.SwingChannelInfoDialog;
import service.channel.list.ChannelListManager;
import util.DateHelper;
import util.ObjectHelper;
import util.ScreenPoint;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoManager implements ChannelInfoManager {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelInfoManager.class);

    private static final String SEARCH_TEXT = "Пошук";

    private static final String CHANNEL_NOT_FOUND_MESSAGE = "Канал з данним кодом не знайдений.";
    private static final String CHANNEL_FOUND_MESSAGE = "Канал знайдено. Відкрити інформацію про канал у данному вікні?";

    private final ApplicationScreen applicationScreen;
    private final RepositoryFactory repositoryFactory;
    private final ChannelListManager channelListManager;
    private final ChannelInfoSwingContext context;
    private SwingChannelInfoDialog channelInfoDialog;
    private Channel oldChannel;

    public SwingChannelInfoManager(@Nonnull ApplicationScreen applicationScreen,
                                   @Nonnull RepositoryFactory repositoryFactory,
                                   @Nonnull ChannelListManager channelListManager,
                                   @Nonnull ChannelInfoSwingContext context) {
        this.applicationScreen = applicationScreen;
        this.repositoryFactory = repositoryFactory;
        this.channelListManager = channelListManager;
        this.context = context;
    }

    public void registerDialog(SwingChannelInfoDialog dialog) {
        this.channelInfoDialog = dialog;
    }

    @Override
    public void searchChannelByCode() {
        ChannelInfoCodePanel codePanel = context.getElement(ChannelInfoCodePanel.class);
        if (codePanel != null) {
            ChannelRepository repository = repositoryFactory.getImplementation(ChannelRepository.class);
            Channel channel = repository.get(codePanel.getCode());
            if (channel == null) {
                JOptionPane.showMessageDialog(channelInfoDialog, CHANNEL_NOT_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.INFORMATION_MESSAGE);
            } else {
                int result = JOptionPane.showConfirmDialog(channelInfoDialog, CHANNEL_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) setChannelInfo(channel);
            }
        }
    }

    @Override
    public void setChannelInfo(Channel channel) {
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        if (Objects.nonNull(channel)) {
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            ChannelInfoCodePanel codePanel = context.getElement(ChannelInfoCodePanel.class);
            if (Objects.nonNull(codePanel)) codePanel.setCode(channel.getCode());

            ChannelInfoNamePanel namePanel = context.getElement(ChannelInfoNamePanel.class);
            if (Objects.nonNull(namePanel)) namePanel.setChannelName(channel.getName());

            ChannelInfoTechnologyNumberPanel technologyNumberPanel = context.getElement(ChannelInfoTechnologyNumberPanel.class);
            if (Objects.nonNull(technologyNumberPanel)) technologyNumberPanel.setTechnologyNumber(channel.getTechnologyNumber());

            ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
            if (Objects.nonNull(datePanel)) datePanel.setDate(channel.getDate());

            ChannelInfoProtocolNumberPanel protocolNumberPanel = context.getElement(ChannelInfoProtocolNumberPanel.class);
            if (Objects.nonNull(protocolNumberPanel)) protocolNumberPanel.setProtocolNumber(channel.getNumberOfProtocol());

            ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
            if (Objects.nonNull(frequencyPanel)) frequencyPanel.setFrequency(String.valueOf(channel.getFrequency()));
            /* set next date */ changeDateOrFrequency();

            ChannelInfoPathPanel pathPanel = context.getElement(ChannelInfoPathPanel.class);
            if (Objects.nonNull(pathPanel)) {
                pathPanel.setDepartment(channel.getDepartment());
                pathPanel.setArea(channel.getArea());
                pathPanel.setProcess(channel.getProcess());
                pathPanel.setInstallation(channel.getInstallation());
            }

            ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
            if (Objects.nonNull(measurementPanel)) {
                measurementPanel.setMeasurementName(channel.getMeasurementName());
                String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
                measurementPanel.setMeasurementValues(Arrays.asList(measurementRepository.getValues(selectedMeasurementName)));
                measurementPanel.setMeasurementValue(channel.getMeasurementValue());

                ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
                if (Objects.nonNull(sensorPanel)) {
                    sensorPanel.setSensorsTypes(new ArrayList<>(
                            sensorRepository.getAllSensorsTypesByMeasurementName(
                                    measurementPanel.getSelectedMeasurementName())));
                    sensorPanel.setMeasurementValues(Arrays.asList(measurementRepository.getValues(selectedMeasurementName)));
                    Sensor sensor = sensorRepository.get(channel.getCode());
                    if (Objects.nonNull(sensor)) {
                        sensorPanel.setSensorType(sensor.getType());
                        sensorPanel.setSerialNumber(sensor.getSerialNumber());
                        sensorPanel.setMeasurementValue(sensor.getMeasurementValue());
                        sensorPanel.setRange(String.valueOf(sensor.getRangeMin()), String.valueOf(sensor.getRangeMax()));
                        sensorPanel.setErrorFormula(sensor.getErrorFormula());
                    }
                }
            }

            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            if (rangePanel != null && measurementPanel != null) {
                rangePanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
                rangePanel.setRangeMin(String.valueOf(channel.getRangeMin()));
                rangePanel.setRangeMax(String.valueOf(channel.getRangeMax()));
            }

            ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
            if (allowableErrorPanel != null && measurementPanel != null) {
                allowableErrorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
                allowableErrorPanel.setAllowableErrorPercent(String.valueOf(channel.getAllowableErrorPercent()));
                allowableErrorPanel.setAllowableErrorValue(String.valueOf(channel.getAllowableErrorValue()));
            }

            oldChannel = channel;
        } else {
            ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
            if (Objects.nonNull(measurementPanel)) {
                String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
                List<String> measurementValues = Arrays.asList(measurementRepository.getValues(selectedMeasurementName));
                measurementPanel.setMeasurementValues(measurementValues);

                ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
                SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
                if (Objects.nonNull(sensorPanel) && Objects.nonNull(sensorRepository)) {
                    sensorPanel.setSensorsTypes(new ArrayList<>(
                            sensorRepository.getAllSensorsTypesByMeasurementName(
                                    measurementPanel.getSelectedMeasurementName())));
                    sensorPanel.setRange("0.0", "100.0");
                    sensorPanel.setMeasurementValues(measurementValues);
                    sensorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
                    setErrorFormulasList();
                }

                ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
                if (Objects.nonNull(rangePanel)) {
                    rangePanel.setRangeMin("0.0");
                    rangePanel.setRangeMax("100.0");
                    rangePanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
                }

                ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
                if (Objects.nonNull(allowableErrorPanel)) {
                    allowableErrorPanel.setAllowableErrorPercent("1.5");
                    allowableErrorPanel.setAllowableErrorValue("1.5");
                    allowableErrorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
                }
            }

            ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
            if (Objects.nonNull(datePanel)) {
                datePanel.setDate(DateHelper.dateToString(Calendar.getInstance()));
            }

            ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
            if (Objects.nonNull(frequencyPanel)) {
                frequencyPanel.setFrequency("2.0");
                /* set next date */ changeDateOrFrequency();
            }
        }
    }

    @Override
    public void changeMeasurementName() {
        ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
        if (measurementPanel != null) {
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
            List<String> measurementValues = Arrays.asList(measurementRepository.getValues(selectedMeasurementName));
            List<String> sensorsTypes = new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(selectedMeasurementName));

            measurementPanel.setMeasurementValues(measurementValues);
            ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
            if (sensorPanel != null) {
                sensorPanel.setSensorsTypes(sensorsTypes);
                sensorPanel.setMeasurementValues(measurementValues);
            }

            String selectedMeasurementValue = measurementPanel.getSelectedMeasurementValue();

            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            if (rangePanel != null) rangePanel.setMeasurementValue(selectedMeasurementValue);

            ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
            if (allowableErrorPanel != null) allowableErrorPanel.setMeasurementValue(selectedMeasurementValue);

            setErrorFormulasList();
        }
    }

    @Override
    public void changeMeasurementValue() {
        ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
        if (measurementPanel != null) {
            String selectedMeasurementValue =measurementPanel.getSelectedMeasurementValue();

            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            if (rangePanel != null) rangePanel.setMeasurementValue(selectedMeasurementValue);

            ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
            if (allowableErrorPanel != null) allowableErrorPanel.setMeasurementValue(selectedMeasurementValue);

            ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
            if (sensorPanel != null && sensorPanel.isEqualsRangesCheckboxAreSelected()) {
                sensorPanel.setMeasurementValue(selectedMeasurementValue);
            }

            setErrorFormulasList();
        }
    }

    @Override
    public void changeDateOrFrequency() {
        ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
        ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
        ChannelInfoNextDatePanel nextDatePanel = context.getElement(ChannelInfoNextDatePanel.class);

        if (ObjectHelper.nonNull(datePanel, frequencyPanel, nextDatePanel)) {
            String date = datePanel.getDate();
            String frequency = frequencyPanel.isFrequencyValid() ? frequencyPanel.getFrequency() : EMPTY;
            if (!date.isEmpty() && !frequency.isEmpty())
                nextDatePanel.setNextDate(DateHelper.getNextDate(date, Double.parseDouble(frequency)));
        }
    }

    @Override
    public void setChannelAndSensorRangesEqual() {
        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
        if (rangePanel != null && sensorPanel != null) {
            sensorPanel.setRangeMin(rangePanel.getRangeMin());
            sensorPanel.setRangeMax(rangePanel.getRangeMax());
            sensorPanel.setMeasurementValue(rangePanel.getMeasurementValue());
            sensorPanel.setRangePanelEnabled(false);
        }

        setErrorFormulasList();
    }

    @Override
    public void changedChannelRange() {
        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        if (rangePanel != null) {
            ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
            if (allowableErrorPanel != null &&
                    rangePanel.isRangeValid() && allowableErrorPanel.isAllowableErrorPercentValid()) {
                double range = Double.parseDouble(rangePanel.getRangeMax()) - Double.parseDouble(rangePanel.getRangeMin());
                double percent = Double.parseDouble(allowableErrorPanel.getAllowableErrorPercent());
                double value = (range / 100) * percent;
                allowableErrorPanel.setAllowableErrorValue(String.valueOf(value));
            }

            ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
            if (sensorPanel != null && sensorPanel.isEqualsRangesCheckboxAreSelected()) {
                sensorPanel.setRangeMin(rangePanel.getRangeMin());
                sensorPanel.setRangeMax(rangePanel.getRangeMax());
            }

            setErrorFormulasList();
        }
    }

    @Override
    public void changedAllowableErrorPercent() {
        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
        if (allowableErrorPanel != null && allowableErrorPanel.isAllowableErrorPercentValid()) {
            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            if (rangePanel != null && rangePanel.isRangeValid()) {
                double range = Double.parseDouble(rangePanel.getRangeMax()) - Double.parseDouble(rangePanel.getRangeMin());
                double percent = Double.parseDouble(allowableErrorPanel.getAllowableErrorPercent());
                double value = (range / 100) * percent;
                allowableErrorPanel.setAllowableErrorValue(String.valueOf(value));
            }
        }
    }

    @Override
    public void changedAllowableErrorValue() {
        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
        if (allowableErrorPanel != null && allowableErrorPanel.isAllowableErrorValueValid()) {
            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            if (rangePanel != null && rangePanel.isRangeValid()) {
                double range = Double.parseDouble(rangePanel.getRangeMax()) - Double.parseDouble(rangePanel.getRangeMin());
                double value = Double.parseDouble(allowableErrorPanel.getAllowableErrorValue());
                double percent = value / (range / 100);
                allowableErrorPanel.setAllowableErrorPercent(String.valueOf(percent));
            }
        }
    }

    @Override
    public void saveChannel() {
        Channel channel = createChannelFromPanel();
        if (Objects.nonNull(channel)) {
            new Worker("Канал був успішно збережений") {
                @Override
                protected Boolean doInBackground() {
                    ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
                    SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

                    Sensor newSensor = createSensorFromPanel();

                    if (newSensor != null) {
                        if (oldChannel == null) {
                            return channelRepository.add(channel) && sensorRepository.add(newSensor);
                        } else {
                            Sensor oldSensor = sensorRepository.get(oldChannel.getCode());
                            if (oldSensor != null) {
                                return channelRepository.set(oldChannel, channel) && sensorRepository.set(oldSensor, newSensor);
                            }
                        }
                    }

                    return false;
                }
            }.execute();
        } else {
            channelInfoDialog.refresh();
        }
    }

    @Override
    public void saveAndCalculateChannel() {

    }

    @Override
    public void resetChannelInfo() {
        if (oldChannel!= null) {
            setChannelInfo(oldChannel);
        }
    }

    @Override
    public void deleteChannel() {
        ChannelInfoCodePanel codePanel = context.getElement(ChannelInfoCodePanel.class);
        if (codePanel != null) {
            String code = codePanel.getCode();
            if (oldChannel != null && oldChannel.getCode().equals(code)) {
                String message = String.format("Ви впевнені що хочете видалити канал: \"%s\"", oldChannel.getName());
                String title = String.format("Видалити: \"%s\"", code);
                int result = JOptionPane.showConfirmDialog(channelInfoDialog, message, title, JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    Worker worker = new Worker("Канал був успішно видалений") {
                        @Override
                        protected Boolean doInBackground() {
                            ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
                            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

                            return channelRepository.remove(oldChannel) && sensorRepository.removeByChannelCode(oldChannel.getCode());
                        }
                    };
                    worker.execute();
                }
            }
        }
    }

    @Override
    public void setErrorFormulasList() {
        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
        ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        if (sensorPanel != null && measurementPanel != null && rangePanel != null) {
            String currentError = sensorPanel.getErrorFormula();

            String channelRangeMin = rangePanel.getRangeMin();
            String channelRangeMax = rangePanel.getRangeMax();
            if (StringHelper.isDouble(channelRangeMin) && StringHelper.isDouble(channelRangeMax)) {
                SensorErrorRepository errorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);
                MeasurementFactorRepository measurementFactorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);

                String sensorType = sensorPanel.getSelectedSensorType();
                String channelMeasurementValue = measurementPanel.getSelectedMeasurementValue();

                List<String> errors = errorRepository.getBySensorType(sensorType).stream()
                        .filter(e -> {
                            String errorMeasurementValue = e.getMeasurementValue();

                            double errRangeMin = e.getRangeMin();
                            double errRangeMax = e.getRangeMax();
                            double chRangeMin = Double.parseDouble(channelRangeMin);
                            double chRangeMax = Double.parseDouble(channelRangeMax);
                            if (!channelMeasurementValue.equals(errorMeasurementValue)) {
                                double factor = measurementFactorRepository.getBySource(errorMeasurementValue).stream()
                                        .filter(mf -> mf.getTransformTo().equals(channelMeasurementValue))
                                        .findAny()
                                        .map(MeasurementTransformFactor::getTransformFactor)
                                        .orElse(1.0);
                                errRangeMin *= factor;
                                errRangeMax *= factor;
                            }

                            return errRangeMin <= chRangeMin && errRangeMax >= chRangeMax;
                        })
                        .map(SensorError::getErrorFormula)
                        .collect(Collectors.toList());

                if (!currentError.isEmpty()) {
                    errors.add(currentError);
                    sensorPanel.setErrorFormula(currentError);
                }

                sensorPanel.setErrorFormulas(errors);
            }
        }
    }

    private boolean isChannelValid() {
        ChannelInfoCodePanel codePanel = context.getElement(ChannelInfoCodePanel.class);
        ChannelInfoNamePanel namePanel = context.getElement(ChannelInfoNamePanel.class);
        ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
        ChannelInfoTechnologyNumberPanel technologyNumberPanel = context.getElement(ChannelInfoTechnologyNumberPanel.class);
        ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
        ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);

        return ObjectHelper.nonNull(codePanel, namePanel, measurementPanel, technologyNumberPanel, datePanel, frequencyPanel,
                sensorPanel, rangePanel, allowableErrorPanel) &&
                codePanel.isCodeValid(Objects.isNull(oldChannel) ? null : oldChannel.getCode()) &
                namePanel.isNameValid() &
                technologyNumberPanel.isTechnologyNumberValid() &
                datePanel.isDateValid() &
                frequencyPanel.isFrequencyValid() &
                sensorPanel.isRangeValid() &
                sensorPanel.isErrorFormulaValid() &
                rangePanel.isRangeValid() &
                allowableErrorPanel.isAllowableErrorPercentValid() &
                allowableErrorPanel.isAllowableErrorValueValid();
    }

    private Channel createChannelFromPanel(){
        if (isChannelValid()) {
            ChannelInfoCodePanel codePanel = context.getElement(ChannelInfoCodePanel.class);
            ChannelInfoNamePanel namePanel = context.getElement(ChannelInfoNamePanel.class);
            ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
            ChannelInfoTechnologyNumberPanel technologyNumberPanel = context.getElement(ChannelInfoTechnologyNumberPanel.class);
            ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
            ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
            ChannelInfoProtocolNumberPanel protocolNumberPanel = context.getElement(ChannelInfoProtocolNumberPanel.class);
            ChannelInfoPathPanel pathPanel = context.getElement(ChannelInfoPathPanel.class);

            Channel channel = new Channel();

            channel.setCode(codePanel.getCode());
            channel.setName(namePanel.getChannelName());
            channel.setMeasurementName(measurementPanel.getSelectedMeasurementName());
            channel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
            channel.setTechnologyNumber(technologyNumberPanel.getTechnologyNumber());
            channel.setDate(datePanel.getDate());
            channel.setFrequency(Double.parseDouble(frequencyPanel.getFrequency()));
            channel.setRange(Double.parseDouble(rangePanel.getRangeMin()), Double.parseDouble(rangePanel.getRangeMax()));
            channel.setAllowableError(
                    Double.parseDouble(allowableErrorPanel.getAllowableErrorPercent()),
                    Double.parseDouble(allowableErrorPanel.getAllowableErrorValue())
            );

            if (protocolNumberPanel != null) channel.setNumberOfProtocol(protocolNumberPanel.getProtocolNumber());
            if (pathPanel != null) {
                channel.setDepartment(pathPanel.getSelectedDepartment());
                channel.setArea(pathPanel.getSelectedArea());
                channel.setProcess(pathPanel.getSelectedProcess());
                channel.setInstallation(pathPanel.getSelectedInstallation());
            }

            if (oldChannel != null) {
                boolean suitability = oldChannel.isSuitability();
                channel.setSuitability(suitability);
                if (!suitability) {
                    channel.setReference(oldChannel.getReference());
                }
            }

            return channel;
        }

        return null;
    }

    private Sensor createSensorFromPanel() {
        if (isChannelValid()) {
            ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
            ChannelInfoCodePanel codePanel = context.getElement(ChannelInfoCodePanel.class);
            ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);

            Sensor sensor = new Sensor();

            sensor.setChannelCode(codePanel.getCode());
            sensor.setType(sensorPanel.getSelectedSensorType());
            sensor.setRangeMin(Double.parseDouble(sensorPanel.getRangeMin()));
            sensor.setRangeMax(Double.parseDouble(sensorPanel.getRangeMax()));
            sensor.setSerialNumber(sensorPanel.getSerialNumber());
            sensor.setMeasurementName(measurementPanel.getSelectedMeasurementName());
            sensor.setMeasurementValue(sensorPanel.getSelectedMeasurementValue());
            sensor.setErrorFormula(sensorPanel.getErrorFormula());

            return sensor;
        }

        return null;
    }

    private abstract class Worker extends SwingWorker<Boolean, Void> {
        private final DialogWrapper loadingDialog;

        private final String successMessage;

        public Worker(String successMessage) {
            super();
            this.successMessage = successMessage;

            LoadingDialog dialog = LoadingDialog.getInstance();
            loadingDialog = new DialogWrapper(channelInfoDialog, dialog, ScreenPoint.center(channelInfoDialog, dialog));
            loadingDialog.showing();
        }

        @Override
        protected void done() {
            loadingDialog.shutdown();
            try {
                if (get()) {
                    channelInfoDialog.shutdown();
                    JOptionPane.showMessageDialog(applicationScreen, successMessage, "Успіх", JOptionPane.INFORMATION_MESSAGE);
                } else errorReaction(null);
            } catch (InterruptedException | ExecutionException e) {
                errorReaction(e);
            }
            channelListManager.revaluateChannelTable();
        }

        private void errorReaction(Exception e) {
            logger.warn("Exception was thrown", e);
            String message = "Виникла помилка, будь ласка спробуйте ще раз";
            JOptionPane.showMessageDialog(channelInfoDialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
