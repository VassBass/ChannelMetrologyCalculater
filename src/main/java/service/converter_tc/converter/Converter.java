package service.converter_tc.converter;

import service.converter_tc.model.Type;

public interface Converter {
    double transformFromTemperature(Type type, double r0, double temperatureValue);
    double transformFromResistance(Type type, double r0, double resistanceValue);
}
