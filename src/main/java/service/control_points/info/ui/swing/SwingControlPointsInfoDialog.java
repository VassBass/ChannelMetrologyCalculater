package service.control_points.info.ui.swing;

import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.control_points.info.ControlPointsInfoConfigHolder;
import service.control_points.info.ui.ControlPointsInfoContext;
import service.control_points.list.ui.swing.SwingControlPointsListDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

public class SwingControlPointsInfoDialog extends JDialog implements UI {
    private static final String TITLE_TEXT = "Контрольні точки";

    public SwingControlPointsInfoDialog(@Nonnull SwingControlPointsListDialog parent,
                                        @Nonnull ControlPointsInfoContext context,
                                        @Nonnull ControlPointsInfoConfigHolder configHolder) {
        super(parent, TITLE_TEXT, true);

        SwingControlPointsInfoSensorTypePanel sensorTypePanel = context.getElement(SwingControlPointsInfoSensorTypePanel.class);
        SwingControlPointsInfoRangePanel rangePanel = context.getElement(SwingControlPointsInfoRangePanel.class);
        SwingControlPointsInfoValuesPanel valuesPanel = context.getElement(SwingControlPointsInfoValuesPanel.class);
        SwingControlPointsInfoButtonsPanel buttonsPanel = context.getElement(SwingControlPointsInfoButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(sensorTypePanel, new CellBuilder().weightY(0.05).width(1).x(0).y(0).build());
        panel.add(rangePanel, new CellBuilder().weightY(0.05).width(1).x(1).y(0).build());
        panel.add(valuesPanel, new CellBuilder().weightY(0.9).width(2).x(0).y(1).build());
        panel.add(buttonsPanel, new CellBuilder().weightY(0.05).width(2).x(0).y(2).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(parent, this));
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
