package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class SensorError implements Serializable {
    private String id;
    private String type;
    private String errorFormula;
    private double rangeMin;
    private double rangeMax;
    private String measurementValue;

    private SensorError() {}

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getErrorFormula() {
        return errorFormula;
    }

    public double getRangeMin() {
        return rangeMin;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public String getMeasurementValue() {
        return measurementValue;
    }

    public void setErrorFormula(String errorFormula) {
        this.errorFormula = errorFormula;
    }

    public void setType(String type) {
        this.type = type;
        this.generateId();
    }

    public void setRangeMin(double rangeMin) {
        this.rangeMin = rangeMin;
        this.generateId();
    }

    public void setRangeMax(double rangeMax) {
        this.rangeMax = rangeMax;
        this.generateId();
    }

    public void setMeasurementValue(String value) {
        this.measurementValue = value;
        this.generateId();
    }

    public static SensorError create(String sensorType,
                                     double channelRangeMin, double channelRangeMax, String measurementValue,
                                     String errorFormula) {
        SensorError error = new SensorError();
        error.type = sensorType;
        error.rangeMin = channelRangeMin;
        error.rangeMax = channelRangeMax;
        error.errorFormula = errorFormula;
        error.measurementValue = measurementValue;
        error.generateId();
        return error;
    }

    private void generateId() {
        id = String.format("%s[%s...%s %s]", type, rangeMin, rangeMax, measurementValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorError error = (SensorError) o;
        return id.equals(error.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rangeMin, rangeMax, measurementValue);
    }

    @Override
    public String toString() {
        return id;
    }

    public static SensorError getCopyOf(SensorError error) {
        return create(error.type, error.rangeMin, error.rangeMax, error.measurementValue, error.errorFormula);
    }
}
