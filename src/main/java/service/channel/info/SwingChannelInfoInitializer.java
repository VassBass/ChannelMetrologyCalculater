package service.channel.info;

import model.dto.Channel;
import model.dto.Sensor;
import repository.RepositoryFactory;
import repository.repos.area.AreaRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.department.DepartmentRepository;
import repository.repos.installation.InstallationRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.process.ProcessRepository;
import repository.repos.sensor.SensorRepository;
import service.channel.info.ui.swing.*;
import service.root.ServiceInitializer;
import util.DateHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SwingChannelInfoInitializer implements ServiceInitializer {

    private final SwingChannelInfoDialog dialog;
    private final ChannelInfoManager manager;
    private final RepositoryFactory repositoryFactory;
    private final Channel channel;

    public SwingChannelInfoInitializer(SwingChannelInfoDialog dialog,
                                       ChannelInfoManager manager,
                                       RepositoryFactory repositoryFactory,
                                       Channel channel) {
        this.dialog = dialog;
        this.manager = manager;
        this.repositoryFactory = repositoryFactory;
        this.channel = channel;
    }

    @Override
    public void init() {
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        DepartmentRepository departmentRepository = repositoryFactory.getImplementation(DepartmentRepository.class);
        AreaRepository areaRepository = repositoryFactory.getImplementation(AreaRepository.class);
        ProcessRepository processRepository = repositoryFactory.getImplementation(ProcessRepository.class);
        InstallationRepository installationRepository = repositoryFactory.getImplementation(InstallationRepository.class);
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

        SwingChannelInfoCodePanel codePanel = initCodePanel(channel, channelRepository);
        SwingChannelInfoNamePanel namePanel = initNamePanel(channel);
        SwingChannelInfoMeasurementPanel measurementPanel = initMeasurementPanel(channel, measurementRepository);
        SwingChannelInfoTechnologyNumberPanel technologyNumberPanel = initTechnologyNumberPanel(channel);
        SwingChannelInfoDatePanel datePanel= initDatePanel(channel);
        SwingChannelInfoProtocolNumberPanel protocolNumberPanel = initProtocolNumberPanel(channel);
        SwingChannelInfoFrequencyPanel frequencyPanel = initFrequencyPanel(channel);
        SwingChannelInfoNextDatePanel nextDatePanel = initNextDatePanel(datePanel, frequencyPanel);
        SwingChannelInfoPathPanel pathPanel = initPathPanel(channel,
                departmentRepository, areaRepository, processRepository, installationRepository);
        SwingChannelInfoSensorPanel sensorPanel = initSensorPanel(channel, sensorRepository, measurementRepository, measurementPanel);
        SwingChannelInfoRangePanel rangePanel = initRangePanel(channel, measurementPanel);
        SwingChannelInfoAllowableErrorPanel allowableErrorPanel = initAllowableErrorPanel(channel, measurementPanel);
        SwingChannelInfoButtonsPanel buttonsPanel = new SwingChannelInfoButtonsPanel(manager);

        dialog.init(codePanel, namePanel, measurementPanel, technologyNumberPanel, datePanel, protocolNumberPanel,
                frequencyPanel, nextDatePanel, pathPanel, sensorPanel, rangePanel, allowableErrorPanel, buttonsPanel);
        manager.init(codePanel, namePanel, measurementPanel, technologyNumberPanel, datePanel, protocolNumberPanel,
                frequencyPanel, nextDatePanel, pathPanel, sensorPanel, rangePanel, allowableErrorPanel);
    }

    private SwingChannelInfoCodePanel initCodePanel(Channel channel, ChannelRepository channelRepository) {
        SwingChannelInfoCodePanel codePanel = new SwingChannelInfoCodePanel(manager, channelRepository);

        if (channel != null) codePanel.setCode(channel.getCode());

        return codePanel;
    }

    private SwingChannelInfoNamePanel initNamePanel(Channel channel) {
        SwingChannelInfoNamePanel namePanel = new SwingChannelInfoNamePanel();

        if (channel != null) namePanel.setName(channel.getName());

        return namePanel;
    }

    private SwingChannelInfoMeasurementPanel initMeasurementPanel(Channel channel, MeasurementRepository measurementRepository) {
        SwingChannelInfoMeasurementPanel measurementPanel = new SwingChannelInfoMeasurementPanel(manager);

        measurementPanel.setMeasurementNames(Arrays.asList(measurementRepository.getAllNames()));
        measurementPanel.setMeasurementValues(Arrays.asList(measurementRepository.getValues(measurementPanel.getName())));

        if (channel != null) measurementPanel.setMeasurementName(channel.getMeasurementName());
        if (channel != null) measurementPanel.setMeasurementValue(channel.getMeasurementValue());

        return measurementPanel;
    }

    private SwingChannelInfoTechnologyNumberPanel initTechnologyNumberPanel(Channel channel){
        SwingChannelInfoTechnologyNumberPanel technologyNumberPanel = new SwingChannelInfoTechnologyNumberPanel();

        if (channel != null) technologyNumberPanel.setTechnologyNumber(channel.getTechnologyNumber());

        return technologyNumberPanel;
    }

    private SwingChannelInfoDatePanel initDatePanel(Channel channel) {
        SwingChannelInfoDatePanel datePanel = new SwingChannelInfoDatePanel(manager);

        if (channel == null) datePanel.setDate(DateHelper.dateToString(Calendar.getInstance()));
        else datePanel.setDate(channel.getDate());

        return datePanel;
    }

    private SwingChannelInfoProtocolNumberPanel initProtocolNumberPanel(Channel channel) {
        SwingChannelInfoProtocolNumberPanel protocolNumberPanel = new SwingChannelInfoProtocolNumberPanel();

        if (channel != null) protocolNumberPanel.setProtocolNumber(channel.getNumberOfProtocol());

        return protocolNumberPanel;
    }

    private SwingChannelInfoFrequencyPanel initFrequencyPanel(Channel channel) {
        SwingChannelInfoFrequencyPanel frequencyPanel = new SwingChannelInfoFrequencyPanel(manager);

        if (channel == null) frequencyPanel.setFrequency("2");
        else frequencyPanel.setFrequency(String.valueOf(channel.getFrequency()));

        return frequencyPanel;
    }

    private SwingChannelInfoNextDatePanel initNextDatePanel(SwingChannelInfoDatePanel datePanel,
                                                            SwingChannelInfoFrequencyPanel frequencyPanel) {
        SwingChannelInfoNextDatePanel nextDatePanel = new SwingChannelInfoNextDatePanel();

        String date = datePanel.getDate();
        String frequency = frequencyPanel.isFrequencyValid() ? frequencyPanel.getFrequency() : EMPTY;
        if (!date.isEmpty() || !frequency.isEmpty()) nextDatePanel.setNextDate(DateHelper.getNextDate(date, Double.parseDouble(frequency)));

        return nextDatePanel;
    }

    private SwingChannelInfoPathPanel initPathPanel(Channel channel,
                                                DepartmentRepository departmentRepository,
                                                AreaRepository areaRepository,
                                                ProcessRepository processRepository,
                                                InstallationRepository installationRepository) {
        SwingChannelInfoPathPanel pathPanel = new SwingChannelInfoPathPanel();
        pathPanel.setDepartments(new ArrayList<>(departmentRepository.getAll()));
        pathPanel.setAreas(new ArrayList<>(areaRepository.getAll()));
        pathPanel.setProcesses(new ArrayList<>(processRepository.getAll()));
        pathPanel.setInstallations(new ArrayList<>(installationRepository.getAll()));
        if (channel != null) {
            pathPanel.setDepartment(channel.getDepartment());
            pathPanel.setArea(channel.getArea());
            pathPanel.setProcess(channel.getProcess());
            pathPanel.setInstallation(channel.getInstallation());
        }

        return pathPanel;
    }

    private SwingChannelInfoSensorPanel initSensorPanel(Channel channel,
                                                        SensorRepository sensorRepository, MeasurementRepository measurementRepository,
                                                        SwingChannelInfoMeasurementPanel measurementPanel) {
        String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
        Sensor sensor = channel == null ? null : sensorRepository.get(channel.getCode());

        SwingChannelInfoSensorPanel sensorPanel = new SwingChannelInfoSensorPanel(manager);
        sensorPanel.setSensorsTypes(new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(selectedMeasurementName)));
        sensorPanel.setMeasurementValues(Arrays.asList(measurementRepository.getValues(selectedMeasurementName)));

        if (sensor != null) sensorPanel.setSensorType(sensor.getType());
        if (sensor != null) sensorPanel.setSerialNumber(sensor.getSerialNumber());
        if (sensor != null) sensorPanel.setMeasurementValue(sensor.getMeasurementValue());
        if (sensor == null) sensorPanel.setRange("0", "100");
        else sensorPanel.setRange(String.valueOf(sensor.getRangeMin()), String.valueOf(sensor.getRangeMax()));

        return sensorPanel;
    }

    private SwingChannelInfoRangePanel initRangePanel(Channel channel, SwingChannelInfoMeasurementPanel measurementPanel) {
        SwingChannelInfoRangePanel rangePanel = new SwingChannelInfoRangePanel(manager);

        rangePanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());

        if (channel != null) rangePanel.setRangeMin(String.valueOf(channel.getRangeMin()));
        if (channel != null) rangePanel.setRangeMax(String.valueOf(channel.getRangeMax()));

        return rangePanel;
    }

    private SwingChannelInfoAllowableErrorPanel initAllowableErrorPanel(Channel channel, SwingChannelInfoMeasurementPanel measurementPanel) {
        SwingChannelInfoAllowableErrorPanel allowableErrorPanel = new SwingChannelInfoAllowableErrorPanel(manager);

        allowableErrorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());

        if (channel != null) {
            allowableErrorPanel.setAllowableErrorPercent(String.valueOf(channel.getAllowableErrorPercent()));
            allowableErrorPanel.setAllowableErrorValue(String.valueOf(channel.getAllowableError()));
        }

        return allowableErrorPanel;
    }
}
