package service.measurement.converter;

public interface Converter {
    double convert(String fromMeasurementValue, String toMeasurementValue, double value);
}
