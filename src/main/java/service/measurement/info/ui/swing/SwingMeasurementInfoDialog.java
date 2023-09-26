package service.measurement.info.ui.swing;

import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.measurement.info.MeasurementInfoConfigHolder;
import service.measurement.info.ui.MeasurementInfoContext;
import service.measurement.list.ui.swing.SwingMeasurementListDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;

public class SwingMeasurementInfoDialog extends DefaultDialog {
    private static final String TITLE_TEXT = "Величина вимірюваннь";

    public SwingMeasurementInfoDialog(@Nonnull SwingMeasurementListDialog parentDialog,
                                      @Nonnull MeasurementInfoContext context,
                                      @Nonnull MeasurementInfoConfigHolder configHolder) {
        super(parentDialog, TITLE_TEXT);

        SwingMeasurementInfoNamePanel namePanel = context.getElement(SwingMeasurementInfoNamePanel.class);
        SwingMeasurementInfoValuePanel valuePanel = context.getElement(SwingMeasurementInfoValuePanel.class);
        SwingMeasurementInfoFactorsPanel factorsPanel = context.getElement(SwingMeasurementInfoFactorsPanel.class);
        SwingMeasurementInfoButtonsPanel buttonsPanel = context.getElement(SwingMeasurementInfoButtonsPanel.class);

        DefaultPanel panel = new DefaultPanel();
        panel.add(namePanel, new CellBuilder().weightY(0.05).width(1).x(0).y(0).build());
        panel.add(valuePanel, new CellBuilder().weightY(0.05).width(1).x(1).y(0).build());
        panel.add(factorsPanel, new CellBuilder().weightY(0.9).width(2).x(0).y(1).build());
        panel.add(buttonsPanel, new CellBuilder().weightY(0.05).width(2).x(0).y(2).build());

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(parentDialog, this));
        this.setContentPane(panel);
    }
}
