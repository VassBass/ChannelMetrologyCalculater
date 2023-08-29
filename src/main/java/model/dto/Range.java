package model.dto;

import java.io.Serializable;
import java.util.Objects;

public class Range implements Serializable {
    private final double rangeMin;
    private final double rangeMax;
    private final String value;

    public Range(double rangeMin, double rangeMax, String value) {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.value = value;
    }

    public double getRangeMin() {
        return rangeMin;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return Double.compare(range.rangeMin, rangeMin) == 0 && Double.compare(range.rangeMax, rangeMax) == 0 && value.equals(range.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rangeMin, rangeMax, value);
    }
}
