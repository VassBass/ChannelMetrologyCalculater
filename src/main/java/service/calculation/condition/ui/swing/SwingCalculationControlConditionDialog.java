package service.calculation.condition.ui.swing;

import application.ApplicationScreen;
import model.dto.Calibrator;
import model.dto.Channel;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import repository.RepositoryFactory;
import repository.repos.calibrator.CalibratorRepository;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationCollectDialog;
import service.calculation.condition.ui.*;
import service.calculation.dto.Protocol;
import util.DateHelper;
import util.ObjectHelper;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import static model.ui.builder.CellBuilder.HORIZONTAL;

public class SwingCalculationControlConditionDialog extends JDialog implements CalculationCollectDialog {
    private static final String TITLE = "Розрахунок ВК: ";

    private final RepositoryFactory repositoryFactory;

    private final SwingCalculationControlConditionDatePanel datePanel;
    private final SwingCalculationControlConditionProtocolNumberPanel protocolNumberPanel;
    private final SwingCalculationControlConditionCalibratorPanel calibratorPanel;
    private final SwingCalculationControlConditionEnvironmentPanel environmentPanel;

    public SwingCalculationControlConditionDialog(@Nonnull ApplicationScreen applicationScreen,
                                                  @Nonnull RepositoryFactory repositoryFactory,
                                                  @Nonnull CalculationConfigHolder configHolder,
                                                  @Nonnull SwingCalculationControlConditionContext context,
                                                  @Nonnull Channel channel) {
        super(applicationScreen, TITLE + channel.getName(), true);
        this.repositoryFactory = repositoryFactory;

        DefaultPanel panel = new DefaultPanel();

        datePanel = context.getElement(SwingCalculationControlConditionDatePanel.class);
        protocolNumberPanel = context.getElement(SwingCalculationControlConditionProtocolNumberPanel.class);
        calibratorPanel = context.getElement(SwingCalculationControlConditionCalibratorPanel.class);
        environmentPanel = context.getElement(SwingCalculationControlConditionEnvironmentPanel.class);
        SwingCalculationControlConditionButtonsPanel buttonsPanel = context.getElement(SwingCalculationControlConditionButtonsPanel.class);

        CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
        String measurementName = channel.getMeasurementName();
        List<String> suitableCalibratorsNames = Arrays.asList(calibratorRepository.getAllNamesByMeasurementName(measurementName));
        calibratorPanel.setCalibratorsNamesList(suitableCalibratorsNames);

        datePanel.setDate(DateHelper.dateToString(Calendar.getInstance()));
        environmentPanel.setTemperature("21.0");
        environmentPanel.setHumidity("70.0");
        environmentPanel.setPressure("750.0");

        panel.add(datePanel, new CellBuilder().fill(HORIZONTAL).x(0).y(0).width(1).height(1).build());
        panel.add(protocolNumberPanel, new CellBuilder().fill(HORIZONTAL).x(1).y(0).width(1).height(1).build());
        panel.add(calibratorPanel, new CellBuilder().fill(HORIZONTAL).x(2).y(0).width(1).height(1).build());
        panel.add(environmentPanel, new CellBuilder().fill(HORIZONTAL).x(0).y(1).width(3).height(4).build());
        panel.add(buttonsPanel, new CellBuilder().fill(HORIZONTAL).x(0).y(5).width(3).height(1).build());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                buttonsPanel.clickNegativeButton();
            }
        });

        int width = configHolder.getControlConditionDialogWidth();
        int height = configHolder.getControlConditionDialogHeight();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
    public boolean fillProtocol(Protocol protocol) {
        String date = datePanel.getDate();
        String protocolNumber = protocolNumberPanel.getProtocolNumber();
        String calibratorName = calibratorPanel.getSelectedCalibratorName();
        String temperature = environmentPanel.getTemperature();
        String humidity = environmentPanel.getHumidity();
        String pressure = environmentPanel.getPressure();

        if (ObjectHelper.anyNull(date, temperature, humidity, pressure) ||
                protocolNumber.isEmpty() || calibratorName.isEmpty()) {
            return false;
        } else {
            CalibratorRepository calibratorRepository = repositoryFactory.getImplementation(CalibratorRepository.class);
            Calibrator calibrator = calibratorRepository.get(calibratorName);
            if (Objects.isNull(calibrator)) return false;

            protocol.setDate(date);
            protocol.setNumber(protocolNumber);
            protocol.setCalibrator(calibrator);
            protocol.setTemperature(temperature);
            protocol.setHumidity(humidity);
            protocol.setPressure(pressure);
            return true;
        }
    }
}
