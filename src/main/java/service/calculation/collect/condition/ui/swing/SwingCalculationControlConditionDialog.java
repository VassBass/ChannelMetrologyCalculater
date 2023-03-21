package service.calculation.collect.condition.ui.swing;

import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationConfigHolder;
import service.calculation.CalculationValue;
import service.calculation.collect.condition.ui.*;
import util.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static model.ui.builder.CellBuilder.HORIZONTAL;
import static model.ui.builder.CellBuilder.NONE;
import static service.calculation.CalculationValue.*;

public class SwingCalculationControlConditionDialog extends JDialog implements CalculationControlConditionDialog {
    private static final String TITLE = "Розрахунок ВК: ";

    private final Frame owner;

    private CalculationControlConditionDatePanel datePanel;
    private CalculationControlConditionProtocolNumberPanel protocolNumberPanel;
    private CalculationControlConditionCalibratorPanel calibratorPanel;
    private CalculationControlConditionEnvironmentPanel environmentPanel;

    public SwingCalculationControlConditionDialog(Frame owner, String channelName) {
        super(owner, TITLE + channelName, true);
        this.owner = owner;
    }

    public void init(CalculationConfigHolder configHolder,
                     SwingCalculationControlConditionDatePanel datePanel,
                     SwingCalculationControlConditionProtocolNumberPanel protocolNumberPanel,
                     SwingCalculationControlConditionCalibratorPanel calibratorPanel,
                     SwingCalculationControlConditionEnvironmentPanel environmentPanel,
                     SwingCalculationControlConditionButtonsPanel buttonsPanel) {
        DefaultPanel panel = new DefaultPanel();

        this.datePanel = datePanel;
        this.protocolNumberPanel = protocolNumberPanel;
        this.calibratorPanel = calibratorPanel;
        this.environmentPanel = environmentPanel;

        panel.add(datePanel, new CellBuilder().fill(HORIZONTAL).x(0).y(0).width(1).height(1).build());
        panel.add(protocolNumberPanel, new CellBuilder().fill(HORIZONTAL).x(1).y(0).width(1).height(1).build());
        panel.add(calibratorPanel, new CellBuilder().fill(HORIZONTAL).x(2).y(0).width(1).height(1).build());
        panel.add(environmentPanel, new CellBuilder().fill(HORIZONTAL).x(0).y(1).width(3).height(4).build());
        panel.add(buttonsPanel, new CellBuilder().fill(HORIZONTAL).x(0).y(5).width(3).height(1).build());

        int width = configHolder.getControlConditionDialogWidth();
        int height = configHolder.getControlConditionDialogHeight();
        this.setSize(width, height);
        this.setLocation(ScreenPoint.center(owner, this));
        this.setContentPane(panel);
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
    public Map<CalculationValue, String> getControlConditionValues() {
        String date = datePanel.getDate();
        String protocolNumber = protocolNumberPanel.getProtocolNumber();
        String calibratorName = calibratorPanel.getSelectedCalibratorName();
        String temperature = environmentPanel.getTemperature();
        String humidity = environmentPanel.getHumidity();
        String pressure = environmentPanel.getPressure();

        if (date == null || protocolNumber.isEmpty() ||
                temperature == null || humidity == null || pressure == null) {
            return null;
        } else {
            Map<CalculationValue, String> values = new HashMap<>();
            values.put(CONTROL_CONDITION_DATE, date);
            values.put(CONTROL_CONDITION_PROTOCOL_NUMBER, protocolNumber);
            values.put(CONTROL_CONDITION_CALIBRATOR_NAME, calibratorName);
            values.put(CONTROL_CONDITION_TEMPERATURE, temperature);
            values.put(CONTROL_CONDITION_HUMIDITY, humidity);
            values.put(CONTROL_CONDITION_PRESSURE, pressure);
            return values;
        }
    }
}
