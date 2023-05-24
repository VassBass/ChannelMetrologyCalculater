package service.measurement.info.ui.swing;

import model.dto.Measurement;
import model.ui.DefaultTextField;
import service.measurement.info.MeasurementInfoManager;
import service.measurement.info.ui.MeasurementInfoValuePanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;

public class SwingMeasurementInfoValuePanel extends DefaultTextField implements MeasurementInfoValuePanel {

    public SwingMeasurementInfoValuePanel(@Nonnull MeasurementInfoManager manager, @Nullable Measurement oldMeasurement) {
        super(20);

        if (Objects.nonNull(oldMeasurement)) this.setText(oldMeasurement.getValue());

        this.setFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                manager.changeValue();
            }
        });
    }

    @Override
    public void setMeasurementValue(String value) {
        this.setText(value);
    }

    @Override
    public String getMeasurementValue() {
        return this.getText();
    }
}
