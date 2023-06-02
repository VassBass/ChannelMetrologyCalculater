package service.channel.exchange.heroku.model;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Check {
    private static final int CODE = 0;
    private static final int NAME = 1;
    private static final int TECHNOLOGY_NUMBER = 2;

    private String code;
    private String name;
    private String technologyNumber;
    private String additionalInfo;
    private String sensorType;
    private String sensorSerialNumber;
    private double rangeMin;
    private double rangeMax;
    private double sensorRangeMin;
    private double sensorRangeMax;
    private Map<Double, List<Double>> values;

    public Check(@Nonnull String id, int idType) {
        if (idType == CODE) {
            code = id;
        } else if (idType == TECHNOLOGY_NUMBER) {
            technologyNumber = id;
        } else name = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnologyNumber() {
        return technologyNumber;
    }

    public void setTechnologyNumber(String technologyNumber) {
        this.technologyNumber = technologyNumber;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorSerialNumber() {
        return sensorSerialNumber;
    }

    public void setSensorSerialNumber(String sensorSerialNumber) {
        this.sensorSerialNumber = sensorSerialNumber;
    }

    public double getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(double rangeMin) {
        this.rangeMin = rangeMin;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(double rangeMax) {
        this.rangeMax = rangeMax;
    }

    public double getSensorRangeMin() {
        return sensorRangeMin;
    }

    public void setSensorRangeMin(double sensorRangeMin) {
        this.sensorRangeMin = sensorRangeMin;
    }

    public double getSensorRangeMax() {
        return sensorRangeMax;
    }

    public void setSensorRangeMax(double sensorRangeMax) {
        this.sensorRangeMax = sensorRangeMax;
    }

    public Map<Double, List<Double>> getValues() {
        return values;
    }

    public void setValues(Map<Double, List<Double>> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Check check = (Check) o;
        return Objects.equals(code, check.code) &&
                Objects.equals(name, check.name) &&
                Objects.equals(technologyNumber, check.technologyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, technologyNumber);
    }
}
