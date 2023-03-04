package model.dto.builder;

import model.dto.Sensor;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SensorBuilder {
    private final Sensor sensor;

    public SensorBuilder() {
        sensor = new Sensor();
    }

    public SensorBuilder(@Nonnull String channelCode) {
        sensor = new Sensor(channelCode);
    }

    public SensorBuilder setChannelCode(@Nonnull String code) {
        sensor.setChannelCode(code);
        return this;
    }

    public SensorBuilder setType(String type) {
        sensor.setType(type == null ? EMPTY : type);
        return this;
    }

    public SensorBuilder setSerialNumber(String number) {
        sensor.setSerialNumber(number == null ? EMPTY : number);
        return this;
    }

    public SensorBuilder setMeasurementName(String name) {
        sensor.setMeasurementName(name == null ? EMPTY : name);
        return this;
    }

    public SensorBuilder setMeasurementValue(String value) {
        sensor.setMeasurementValue(value == null ? EMPTY : value);
        return this;
    }

    public SensorBuilder setRangeMin(double rangeMin) {
        sensor.setRangeMin(rangeMin);
        return this;
    }

    public SensorBuilder setRangeMax(double rangeMax) {
        sensor.setRangeMax(rangeMax);
        return this;
    }

    public SensorBuilder setRange(double r1, double r2) {
        if (r1 > r2) {
            r1 = r1 + r2;
            r2 = r1 - r2;
            r1 -= r2;
        }
        sensor.setRangeMin(r1);
        sensor.setRangeMax(r2);

        return this;
    }

    public SensorBuilder setErrorFormula(String formula) {
        sensor.setErrorFormula(formula == null ? EMPTY : formula);
        return this;
    }

    public Sensor build() {
        return sensor;
    }
}
