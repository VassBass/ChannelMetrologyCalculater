package service.calculation.collect.condition;

import model.dto.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationManager;
import service.calculation.collect.condition.ui.swing.*;
import service.ServiceInitializer;

import java.util.Arrays;

public class SwingCalculationControlConditionInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SwingCalculationControlConditionInitializer.class);

    private final CalculationConfigHolder configHolder;
    private final SwingCalculationControlConditionDialog dialog;
    private final RepositoryFactory repositoryFactory;
    private final CalculationManager manager;
    private final Channel channel;

    public SwingCalculationControlConditionInitializer(CalculationConfigHolder configHolder,
                                                       SwingCalculationControlConditionDialog dialog,
                                                       RepositoryFactory repositoryFactory,
                                                       CalculationManager manager,
                                                       Channel channel) {
        this.configHolder = configHolder;
        this.dialog = dialog;
        this.repositoryFactory = repositoryFactory;
        this.manager = manager;
        this.channel = channel;
    }

    @Override
    public void init() {
        SwingCalculationControlConditionDatePanel datePanel = new SwingCalculationControlConditionDatePanel();
        datePanel.setDate("23.02.2023");

        SwingCalculationControlConditionProtocolNumberPanel protocolNumberPanel = new SwingCalculationControlConditionProtocolNumberPanel();

        SwingCalculationControlConditionCalibratorPanel calibratorPanel = new SwingCalculationControlConditionCalibratorPanel();
        CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
        calibratorPanel.setCalibratorsNamesList(Arrays.asList(calibratorRepository.getAllNamesByMeasurementName(channel.getMeasurementName())));

        SwingCalculationControlConditionEnvironmentPanel environmentPanel = new SwingCalculationControlConditionEnvironmentPanel();
        environmentPanel.setTemperature("21");
        environmentPanel.setHumidity("70");
        environmentPanel.setPressure("750");

        SwingCalculationControlConditionButtonsPanel buttonsPanel = new SwingCalculationControlConditionButtonsPanel(manager);

        dialog.init(configHolder, datePanel, protocolNumberPanel, calibratorPanel, environmentPanel, buttonsPanel);

        logger.info(("Initialization completed successfully"));
    }
}
