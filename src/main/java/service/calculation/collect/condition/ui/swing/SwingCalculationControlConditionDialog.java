package service.calculation.collect.condition.ui.swing;

import application.ApplicationScreen;
import model.dto.Calibrator;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import service.calculation.CalculationConfigHolder;
import service.calculation.collect.CalculationCollectDialog;
import service.calculation.collect.condition.ui.*;
import service.calculation.dto.Protocol;
import util.DateHelper;
import util.ObjectHelper;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static model.ui.builder.CellBuilder.HORIZONTAL;

public class SwingCalculationControlConditionDialog extends JDialog implements CalculationCollectDialog {
    private static final String TITLE = "Розрахунок ВК: ";

    private final RepositoryFactory repositoryFactory;
    private final Protocol protocol;

    private final SwingCalculationControlConditionDatePanel datePanel;
    private final SwingCalculationControlConditionProtocolNumberPanel protocolNumberPanel;
    private final SwingCalculationControlConditionCalibratorPanel calibratorPanel;
    private final SwingCalculationControlConditionEnvironmentPanel environmentPanel;

    public SwingCalculationControlConditionDialog(@Nonnull ApplicationScreen applicationScreen,
                                                  @Nonnull RepositoryFactory repositoryFactory,
                                                  @Nonnull CalculationConfigHolder configHolder,
                                                  @Nonnull SwingCalculationControlConditionContext context,
                                                  @Nonnull Protocol protocol) {
        super(applicationScreen, TITLE + protocol.getChannel().getName(), true);
        this.repositoryFactory = repositoryFactory;
        this.protocol = protocol;

        DefaultPanel panel = new DefaultPanel();

        datePanel = context.getElement(SwingCalculationControlConditionDatePanel.class);
        protocolNumberPanel = context.getElement(SwingCalculationControlConditionProtocolNumberPanel.class);
        calibratorPanel = context.getElement(SwingCalculationControlConditionCalibratorPanel.class);
        environmentPanel = context.getElement(SwingCalculationControlConditionEnvironmentPanel.class);
        SwingCalculationControlConditionButtonsPanel buttonsPanel = context.getElement(SwingCalculationControlConditionButtonsPanel.class);

        CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
        String measurementName = protocol.getChannel().getMeasurementName();
        List<String> suitableCalibratorsNames = Arrays.asList(calibratorRepository.getAllNamesByMeasurementName(measurementName));
        calibratorPanel.setCalibratorsNamesList(suitableCalibratorsNames);

        if (Objects.nonNull(protocol.getDate())) datePanel.setDate(protocol.getDate());
        else datePanel.setDate(DateHelper.dateToString(Calendar.getInstance()));

        if (Objects.nonNull(protocol.getNumber())) protocolNumberPanel.setProtocolNumber(protocol.getNumber());
        if (Objects.nonNull(protocol.getCalibrator())) calibratorPanel.setSelectedCalibrator(protocol.getCalibrator().getName());

        if (Objects.nonNull(protocol.getTemperature())) environmentPanel.setTemperature(protocol.getTemperature());
        else environmentPanel.setTemperature("21.0");

        if (Objects.nonNull(protocol.getHumidity())) environmentPanel.setHumidity(protocol.getHumidity());
        else environmentPanel.setHumidity("70.0");

        if (Objects.nonNull(protocol.getPressure())) environmentPanel.setPressure(protocol.getPressure());
        else environmentPanel.setPressure("750.0");

        panel.add(datePanel, new CellBuilder().fill(HORIZONTAL).x(0).y(0).width(1).height(1).build());
        panel.add(protocolNumberPanel, new CellBuilder().fill(HORIZONTAL).x(1).y(0).width(1).height(1).build());
        panel.add(calibratorPanel, new CellBuilder().fill(HORIZONTAL).x(2).y(0).width(1).height(1).build());
        panel.add(environmentPanel, new CellBuilder().fill(HORIZONTAL).x(0).y(1).width(3).height(4).build());
        panel.add(buttonsPanel, new CellBuilder().fill(HORIZONTAL).x(0).y(5).width(3).height(1).build());

        int width = configHolder.getControlConditionDialogWidth();
        int height = configHolder.getControlConditionDialogHeight();
        this.setSize(width, height);
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
    }

    @Override
    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }

    @Override
    public void shutdown() {
        this.dispose();
    }

    @Override
    public Protocol fillProtocol() {
        String date = datePanel.getDate();
        String protocolNumber = protocolNumberPanel.getProtocolNumber();
        String calibratorName = calibratorPanel.getSelectedCalibratorName();
        String temperature = environmentPanel.getTemperature();
        String humidity = environmentPanel.getHumidity();
        String pressure = environmentPanel.getPressure();

        if (ObjectHelper.anyNull(date, temperature, humidity, pressure) ||
                protocolNumber.isEmpty() || calibratorName.isEmpty()) {
            return null;
        } else {
            CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
            Calibrator calibrator = calibratorRepository.get(calibratorName);
            if (Objects.isNull(calibrator)) return null;

            protocol.setDate(date);
            protocol.setNumber(protocolNumber);
            protocol.setCalibrator(calibrator);
            protocol.setTemperature(temperature);
            protocol.setHumidity(humidity);
            protocol.setPressure(pressure);
            return protocol;
        }
    }
}
