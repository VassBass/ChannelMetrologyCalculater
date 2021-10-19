package sensors;

import constants.SensorType;
import support.Channel;

import java.io.Serializable;

public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    protected SensorType type;
    protected double rangeMin = 0D;
    protected double rangeMax = 0D;
    protected String number = "";
    protected String value = "";
    protected String measurement = "";

    //Setters
    public void setType(SensorType type) {this.type = type;}
    public void setRange(double min, double max) {
        this.rangeMin = min;
        this.rangeMax = max;
    }
    public void setRangeMin(double min) {this.rangeMin = min;}
    public void setRangeMax(double max) {this.rangeMax = max;}
    public void setNumber(String number){this.number = number;}
    public void setValue(String value){this.value = value;}
    public void setMeasurement(String measurement){this.measurement = measurement;}

    //Getters
    public SensorType getType() {return this.type;}
    public double getRangeMin() {return this.rangeMin;}
    public double getRangeMax() {return this.rangeMax;}
    public String getNumber(){return this.number;}
    public String getValue(){return this.value;}
    public String getMeasurement(){return this.measurement;}

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(this.getClass())){
            return false;
        }
        Sensor s = (Sensor)object;
        if (this.type.equals(s.getType())
                && this.rangeMin==s.getRangeMin()
                && this.rangeMax==s.getRangeMax()
                && this.value.equals(s.getValue())) {
            if (this.number.length() == 0) {
                return s.getNumber().length() == 0;
            }else {
                if (s.getNumber().length() == 0){
                    return false;
                }else {
                    return this.number.equals(s.getNumber());
                }
            }
        }else {
            return false;
        }
    }
}
