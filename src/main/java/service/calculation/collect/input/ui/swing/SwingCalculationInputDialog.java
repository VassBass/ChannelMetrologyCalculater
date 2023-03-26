package service.calculation.collect.input.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.calculation.collect.CalculationCollectDialog;
import service.calculation.collect.input.ui.SwingCalculationInputContext;
import service.calculation.dto.Protocol;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SwingCalculationInputDialog extends JDialog implements CalculationCollectDialog {
    private static final String TITLE = "Вхідні дані";

    public SwingCalculationInputDialog(@Nonnull ApplicationScreen applicationScreen,
                                       @Nonnull SwingCalculationInputContext context) {
        super(applicationScreen, TITLE, true);

        DefaultPanel panel = new DefaultPanel();

        SwingCalculationInputAlarmPanel alarmPanel = context.getElement(SwingCalculationInputAlarmPanel.class);
        SwingCalculationInputMeasurementPanel measurementPanel = context.getElement(SwingCalculationInputMeasurementPanel.class);
        SwingCalculationInputButtonsPanel buttonsPanel = context.getElement(SwingCalculationInputButtonsPanel.class);

        panel.add(alarmPanel, new CellBuilder().y(0).build());
        panel.add(measurementPanel, new CellBuilder().y(1).build());
        panel.add(buttonsPanel, new CellBuilder().y(2).build());

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                buttonsPanel.clickCloseButton();
            }
        });
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
        return false;
    }
}
