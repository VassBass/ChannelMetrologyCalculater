package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class SensorError implements Serializable {
    /**
     * DB field = id
     *
     * @see #generateId()
     */
    private String id;

    /**
     * DB field = type.
     *
     * @see Sensor#getType()
     */
    private String type;

    /**
     * DB field = error_formula
     *
     * @see Sensor#getErrorFormula()
     */
    private String errorFormula;

    /**
     * DB field = range_min
     * If {@link Sensor#getRangeMin()} >= this value &&
     * {@link Sensor#getRangeMax()} <= {@link #rangeMax}
     * then {@link #errorFormula} should be offered
     */
    private double rangeMin;

    /**
     * DB field = range_max
     * If {@link Sensor#getRangeMin()} >= {@link #rangeMin} &&
     * {@link Sensor#getRangeMax()} <= this value
     * then {@link #errorFormula} should be offered
     */
    private double rangeMax;

    /**
     * DB field = measurement_value
     */
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
