package service.measurement.converter.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.measurement.converter.ConverterConfigHolder;
import service.measurement.converter.ui.ConverterContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

public class ConverterDialog extends JDialog implements UI {
    private static final String TITLE_TEXT = "Перетворювач величин";

    public ConverterDialog(@Nonnull ApplicationScreen applicationScreen,
                           @Nonnull ConverterConfigHolder configHolder,
                           @Nonnull ConverterContext context) {
        super(applicationScreen, TITLE_TEXT, true);

        SwingMeasurementNamePanel measurementNamePanel = context.getElement(SwingMeasurementNamePanel.class);
        SwingSourceMeasurementValuePanel sourceValuePanel = context.getElement(SwingSourceMeasurementValuePanel.class);
        SwingResultMeasurementValuePanel resultValuePanel = context.getElement(SwingResultMeasurementValuePanel.class);
        SwingResultPanel resultPanel = context.getElement(SwingResultPanel.class);
        SwingButtonsPanel buttonsPanel = context.getElement(SwingButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(measurementNamePanel, new CellBuilder().weightY(0.05).width(1).x(0).y(0).build());
        panel.add(sourceValuePanel, new CellBuilder().weightY(0.05).width(1).x(1).y(0).build());
        panel.add(resultValuePanel, new CellBuilder().weightY(0.05).width(1).x(2).y(0).build());
        panel.add(new JScrollPane(resultPanel), new CellBuilder().weightY(0.9).width(3).x(0).y(1).build());
        panel.add(buttonsPanel, new CellBuilder().weightY(0.05).width(3).x(0).y(2).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
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
        dispose();
    }

    @Override
    public Object getSource() {
        return this;
    }
}