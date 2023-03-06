package service.channel.info;

import model.dto.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(SwingChannelInfoInitializer.class);

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
        ChannelInfoConfigHolder configHolder = new PropertiesChannelInfoConfigHolder();
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        MeasurementRepository measurementRepository = repositoryFactory.getImplementation(MeasurementRepository.class);
        DepartmentRepository departmentRepository = repositoryFactory.getImplementation(DepartmentRepository.class);
        AreaRepository areaRepository = repositoryFactory.getImplementation(AreaRepository.class);
        ProcessRepository processRepository = repositoryFactory.getImplementation(ProcessRepository.class);
        InstallationRepository installationRepository = repositoryFactory.getImplementation(InstallationRepository.class);
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);

        SwingChannelInfoCodePanel codePanel = new SwingChannelInfoCodePanel(manager, channelRepository);
        SwingChannelInfoNamePanel namePanel = new SwingChannelInfoNamePanel();
        SwingChannelInfoMeasurementPanel measurementPanel = new SwingChannelInfoMeasurementPanel(manager);
        SwingChannelInfoTechnologyNumberPanel technologyNumberPanel = new SwingChannelInfoTechnologyNumberPanel();
        SwingChannelInfoProtocolNumberPanel protocolNumberPanel = new SwingChannelInfoProtocolNumberPanel();

        SwingChannelInfoDatePanel datePanel= new SwingChannelInfoDatePanel(manager);
        datePanel.setDate(DateHelper.dateToString(Calendar.getInstance()));

        SwingChannelInfoFrequencyPanel frequencyPanel = new SwingChannelInfoFrequencyPanel(manager);
        frequencyPanel.setFrequency("2");

        SwingChannelInfoNextDatePanel nextDatePanel = new SwingChannelInfoNextDatePanel();
        String date = datePanel.getDate();
        String frequency = frequencyPanel.isFrequencyValid() ? frequencyPanel.getFrequency() : EMPTY;
        if (!date.isEmpty() || !frequency.isEmpty()) nextDatePanel.setNextDate(DateHelper.getNextDate(date, Double.parseDouble(frequency)));

        SwingChannelInfoPathPanel pathPanel = new SwingChannelInfoPathPanel();
        pathPanel.setDepartments(new ArrayList<>(departmentRepository.getAll()));
        pathPanel.setAreas(new ArrayList<>(areaRepository.getAll()));
        pathPanel.setProcesses(new ArrayList<>(processRepository.getAll()));
        pathPanel.setInstallations(new ArrayList<>(installationRepository.getAll()));

        SwingChannelInfoSensorPanel sensorPanel = new SwingChannelInfoSensorPanel(manager);
        String selectedMeasurementName = measurementPanel.getSelectedMeasurementName();
        sensorPanel.setSensorsTypes(new ArrayList<>(sensorRepository.getAllSensorsTypesByMeasurementName(selectedMeasurementName)));
        sensorPanel.setMeasurementValues(Arrays.asList(measurementRepository.getValues(selectedMeasurementName)));
        sensorPanel.setRange("0", "100");

        SwingChannelInfoRangePanel rangePanel = new SwingChannelInfoRangePanel(manager);
        rangePanel.setRangeMin("0");
        rangePanel.setRangeMax("100");
        rangePanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());

        SwingChannelInfoAllowableErrorPanel allowableErrorPanel = new SwingChannelInfoAllowableErrorPanel(manager);
        allowableErrorPanel.setMeasurementValue(measurementPanel.getSelectedMeasurementValue());
        allowableErrorPanel.setAllowableErrorPercent("1.5");
        allowableErrorPanel.setAllowableErrorValue("1.5");

        SwingChannelInfoButtonsPanel buttonsPanel = new SwingChannelInfoButtonsPanel(manager);

        dialog.init(configHolder,
                codePanel, namePanel, measurementPanel, technologyNumberPanel, datePanel, protocolNumberPanel,
                frequencyPanel, nextDatePanel, pathPanel, sensorPanel, rangePanel, allowableErrorPanel, buttonsPanel);
        manager.init(codePanel, namePanel, measurementPanel, technologyNumberPanel, datePanel, protocolNumberPanel,
                frequencyPanel, nextDatePanel, pathPanel, sensorPanel, rangePanel, allowableErrorPanel);
        manager.setChannelInfo(channel);

        logger.info(("Initialization completed successfully"));
    }
}
