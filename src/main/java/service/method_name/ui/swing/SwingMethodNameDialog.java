package service.method_name.ui.swing;

import application.ApplicationScreen;
import localization.Labels;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.method_name.MethodNameConfigHolder;
import service.method_name.ui.MethodNameContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;

import static model.ui.builder.CellBuilder.HORIZONTAL;

public class SwingMethodNameDialog extends DefaultDialog {
    private static final String CALCULATION_METHOD = "calculationMethod";

    public SwingMethodNameDialog(@Nonnull ApplicationScreen applicationScreen,
                                 @Nonnull MethodNameConfigHolder configHolder,
                                 @Nonnull MethodNameContext context) {
        super(applicationScreen, Labels.getLabels(SwingMethodNameDialog.class).get(CALCULATION_METHOD));

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
}
