package service.calibrator.info.ui;

public interface CalibratorInfoRangePanel {
    double getRangeMin();
    double getRangeMax();
    String getMeasurementValue();

    void setRangeMin(double value);
    void setRangeMax(double value);
    void setMeasurementValue(String value);
}
