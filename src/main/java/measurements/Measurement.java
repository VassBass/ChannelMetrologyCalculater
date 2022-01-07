package measurements;

import constants.MeasurementConstants;

import java.io.Serializable;

public class Measurement implements Serializable {

    protected MeasurementConstants name;
    protected MeasurementConstants value;

    public Measurement(MeasurementConstants name, MeasurementConstants value){
        this.name = name;
        this.value = value;
    }

    public Measurement(String name, String value){
        MeasurementConstants n = MeasurementConstants.getConstantFromString(name);
        MeasurementConstants v = MeasurementConstants.getConstantFromString(value);
        if (n == null || v == null){
            throw new NullPointerException();
        }else {
            this.name = n;
            this.value = v;
        }
    }

    //Getters
    public String getName() {return this.name.getValue();}
    public MeasurementConstants getNameConstant(){return this.name;}
    public String getValue() {return this.value.getValue();}
    public MeasurementConstants getValueConstant(){return this.value;}

    //Setters
    public void setName(MeasurementConstants name) {this.name = name;}
    public void setValue(MeasurementConstants value){this.value = value;}
}
