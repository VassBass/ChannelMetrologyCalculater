package service.measurement.converter.service;

public interface Converter {
    double convert(String fromMeasurementValue, String toMeasurementValue, double value);
}
