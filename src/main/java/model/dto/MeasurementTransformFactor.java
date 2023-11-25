package model.dto;

import javax.annotation.Nonnegative;
import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = measurement_factors
 */
public class MeasurementTransformFactor implements Serializable {
    private static final long serialVersionUID = 6L;

    /**
     * DB field = id (primary key)[INTEGER]
     */
    private final int id;

    /**
     * Source measurement value to transform
     * DB field = source [TEXT]
     *
     * @see Measurement#value
     */
    private String transformFrom;

    /**
     * Measurement value transform result
     * DB field = result [TEXT]
     *
     * @see Measurement#value
     */
    private String transformTo;

    /**
     * DB field = factor [REAL]
     */
    private double transformFactor;

    public MeasurementTransformFactor(@Nonnegative int id,
                                      String transformFrom,
                                      String transformTo,
                                      double transformFactor) {
        this.id = id;
        this.transformFrom = transformFrom == null ? EMPTY : transformFrom;
        this.transformTo = transformTo == null ? EMPTY : transformTo;
        this.transformFactor = transformFactor;
    }

    public int getId() {
        return id;
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

    public void setTransformFrom(String transformFrom) {
        this.transformFrom = transformFrom;
    }

    public void setTransformTo(String transformTo) {
        this.transformTo = transformTo;
    }

    public void setTransformFactor(double transformFactor) {
        this.transformFactor = transformFactor;
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
        return "MeasurementTransformFactor[" + id + "]{" +
                "transformFrom='" + transformFrom + '\'' +
                ", transformTo='" + transformTo + '\'' +
                ", transformFactor=" + transformFactor +
                '}';
    }
}
