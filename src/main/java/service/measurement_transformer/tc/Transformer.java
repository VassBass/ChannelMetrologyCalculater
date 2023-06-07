package service.measurement_transformer.tc;

public interface Transformer {
    double transformFromTemperature(Type type, double r0, double temperatureValue);
    double transformFromResistance(Type type, double r0, double resistanceValue);
}
