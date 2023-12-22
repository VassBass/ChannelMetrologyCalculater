package service.certificate.converter.kt200k.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sensor {
    private final boolean benchmark;
    private String zeroResistance;
    private String type;
    private String number;
    private ClearanceClass clearanceClass;
    private String owner;
    private String rangeMin;
    private String rangeMax;
    private String remark;
    private final List<Point> points;

    private static final Sensor mock = new Sensor(false);

    public Sensor(boolean benchmark) {
        this.benchmark = benchmark;
        points = new ArrayList<>();
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

    public ClearanceClass getClearanceClass() {
        return clearanceClass;
    }

    public void setClearanceClass(ClearanceClass clearanceClass) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String status) {
        this.remark = status;
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public Point getLastPoint() {
        return points.isEmpty() ? null : points.get(points.size() - 1);
    }

    public void addPoint(Point point) {
        points.add(point);
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

    @Override
    public String toString() {
        return "Sensor{" +
                "\n\tbenchmark=" + benchmark +
                ",\n\tzeroResistance=" + zeroResistance +
                ",\n\ttype=" + type +
                ",\n\tnumber=" + number +
                ",\n\tclearanceClass=" + clearanceClass +
                ",\n\towner=" + owner +
                ",\n\trangeMin=" + rangeMin +
                ",\n\trangeMax=" + rangeMax +
                ",\n\tremark=" + remark +
                ",\n\tpoints=" + points +
                "\n}";
    }

    public static Sensor getMock(String number) {
        mock.setNumber(number);
        return mock;
    }
}
