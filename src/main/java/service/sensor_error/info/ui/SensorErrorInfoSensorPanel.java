package service.sensor_error.info.ui;

import javax.annotation.Nonnull;

public interface SensorErrorInfoSensorPanel {
    void setSensorType(@Nonnull String type);
    void setRangeMin(double min);
    void setRangeMax(double max);
    void setRange(double r1, double r2);
    void setMeasurementValue(@Nonnull String value);

    String getSensorType();
    double getRangeMin();
    double getRangeMax();
    String getMeasurementValue();
}
