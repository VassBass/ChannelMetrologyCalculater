package service.measurement.converter.ui;

public interface ResultPanel {
    void appendResult(String sourceMeasurementValue, double sourceValue,
                      String resultMeasurementValue, double resultValue);
}
