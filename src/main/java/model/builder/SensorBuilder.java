package model.builder;

import model.Sensor;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SensorBuilder {
    private final Sensor sensor;

    public SensorBuilder() {
        sensor = new Sensor();
    }

    public SensorBuilder(@Nonnull String name) {
        sensor = new Sensor(name);
    }

    public SensorBuilder setName(@Nonnull String name) {
        sensor.setName(name);
        return this;
    }

    public SensorBuilder setType(String type) {
        sensor.setType(type == null ? EMPTY : type);
        return this;
    }

    public SensorBuilder setNumber(String number) {
        sensor.setNumber(number == null ? EMPTY : number);
        return this;
    }

    public SensorBuilder setMeasurementName(String name) {
        sensor.setMeasurement(name == null ? EMPTY : name);
        return this;
    }

    public SensorBuilder setMeasurementValue(String value) {
        sensor.setValue(value == null ? EMPTY : value);
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
