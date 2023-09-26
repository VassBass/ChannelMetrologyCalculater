package service.sensor_types.info.ui.swing;

import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_types.info.SensorTypesInfoConfigHolder;
import service.sensor_types.info.ui.SensorTypesInfoContext;
import service.sensor_types.list.ui.swing.SwingSensorTypesListDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;

public class SwingSensorTypesInfoDialog extends DefaultDialog {

    public SwingSensorTypesInfoDialog(@Nonnull SwingSensorTypesListDialog parent,
                                      @Nonnull String oldType,
                                      @Nonnull SensorTypesInfoConfigHolder configHolder,
                                      @Nonnull SensorTypesInfoContext context) {
        super(parent, oldType);

        SwingSensorTypesInfoTypePanel typePanel = context.getElement(SwingSensorTypesInfoTypePanel.class);
        SwingSensorTypesInfoButtonsPanel buttonsPanel = context.getElement(SwingSensorTypesInfoButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(typePanel, new CellBuilder().weightY(0.8).y(0).build());
        panel.add(buttonsPanel, new CellBuilder().weightY(0.2).y(1).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(parent, this));
        this.setContentPane(panel);
    }
}
