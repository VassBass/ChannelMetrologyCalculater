package service.method_name.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.method_name.MethodNameConfigHolder;
import service.method_name.ui.MethodNameContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

import static model.ui.builder.CellBuilder.HORIZONTAL;

public class SwingMethodNameDialog extends JDialog implements UI {
    private static final String TITLE = "Методика розрахунку";

    public SwingMethodNameDialog(@Nonnull ApplicationScreen applicationScreen,
                                 @Nonnull MethodNameConfigHolder configHolder,
                                 @Nonnull MethodNameContext context) {
        super(applicationScreen, TITLE, true);

        SwingMeasurementNamePanel measurementNamePanel = context.getElement(SwingMeasurementNamePanel.class);
        SwingMethodNamePanel namePanel = context.getElement(SwingMethodNamePanel.class);
        SwingButtonsPanel buttonsPanel = context.getElement(SwingButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(measurementNamePanel, new CellBuilder().fill(HORIZONTAL).weightY(0.9).width(1).x(0).y(0).build());
        panel.add(namePanel, new CellBuilder().fill(HORIZONTAL).weightY(0.9).width(1).x(1).y(0).build());
        panel.add(buttonsPanel, new CellBuilder().weightY(0.1).width(2).x(0).y(1).margin(0, 20, 0, 0).build());

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
        this.dispose();
    }

    @Override
    public Object getSource() {
        return this;
    }
}
