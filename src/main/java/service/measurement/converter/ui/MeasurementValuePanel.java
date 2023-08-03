package service.measurement.converter.ui;

import javax.annotation.Nonnull;
import java.util.List;

public interface MeasurementValuePanel {
    void setMeasurementValuesList(@Nonnull List<String> measurementValuesList);
    String getMeasurementValue();
    double getValue();
}
