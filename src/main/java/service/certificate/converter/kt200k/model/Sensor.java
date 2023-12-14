package service.certificate.converter.kt200k.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sensor {
    private final boolean benchmark;
    private String zeroResistance;
    private String type;
    private String number;
    private String clearanceClass;
    private String owner;
    private String rangeMin;
    private String rangeMax;
    private String status;
    private final Map<String, Point> points;
    private String result;

    public Sensor(boolean benchmark) {
        this.benchmark = benchmark;
        points = new HashMap<>();
    }

    public boolean isBenchmark() {
        return benchmark;
    }

    public String getZeroResistance() {
        return zeroResistance;
    }

    public void setZeroResistance(String zeroResistance) {
        this.zeroResistance = zeroResistance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClearanceClass() {
        return clearanceClass;
    }

    public void setClearanceClass(String clearanceClass) {
        this.clearanceClass = clearanceClass;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(String rangeMin) {
        this.rangeMin = rangeMin;
    }

    public String getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(String rangeMax) {
        this.rangeMax = rangeMax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Point getPoint(String benchmarkPoint) {
        return points.get(benchmarkPoint);
    }

    public void setPoint(String benchmarkPoint, Point point) {
        points.put(benchmarkPoint, point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(number, sensor.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
