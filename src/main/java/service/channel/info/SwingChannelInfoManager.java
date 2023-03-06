package service.channel.info;

import model.dto.Channel;
import model.dto.Sensor;
import model.ui.DialogWrapper;
import model.ui.LoadingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.sensor.SensorRepository;
import service.application.ApplicationScreen;
import service.channel.info.ui.*;
import service.channel.info.ui.swing.SwingChannelInfoDialog;
import service.channel.list.ChannelListManager;
import util.DateHelper;
import util.ScreenPoint;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoManager implements ChannelInfoManager {
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelInfoManager.class);

    private static final String SEARCH_TEXT = "Пошук";

    private static final String CHANNEL_NOT_FOUND_MESSAGE = "Канал з данним кодом не знайдений.";
    private static final String CHANNEL_FOUND_MESSAGE = "Канал знайдено. Відкрити інформацію про канал у данному вікні?";

    private final SwingChannelInfoDialog owner;
    private final ChannelListManager channelListManager;
    private final RepositoryFactory repositoryFactory;
    private Channel oldChannel;

    private ChannelInfoCodePanel codePanel;
    private ChannelInfoNamePanel namePanel;
    private ChannelInfoMeasurementPanel measurementPanel;
    private ChannelInfoTechnologyNumberPanel technologyNumberPanel;
    private ChannelInfoDatePanel datePanel;
    private ChannelInfoProtocolNumberPanel protocolNumberPanel;
    private ChannelInfoFrequencyPanel frequencyPanel;
    private ChannelInfoNextDatePanel nextDatePanel;
    private ChannelInfoPathPanel pathPanel;
    private ChannelInfoSensorPanel sensorPanel;
    private ChannelInfoRangePanel rangePanel;
    private ChannelInfoAllowableErrorPanel allowableErrorPanel;

    public SwingChannelInfoManager(SwingChannelInfoDialog owner,
                                   ChannelListManager channelListManager,
                                   RepositoryFactory repositoryFactory) {
        this.owner = owner;
        this.channelListManager = channelListManager;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void searchChannelByCode() {
        if (codePanel != null) {
            ChannelRepository repository = repositoryFactory.getImplementation(ChannelRepository.class);
            Channel channel = repository.get(codePanel.getCode());
            if (channel == null) {
                JOptionPane.showMessageDialog(owner, CHANNEL_NOT_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.INFORMATION_MESSAGE);
            } else {
                int result = JOptionPane.showConfirmDialog(owner, CHANNEL_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) setChannelInfo(channel);
            }
        }
    }

    @Override
    public void setChannelInfo(Channel channel) {
        if (channel != null) {
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            if (codePanel != null) codePanel.setCode(channel.getCode());
            if (namePanel != null) namePanel.setChannelName(channel.getName());
            if (technologyNumberPanel != null) technologyNumberPanel.setTechnologyNumber(channel.getTechnologyNumber());
            if (datePanel != null) datePanel.setDate(channel.getDate());
            if (protocolNumberPanel != null) protocolNumberPanel.setProtocolNumber(channel.getNumberOfProtocol());
            if (frequencyPanel != null) frequencyPanel.setFrequency(String.valueOf(channel.getFrequency()));
            /* set next date */ changeDateOrFrequency();

            if (pathPanel != null) {
                pathPanel.setDepartment(channel.getDepartment());
                pathPanel.setArea(channel.getArea());
                pathPanel.setProcess(channel.getProcess());
                pathPanel.setInstallation(channel.getInstallation());
            }

            if (measurementPanel != null) {
                measurementPanel.setMeasurementNames(Arrays.asList(measurementRepository.getAllNames()));
                measurementPanel.setMeasurementValues(Arrays.asList(measurementRepository.getValues(measurementPanel.getSelectedMeasurementName())));
                measurementPanel.setMeasurementName(channel.getMeasurementName());
                measurementPanel.setMeasurementValue(channel.getMeasurementValue());
            }

            if (sensorPanel != null) {
                Sensor sensor = sensorRepository.get(channel.getCode());
                if (sensor != null) {
                    sensorPanel.setSensorType(sensor.getType());
                    sensorPanel.setSerialNumber(sensor.getSerialNumber());
                    sensorPanel.setMeasurementValue(sensor.getMeasurementValue());
                    sensorPanel.setRange(String.valueOf(sensor.getRangeMin()), String.valueOf(sensor.getRangeMax()));
                }
            }

            if (rangePanel != null && measurementPanel != null) {
                rangePanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
                rangePanel.setRangeMin(String.valueOf(channel.getRangeMin()));
                rangePanel.setRangeMax(String.valueOf(channel.getRangeMax()));
            }

            if (allowableErrorPanel != null && measurementPanel != null) {
                allowableErrorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
                allowableErrorPanel.setAllowableErrorPercent(String.valueOf(channel.getAllowableErrorPercent()));
                allowableErrorPanel.setAllowableErrorValue(String.valueOf(channel.getAllowableError()));
            }

            oldChannel = channel;
        }
    }

    @Override
    public void changeMeasurementName() {
        if (measurementPanel != null) {
            MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
            List<String> measurementValues = Arrays.asList(measurementRepository.getValues(selectedMeasurementName));
            List<String> sensorsTypes = new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(selectedMeasurementName));

            measurementPanel.setMeasurementValues(measurementValues);
            if (sensorPanel != null) {
                sensorPanel.setSensorsTypes(sensorsTypes);
                sensorPanel.setMeasurementValues(measurementValues);
            }

            String selectedMeasurementValue = measurementPanel.getSelectedMeasurementValue();
            if (rangePanel != null) rangePanel.setMeasurementValue(selectedMeasurementValue);
            if (allowableErrorPanel != null) allowableErrorPanel.setMeasurementValue(selectedMeasurementValue);
        }
    }

    @Override
    public void changeMeasurementValue() {
        if (measurementPanel != null) {
            String selectedMeasurementValue =measurementPanel.getSelectedMeasurementValue();

            if (rangePanel != null) rangePanel.setMeasurementValue(selectedMeasurementValue);
            if (allowableErrorPanel != null) allowableErrorPanel.setMeasurementValue(selectedMeasurementValue);

            if (sensorPanel != null && sensorPanel.isEqualsRangesCheckboxAreSelected()) {
                sensorPanel.setMeasurementValue(selectedMeasurementValue);
            }
        }
    }

    @Override
    public void changeDateOrFrequency() {
        if (datePanel != null && frequencyPanel != null && nextDatePanel != null) {
            String date = datePanel.getDate();
            String frequency = frequencyPanel.isFrequencyValid() ? frequencyPanel.getFrequency() : EMPTY;
            if (!date.isEmpty() || !frequency.isEmpty())
                nextDatePanel.setNextDate(DateHelper.getNextDate(date, Double.parseDouble(frequency)));
        }
    }

    @Override
    public void setChannelAndSensorRangesEqual() {
        if (rangePanel != null && sensorPanel != null) {
            sensorPanel.setRangeMin(rangePanel.getRangeMin());
            sensorPanel.setRangeMax(rangePanel.getRangeMax());
            sensorPanel.setMeasurementValue(rangePanel.getMeasurementValue());
            sensorPanel.setRangePanelEnabled(false);
        }
    }

    @Override
    public void changedChannelRange() {
        if (rangePanel != null) {

            if (allowableErrorPanel != null &&
                    rangePanel.isRangeValid() && allowableErrorPanel.isAllowableErrorPercentValid()) {
                double range = Double.parseDouble(rangePanel.getRangeMax()) - Double.parseDouble(rangePanel.getRangeMin());
                double percent = Double.parseDouble(allowableErrorPanel.getAllowableErrorPercent());
                double value = (range / 100) * percent;
                allowableErrorPanel.setAllowableErrorValue(String.valueOf(value));
            }

            if (sensorPanel != null && sensorPanel.isEqualsRangesCheckboxAreSelected()) {
                sensorPanel.setRangeMin(rangePanel.getRangeMin());
                sensorPanel.setRangeMax(rangePanel.getRangeMax());
            }
        }
    }

    @Override
    public void changedAllowableErrorPercent() {
        if (allowableErrorPanel != null && allowableErrorPanel.isAllowableErrorPercentValid()) {
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
        if (allowableErrorPanel != null && allowableErrorPanel.isAllowableErrorValueValid()) {
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
        if (channel != null) {
            Worker worker = new Worker("Канал був успішно збережений") {
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
            };
            worker.execute();
        }
    }

    @Override
    public void saveAndCalculateChannel() {

    }

    @Override
    public void resetChannelInfo() {

    }

    @Override
    public void deleteChannel() {
        if (codePanel != null) {
            String code = codePanel.getCode();
            if (oldChannel != null && oldChannel.getCode().equals(code)) {
                String message = String.format("Ви впевнені що хочете видалити канал: \"%s\"", oldChannel.getName());
                String title = String.format("Видалити: \"%s\"", code);
                int result = JOptionPane.showConfirmDialog(owner, message, title, JOptionPane.YES_NO_OPTION);
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
    public void init(ChannelInfoCodePanel codePanel,
                     ChannelInfoNamePanel namePanel,
                     ChannelInfoMeasurementPanel measurementPanel,
                     ChannelInfoTechnologyNumberPanel technologyNumberPanel,
                     ChannelInfoDatePanel datePanel,
                     ChannelInfoProtocolNumberPanel protocolNumberPanel,
                     ChannelInfoFrequencyPanel frequencyPanel,
                     ChannelInfoNextDatePanel nextDatePanel,
                     ChannelInfoPathPanel pathPanel,
                     ChannelInfoSensorPanel sensorPanel,
                     ChannelInfoRangePanel rangePanel,
                     ChannelInfoAllowableErrorPanel allowableErrorPanel) {
        this.codePanel = codePanel;
        this.namePanel = namePanel;
        this.measurementPanel = measurementPanel;
        this.technologyNumberPanel = technologyNumberPanel;
        this.datePanel = datePanel;
        this.protocolNumberPanel = protocolNumberPanel;
        this.frequencyPanel = frequencyPanel;
        this.nextDatePanel = nextDatePanel;
        this.pathPanel = pathPanel;
        this.sensorPanel = sensorPanel;
        this.rangePanel = rangePanel;
        this.allowableErrorPanel = allowableErrorPanel;
    }

    private boolean isChannelValid() {
        return (codePanel != null && codePanel.isCodeValid(oldChannel.getCode())) &
                (namePanel != null && namePanel.isNameValid()) &
                (measurementPanel != null) &
                (technologyNumberPanel != null && technologyNumberPanel.isTechnologyNumberValid()) &
                (datePanel != null && datePanel.isDateValid()) &
                (frequencyPanel != null && frequencyPanel.isFrequencyValid()) &
                (sensorPanel != null && sensorPanel.isRangeValid()) &
                (rangePanel != null && rangePanel.isRangeValid()) &
                (allowableErrorPanel != null &&
                allowableErrorPanel.isAllowableErrorPercentValid() & allowableErrorPanel.isAllowableErrorValueValid());
    }

    private Channel createChannelFromPanel(){
        if (isChannelValid()) {
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
            SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

            Sensor sensor = new Sensor();
            String type = sensorPanel.getSelectedSensorType();
            String errorFormula = sensorRepository.getErrorFormula(type);

            sensor.setChannelCode(codePanel.getCode());
            sensor.setType(type);
            sensor.setRangeMin(Double.parseDouble(sensorPanel.getRangeMin()));
            sensor.setRangeMax(Double.parseDouble(sensorPanel.getRangeMax()));
            sensor.setSerialNumber(sensorPanel.getSerialNumber());
            sensor.setMeasurementName(measurementPanel.getSelectedMeasurementName());
            sensor.setMeasurementValue(sensorPanel.getSelectedMeasurementValue());
            sensor.setErrorFormula(errorFormula);

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
            loadingDialog = new DialogWrapper(owner, dialog, ScreenPoint.center(owner, dialog));
            loadingDialog.showing();
        }

        @Override
        protected void done() {
            loadingDialog.shutdown();
            try {
                if (get()) {
                    ApplicationScreen applicationScreen = ApplicationScreen.getInstance();
                    owner.shutdown();
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
            JOptionPane.showMessageDialog(owner, message, "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
