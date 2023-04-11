package service.sensor_error.info.ui.swing;

import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.sensor_error.info.SensorErrorInfoConfigHolder;
import service.sensor_error.info.ui.SensorErrorInfoContext;
import service.sensor_error.list.ui.swing.SwingSensorErrorListDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

public class SwingSensorErrorInfoDialog extends JDialog implements UI {
    private static final String TITLE_TEXT = "Формула похибки ПВП";

    public SwingSensorErrorInfoDialog(@Nonnull SwingSensorErrorListDialog parentDialog,
                                      @Nonnull SensorErrorInfoConfigHolder configHolder,
                                      @Nonnull SensorErrorInfoContext context) {
        super(parentDialog, TITLE_TEXT, true);

        SwingSensorErrorInfoSensorPanel sensorPanel = context.getElement(SwingSensorErrorInfoSensorPanel.class);
        SwingSensorErrorInfoErrorPanel errorPanel = context.getElement(SwingSensorErrorInfoErrorPanel.class);
        SwingSensorErrorInfoButtonsPanel buttonsPanel = context.getElement(SwingSensorErrorInfoButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(sensorPanel, new CellBuilder().y(0).build());
        panel.add(errorPanel, new CellBuilder().y(1).build());
        panel.add(buttonsPanel, new CellBuilder().y(2).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(parentDialog, this));
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
