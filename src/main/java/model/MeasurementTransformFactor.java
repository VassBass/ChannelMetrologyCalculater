package model;

import java.util.Objects;

/**
 * DB table = measurement_factors
 */
public class MeasurementTransformFactor {

    /**
     * Source measurement value to transform
     * DB field = source [TEXT]
     *
     * @see Measurement#value
     */
    private final String transformFrom;

    /**
     * Measurement value transform result
     * DB field = result [TEXT]
     *
     * @see Measurement#value
     */
    private final String transformTo;

    /**
     * DB field = factor [REAL]
     */
    private final double transformFactor;

    public MeasurementTransformFactor(String transformFrom, String transformTo, double transformFactor) {
        this.transformFrom = transformFrom;
        this.transformTo = transformTo;
        this.transformFactor = transformFactor;
    }

    public String getTransformFrom() {
        return transformFrom;
    }

    public String getTransformTo() {
        return transformTo;
    }

    public double getTransformFactor() {
        return transformFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementTransformFactor that = (MeasurementTransformFactor) o;
        return Double.compare(that.transformFactor, transformFactor) == 0
                && transformFrom.equals(that.transformFrom)
                && transformTo.equals(that.transformTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transformFrom, transformTo, transformFactor);
    }

    @Override
    public String toString() {
        return "MeasurementTransformFactor{" +
                "transformFrom='" + transformFrom + '\'' +
                ", transformTo='" + transformTo + '\'' +
                ", transformFactor=" + transformFactor +
                '}';
    }
}
