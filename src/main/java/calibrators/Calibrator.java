package calibrators;

import constants.CalibratorType;
import constants.MeasurementConstants;

import java.io.Serializable;
import java.util.ArrayList;

public class Calibrator implements Serializable {

    protected CalibratorType name;
    protected Certificate_calibrator certificate = null;
    protected String number = null;
    protected ArrayList<MeasurementConstants> measurements;
    protected double rangeMin = 0D;
    protected double rangeMax = 0D;
    protected String value = null;

    public Calibrator(){this.measurements = new ArrayList<>();}

    //Getters
    public CalibratorType getName() {return this.name;}
    public Certificate_calibrator getCertificate(){return this.certificate;}
    public String getNumber(){return this.number;}
    public ArrayList<MeasurementConstants>getMeasurements(){return this.measurements;}
    public double getRangeMin(){return this.rangeMin;}
    public double getRangeMax(){return this.rangeMax;}
    public double getRange(){return this.rangeMax - this.rangeMin;}
    public String getValue(){return this.value;}

    //Setters
    public void setName(CalibratorType name) {this.name = name;}
    public void setNumber(String number){this.number = number;}
    public void setRangeMin(double rangeMin){this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax){this.rangeMax = rangeMax;}
    public void setValue(String value){this.value = value;}

    public void createCertificate(){}

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(this.getClass())){
            return false;
        }
        Calibrator c = (Calibrator)object;
        return this.name.equals(c.getName());
    }

}


