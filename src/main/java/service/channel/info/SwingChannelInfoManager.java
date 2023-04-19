package service.channel.info;

import application.ApplicationScreen;
import model.dto.*;
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
import util.ScreenPoint;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static util.StringHelper.FOR_LAST_ZERO;

public class SwingChannelInfoManager implements ChannelInfoManager {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelInfoManager.class);

    private static final int SENSOR_PANEL_OPTION_SET_RANGE = 0;
    private static final int SENSOR_PANEL_OPTION_SET_ERROR_FORMULAS = 1;

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
        ChannelRepository repository = repositoryFactory.getImplementation(ChannelRepository.class);

        Channel channel = repository.get(codePanel.getCode());
        if (channel == null) {
            JOptionPane.showMessageDialog(channelInfoDialog, CHANNEL_NOT_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.INFORMATION_MESSAGE);
        } else {
            int result = JOptionPane.showConfirmDialog(channelInfoDialog, CHANNEL_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.OK_CANCEL_OPTION);
            if (result == 0) setChannelInfo(channel);
        }
    }

    @Override
    public void setChannelInfo(Channel channel) {
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        if (Objects.nonNull(channel)) {
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            ChannelInfoCodePanel codePanel = context.getElement(ChannelInfoCodePanel.class);
            codePanel.setCode(channel.getCode());

            ChannelInfoNamePanel namePanel = context.getElement(ChannelInfoNamePanel.class);
            namePanel.setChannelName(channel.getName());

            ChannelInfoTechnologyNumberPanel technologyNumberPanel = context.getElement(ChannelInfoTechnologyNumberPanel.class);
            technologyNumberPanel.setTechnologyNumber(channel.getTechnologyNumber());

            ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
            datePanel.setDate(channel.getDate());

            ChannelInfoProtocolNumberPanel protocolNumberPanel = context.getElement(ChannelInfoProtocolNumberPanel.class);
            protocolNumberPanel.setProtocolNumber(channel.getNumberOfProtocol());

            ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
            frequencyPanel.setFrequency(String.valueOf(channel.getFrequency()));
            /* set next date */ changeDateOrFrequency();

            ChannelInfoPathPanel pathPanel = context.getElement(ChannelInfoPathPanel.class);
            pathPanel.setDepartment(channel.getDepartment());
            pathPanel.setArea(channel.getArea());
            pathPanel.setProcess(channel.getProcess());
            pathPanel.setInstallation(channel.getInstallation());

            ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
            measurementPanel.setMeasurementName(channel.getMeasurementName());
            String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
            measurementPanel.setMeasurementValues(Arrays.asList(measurementRepository.getValues(selectedMeasurementName)));
            measurementPanel.setMeasurementValue(channel.getMeasurementValue());

            ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
            if (Objects.nonNull(sensorPanel)) {
                sensorPanel.setSensorsTypes(new ArrayList<>(
                        sensorRepository.getAllSensorsTypesByMeasurementName(measurementPanel.getSelectedMeasurementName())));
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

            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            rangePanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
            rangePanel.setRangeMin(String.valueOf(channel.getRangeMin()));
            rangePanel.setRangeMax(String.valueOf(channel.getRangeMax()));

            ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
            allowableErrorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
            allowableErrorPanel.setAllowableErrorPercent(String.valueOf(channel.getAllowableErrorPercent()));
            allowableErrorPanel.setAllowableErrorValue(String.valueOf(channel.getAllowableErrorValue()));

            oldChannel = channel;
            setExpectedSensorInfo(SENSOR_PANEL_OPTION_SET_ERROR_FORMULAS);
        } else {
            ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
            String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
            List<String> measurementValues = Arrays.asList(measurementRepository.getValues(selectedMeasurementName));
            measurementPanel.setMeasurementValues(measurementValues);

            ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            sensorPanel.setSensorsTypes(new ArrayList<>(
                    sensorRepository.getAllSensorsTypesByMeasurementName(measurementPanel.getSelectedMeasurementName())));
            sensorPanel.setRange("0.0", "100.0");
            sensorPanel.setMeasurementValues(measurementValues);
            sensorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());

            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            rangePanel.setRangeMin("0.0");
            rangePanel.setRangeMax("100.0");
            rangePanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());

            ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
            allowableErrorPanel.setAllowableErrorPercent("1.5");
            allowableErrorPanel.setAllowableErrorValue("1.5");
            allowableErrorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());

            ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
            datePanel.setDate(DateHelper.dateToString(Calendar.getInstance()));

            ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
            frequencyPanel.setFrequency("2.0");
            /* set next date */ changeDateOrFrequency();

            setExpectedSensorInfo();
        }
    }

    @Override
    public void changeMeasurementName() {
        ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

        String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
        List<String> measurementValues = Arrays.asList(measurementRepository.getValues(selectedMeasurementName));
        List<String> sensorsTypes = new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(selectedMeasurementName));

        measurementPanel.setMeasurementValues(measurementValues);
        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
        sensorPanel.setSensorsTypes(sensorsTypes);
        sensorPanel.setMeasurementValues(measurementValues);

        String selectedMeasurementValue = measurementPanel.getSelectedMeasurementValue();

        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        rangePanel.setMeasurementValue(selectedMeasurementValue);

        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
        allowableErrorPanel.setMeasurementValue(selectedMeasurementValue);

        setExpectedSensorInfo();
    }

    @Override
    public void changeMeasurementValue() {
        ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);

        String selectedMeasurementValue = measurementPanel.getSelectedMeasurementValue();

        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        rangePanel.setMeasurementValue(selectedMeasurementValue);

        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
        allowableErrorPanel.setMeasurementValue(selectedMeasurementValue);

        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
        if (sensorPanel.isEqualsRangesCheckboxAreSelected()) {
            sensorPanel.setMeasurementValue(selectedMeasurementValue);
        }

        setExpectedSensorInfo(SENSOR_PANEL_OPTION_SET_ERROR_FORMULAS);
    }

    @Override
    public void changeDateOrFrequency() {
        ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
        ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
        ChannelInfoNextDatePanel nextDatePanel = context.getElement(ChannelInfoNextDatePanel.class);

        String date = datePanel.getDate();
        String frequency = frequencyPanel.isFrequencyValid() ? frequencyPanel.getFrequency() : EMPTY;
        if (!date.isEmpty() && !frequency.isEmpty()) {
            nextDatePanel.setNextDate(DateHelper.getNextDate(date, Double.parseDouble(frequency)));
        }
    }

    @Override
    public void setChannelAndSensorRangesEqual() {
        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);

        sensorPanel.setRangeMin(rangePanel.getRangeMin());
        sensorPanel.setRangeMax(rangePanel.getRangeMax());
        sensorPanel.setMeasurementValue(rangePanel.getMeasurementValue());
        sensorPanel.setRangePanelEnabled(false);

        setExpectedSensorInfo(SENSOR_PANEL_OPTION_SET_ERROR_FORMULAS);
    }

    @Override
    public void changedChannelRange() {
        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
        if (rangePanel.isRangeValid() && allowableErrorPanel.isAllowableErrorPercentValid()) {
            double range = Double.parseDouble(rangePanel.getRangeMax()) - Double.parseDouble(rangePanel.getRangeMin());
            double percent = Double.parseDouble(allowableErrorPanel.getAllowableErrorPercent());
            double value = (range / 100) * percent;
            double roundingValue = Double.parseDouble(StringHelper.roundingDouble(value, 5));
            allowableErrorPanel.setAllowableErrorValue(StringHelper.roundingDouble(roundingValue, FOR_LAST_ZERO));
        }

        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
        if (sensorPanel.isEqualsRangesCheckboxAreSelected()) {
            sensorPanel.setRangeMin(rangePanel.getRangeMin());
            sensorPanel.setRangeMax(rangePanel.getRangeMax());
        }

        setExpectedSensorInfo(SENSOR_PANEL_OPTION_SET_ERROR_FORMULAS);
    }

    @Override
    public void changedAllowableErrorPercent() {
        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
        if (allowableErrorPanel.isAllowableErrorPercentValid()) {
            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            if (rangePanel.isRangeValid()) {
                double range = Double.parseDouble(rangePanel.getRangeMax()) - Double.parseDouble(rangePanel.getRangeMin());
                double percent = Double.parseDouble(allowableErrorPanel.getAllowableErrorPercent());
                double value = (range / 100) * percent;
                double roundingValue = Double.parseDouble(StringHelper.roundingDouble(value, 5));
                allowableErrorPanel.setAllowableErrorValue(StringHelper.roundingDouble(roundingValue, FOR_LAST_ZERO));
            }
        }
    }

    @Override
    public void changedAllowableErrorValue() {
        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);
        if (allowableErrorPanel.isAllowableErrorValueValid()) {
            ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
            if (rangePanel.isRangeValid()) {
                double range = Double.parseDouble(rangePanel.getRangeMax()) - Double.parseDouble(rangePanel.getRangeMin());
                double value = Double.parseDouble(allowableErrorPanel.getAllowableErrorValue());
                double percent = value / (range / 100);
                double roundingPercent = Double.parseDouble(StringHelper.roundingDouble(percent, 5));
                allowableErrorPanel.setAllowableErrorPercent(StringHelper.roundingDouble(roundingPercent, FOR_LAST_ZERO));
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
            }.runCalculateAfter(channel).execute();
        } else {
            channelInfoDialog.refresh();
        }
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

    @Override
    public void setExpectedSensorInfo(Integer ... options) {
        List<Integer> optionsList = Arrays.asList(options);

        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
        ChannelInfoMeasurementPanel measurementPanel = context.getElement(ChannelInfoMeasurementPanel.class);
        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);

        String measurementName = measurementPanel.getSelectedMeasurementName();
        String sensorType = sensorPanel.getSelectedSensorType();

        if (optionsList.isEmpty() || optionsList.contains(SENSOR_PANEL_OPTION_SET_RANGE)) {
            Range expectedRange = getExpectedSensorRange(measurementName, sensorType);
            if (Objects.nonNull(expectedRange)) {
                sensorPanel.setRange(
                        StringHelper.roundingDouble(expectedRange.getRangeMin(), FOR_LAST_ZERO),
                        StringHelper.roundingDouble(expectedRange.getRangeMax(), FOR_LAST_ZERO)
                );
                sensorPanel.setMeasurementValue(expectedRange.getValue());
            }
        }

        if (optionsList.isEmpty() || optionsList.contains(SENSOR_PANEL_OPTION_SET_ERROR_FORMULAS)) {
            String channelRangeMin = rangePanel.getRangeMin();
            String channelRangeMax = rangePanel.getRangeMax();
            if (StringHelper.isDouble(channelRangeMin, channelRangeMax)) {

                String currentError = sensorPanel.getErrorFormula();
                String measurementValue = measurementPanel.getSelectedMeasurementValue();
                double rangeMin = Double.parseDouble(channelRangeMin);
                double rangeMax = Double.parseDouble(channelRangeMax);

                List<String> expectedErrors = getExpectedSensorErrorFormulas(sensorType, measurementName, measurementValue, rangeMin, rangeMax, currentError);
                sensorPanel.setErrorFormulas(expectedErrors);
                if (!currentError.isEmpty() && !expectedErrors.contains(currentError)) {
                    sensorPanel.setErrorFormula(currentError);
                }
            }
        }

        channelInfoDialog.refresh();
    }

    private Range getExpectedSensorRange(String measurementName, String sensorType) {
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

        Set<Sensor> sensors = sensorRepository.getAllByMeasurementName(measurementName).stream()
                .filter(s -> StringHelper.containsEachOtherIgnoreCase(s.getType(), sensorType))
                .collect(Collectors.toSet());
        if (sensors.isEmpty()) return null;

        Map<Range, Integer> counts = new HashMap<>();
        for (Sensor sensor : sensors) {
            Range range = new Range(sensor.getRangeMin(), sensor.getRangeMax(), sensor.getMeasurementValue());
            if (counts.containsKey(range)) {
                int count = counts.get(range);
                counts.put(range, ++count);
            } else counts.put(range, 1);
        }

        return Collections.max(counts.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    private List<String> getExpectedSensorErrorFormulas(String sensorType,
                                                        String measurementName,
                                                        String measurementValue,
                                                        double rangeMin,
                                                        double rangeMax,
                                                        String currentError) {
        SensorErrorRepository errorRepository = repositoryFactory.getImplementation(SensorErrorRepository.class);
        MeasurementFactorRepository measurementFactorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);


        List<String> errors = errorRepository.getBySensorType(sensorType).stream()
                .filter(e -> {
                    String errorMeasurementValue = e.getMeasurementValue();
                    Measurement errorMeasurement = measurementRepository.getByValue(errorMeasurementValue);
                    if (Objects.nonNull(errorMeasurement) && measurementName.equals(errorMeasurement.getName())) {

                        double errRangeMin = e.getRangeMin();
                        double errRangeMax = e.getRangeMax();
                        if (!measurementValue.equals(errorMeasurementValue)) {
                            double factor = measurementFactorRepository.getBySource(errorMeasurementValue).stream()
                                    .filter(mf -> mf.getTransformTo().equals(measurementValue))
                                    .findAny()
                                    .map(MeasurementTransformFactor::getTransformFactor)
                                    .orElse(1.0);
                            errRangeMin *= factor;
                            errRangeMax *= factor;
                        }

                        return errRangeMin <= rangeMin && errRangeMax >= rangeMax;
                    } else return false;
                })
                .map(SensorError::getErrorFormula)
                .collect(Collectors.toList());

        if (!currentError.isEmpty() && !errors.contains(currentError)) {
            errors.add(currentError);
        }

        return errors;
    }

    private boolean isChannelValid() {
        ChannelInfoCodePanel codePanel = context.getElement(ChannelInfoCodePanel.class);
        ChannelInfoNamePanel namePanel = context.getElement(ChannelInfoNamePanel.class);
        ChannelInfoTechnologyNumberPanel technologyNumberPanel = context.getElement(ChannelInfoTechnologyNumberPanel.class);
        ChannelInfoDatePanel datePanel = context.getElement(ChannelInfoDatePanel.class);
        ChannelInfoFrequencyPanel frequencyPanel = context.getElement(ChannelInfoFrequencyPanel.class);
        ChannelInfoSensorPanel sensorPanel = context.getElement(ChannelInfoSensorPanel.class);
        ChannelInfoRangePanel rangePanel = context.getElement(ChannelInfoRangePanel.class);
        ChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(ChannelInfoAllowableErrorPanel.class);

        return codePanel.isCodeValid(Objects.isNull(oldChannel) ? null : oldChannel.getCode()) &
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

            channel.setNumberOfProtocol(protocolNumberPanel.getProtocolNumber());
            channel.setDepartment(pathPanel.getSelectedDepartment());
            channel.setArea(pathPanel.getSelectedArea());
            channel.setProcess(pathPanel.getSelectedProcess());
            channel.setInstallation(pathPanel.getSelectedInstallation());

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
        private boolean runCalculate = false;
        private Channel channel;

        public Worker(String successMessage) {
            super();
            this.successMessage = successMessage;

            LoadingDialog dialog = LoadingDialog.getInstance();
            loadingDialog = new DialogWrapper(channelInfoDialog, dialog, ScreenPoint.center(channelInfoDialog, dialog));
            loadingDialog.showing();
        }

        public Worker runCalculateAfter(@Nonnull Channel channel) {
            runCalculate = true;
            this.channel = channel;
            return this;
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
            if (runCalculate && Objects.nonNull(channel)) channelListManager.calculateChannel(channel);
        }

        private void errorReaction(Exception e) {
            logger.warn("Exception was thrown", e);
            String message = "Виникла помилка, будь ласка спробуйте ще раз";
            JOptionPane.showMessageDialog(channelInfoDialog, message, "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
