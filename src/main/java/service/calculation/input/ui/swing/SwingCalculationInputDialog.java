package service.calculation.input.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.CalculationCollectDialog;
import service.calculation.CalculationConfigHolder;
import service.calculation.dto.Protocol;
import service.calculation.input.ui.SwingCalculationInputContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.TreeMap;

public class SwingCalculationInputDialog extends JDialog implements CalculationCollectDialog {
    private static final String TITLE = "Вхідні дані";

    private final SwingCalculationInputAlarmPanel alarmPanel;
    private final SwingCalculationInputMeasurementPanel measurementPanel;

    public SwingCalculationInputDialog(@Nonnull ApplicationScreen applicationScreen,
                                       @Nonnull CalculationConfigHolder configHolder,
                                       @Nonnull SwingCalculationInputContext context) {
        super(applicationScreen, TITLE, true);

        DefaultPanel panel = new DefaultPanel();

        alarmPanel = context.getElement(SwingCalculationInputAlarmPanel.class);
        measurementPanel = context.getElement(SwingCalculationInputMeasurementPanel.class);
        SwingCalculationInputButtonsPanel buttonsPanel = context.getElement(SwingCalculationInputButtonsPanel.class);

        panel.add(alarmPanel, new CellBuilder().y(0).build());
        panel.add(measurementPanel, new CellBuilder().y(1).build());
        panel.add(buttonsPanel, new CellBuilder().y(2).build());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                buttonsPanel.clickCloseButton();
            }
        });

        int width = configHolder.getInputDialogWidth();
        int height = configHolder.getInputDialogHeight();
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
        if (alarmPanel.isEnabled()) {
            double alarm = alarmPanel.getAlarmValue();
            if (Double.isNaN(alarm)) {
                String message = "Значення сигналізації не коректні";
                JOptionPane.showMessageDialog(this, message, "Помилковий ввод", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            protocol.setAlarm(alarm);
        }

        TreeMap<Double, Double> input = measurementPanel.getInputs();
        if (Objects.isNull(input)) {
            String message = "В полях вводу заданих величин присутні некорекні дані";
            JOptionPane.showMessageDialog(this, message, "Помилковий ввод", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        protocol.setInput(input);

        TreeMap<Double, double[]> output = measurementPanel.getMeasurementValues();
        if (Objects.isNull(output)) {
            String message = "В полях вводу отриманих даних присутні некорекні дані";
            JOptionPane.showMessageDialog(this, message, "Помилковий ввод", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        protocol.setOutput(output);

        return true;
    }
}