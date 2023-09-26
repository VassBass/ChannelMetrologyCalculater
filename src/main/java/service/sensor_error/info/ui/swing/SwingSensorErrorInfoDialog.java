package service.sensor_error.info.ui.swing;

import model.dto.SensorError;
import model.ui.DefaultDialog;
import model.ui.DefaultPanel;
import model.ui.builder.CellBuilder;
import service.sensor_error.info.SensorErrorInfoConfigHolder;
import service.sensor_error.info.ui.SensorErrorInfoContext;
import service.sensor_error.list.ui.swing.SwingSensorErrorListDialog;
import util.ScreenPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class SwingSensorErrorInfoDialog extends DefaultDialog {
    private static final String TITLE_TEXT = "Формула похибки ПВП";

    public SwingSensorErrorInfoDialog(@Nonnull SwingSensorErrorListDialog parentDialog,
                                      @Nonnull SensorErrorInfoConfigHolder configHolder,
                                      @Nonnull SensorErrorInfoContext context,
                                      @Nullable SensorError oldError) {
        super(parentDialog, TITLE_TEXT);

        SwingSensorErrorInfoSensorPanel sensorPanel = context.getElement(SwingSensorErrorInfoSensorPanel.class);
        SwingSensorErrorInfoErrorPanel errorPanel = context.getElement(SwingSensorErrorInfoErrorPanel.class);
        SwingSensorErrorInfoButtonsPanel buttonsPanel = context.getElement(SwingSensorErrorInfoButtonsPanel.class);

        if (Objects.nonNull(oldError)) {
            sensorPanel.setSensorType(oldError.getType());
            sensorPanel.setRange(oldError.getRangeMin(), oldError.getRangeMax());
            sensorPanel.setMeasurementValue(oldError.getMeasurementValue());
            errorPanel.setErrorFormula(oldError.getErrorFormula());
        }

        DefaultPanel panel = new DefaultPanel();
        panel.add(sensorPanel, new CellBuilder().y(0).build());
        panel.add(errorPanel, new CellBuilder().y(1).build());
        panel.add(buttonsPanel, new CellBuilder().y(2).build());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(parentDialog, this));
        this.setContentPane(panel);
    }
}
