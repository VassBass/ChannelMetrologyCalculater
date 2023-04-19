package service.control_points.list.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.control_points.list.ControlPointsListConfigHolder;
import service.control_points.list.ControlPointsListManager;
import service.control_points.list.ui.ControlPointsListContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;

import static model.ui.builder.CellBuilder.BOTH;
import static model.ui.builder.CellBuilder.NONE;

public class SwingControlPointsListDialog extends JDialog implements UI {
    private static final String TITLE_TEXT = "Контрольні точки";

    private final ControlPointsListManager manager;
    private final SwingControlPointsListSortPanel sortPanel;

    public SwingControlPointsListDialog(@Nonnull ApplicationScreen applicationScreen,
                                        @Nonnull ControlPointsListConfigHolder configHolder,
                                        @Nonnull ControlPointsListContext context,
                                        @Nonnull ControlPointsListManager manager) {
        super(applicationScreen, TITLE_TEXT, true);
        this.manager = manager;

        sortPanel = context.getElement(SwingControlPointsListSortPanel.class);
        SwingControlPointsListTable table = context.getElement(SwingControlPointsListTable.class);
        SwingControlPointsListButtonsPanel buttonsPanel = context.getElement(SwingControlPointsListButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(sortPanel, new CellBuilder().fill(NONE).weightY(0.05).y(0).build());
        panel.add(new JScrollPane(table), new CellBuilder().fill(BOTH).weightY(0.9).y(1).build());
        panel.add(buttonsPanel, new CellBuilder().fill(BOTH).weightY(0.05).y(2).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        if (sortPanel.getSelectedMeasurementName().equals(SwingControlPointsListSortPanel.ALL_TEXT)) {
            manager.showAllControlPointsInTable();
        } else {
            manager.showSortedControlPointsInTable();
        }
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
