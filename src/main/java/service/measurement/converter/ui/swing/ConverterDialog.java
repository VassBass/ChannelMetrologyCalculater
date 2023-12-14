package service.measurement.converter.ui.swing;

import application.ApplicationScreen;
import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.measurement.converter.ConverterConfigHolder;
import service.measurement.converter.ui.ConverterContext;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.swing.*;

public class ConverterDialog extends DefaultDialog {

    public ConverterDialog(@Nonnull ApplicationScreen applicationScreen,
                           @Nonnull ConverterConfigHolder configHolder,
                           @Nonnull ConverterContext context) {
        super(applicationScreen, Labels.getRootLabels().get(RootLabelName.CONVERTER));

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
}
