package model;

import java.io.Serializable;

public class ControlPointsValues implements Serializable {
    private final String sensorType;
    private final double rangeMin, rangeMax;
    private final double[] values;

    public ControlPointsValues(String sensorType, double rangeMin, double rangeMax, double[]values){
        this.sensorType = sensorType;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.values = values;
    }

    public String getSensorType(){return this.sensorType;}
    public double getRangeMin(){return this.rangeMin;}
    public double getRangeMax(){return this.rangeMax;}
    public double[]getValues(){return this.values;}

    public boolean isMatch(String sensorType, double rangeMin, double rangeMax){
        return this.sensorType.equals(sensorType) &&
                this.rangeMin == rangeMin && this.rangeMax == rangeMax;
    }

    public boolean isMatch(ControlPointsValues controlPointsValues){
        return this.sensorType.equals(controlPointsValues.getSensorType()) &&
                this.rangeMin == controlPointsValues.getRangeMin() &&
                this.rangeMax == controlPointsValues.getRangeMax();
    }
}
