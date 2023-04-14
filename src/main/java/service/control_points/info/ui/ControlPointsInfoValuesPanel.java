package service.control_points.info.ui;

import javax.annotation.Nullable;
import java.util.Map;

public interface ControlPointsInfoValuesPanel {
    void setValues(Map<Double, Double> values);
    @Nullable Map<Double, Double> getValues();
}
