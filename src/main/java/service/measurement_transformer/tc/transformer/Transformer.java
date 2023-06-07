package service.measurement_transformer.tc.transformer;

import service.measurement_transformer.tc.model.Type;

public interface Transformer {
    double transformFromTemperature(Type type, double r0, double temperatureValue);
    double transformFromResistance(Type type, double r0, double resistanceValue);
}
