package model;

import java.io.Serializable;

public class SensorValues implements Serializable {
    private double rangeMin, rangeMax;
    private double[]values;

    public SensorValues(double rangeMin, double rangeMax){
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public boolean isMatch(double rangeMin, double rangeMax){
        return this.rangeMin == rangeMin && this.rangeMax == rangeMax;
    }
    public boolean isMatch(SensorValues values){
        return this.rangeMin == values.getRangeMin() && this.rangeMax == values.getRangeMax();
    }

    public double[]getValues(){
        return this.values;
    }

    public void setValues(double[]values){
        this.values = values;
    }

    public double getRangeMin(){
        return this.rangeMin;
    }

    public void setRangeMin(double rangeMin){
        this.rangeMin = rangeMin;
    }

    public double getRangeMax(){
        return this.rangeMax;
    }

    public void setRangeMax(double rangeMax){
        this.rangeMax = rangeMax;
    }

    @Override
    public int hashCode() {
        double d = 0D;
        for (double v : this.values){
            d += v;
        }
        double s = this.rangeMin + this.rangeMax + d;
        return (int) s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        SensorValues input = (SensorValues) obj;
        if (!this.isMatch(input.getRangeMin(), input.getRangeMax())
                || this.getValues().length != input.getValues().length) return false;
        for (int i=0;i<this.values.length;i++){
            if (this.values[i] != input.getValues()[i]){
                return false;
            }
        }
        return true;
    }
}