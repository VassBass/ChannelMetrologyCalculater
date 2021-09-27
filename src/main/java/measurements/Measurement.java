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

    //Getters
    public String getName() {return this.name.getValue();}
    public MeasurementConstants getNameConstant(){return this.name;}
    public String getValue() {return this.value.getValue();}
    public MeasurementConstants getValueConstant(){return this.value;}

    //Setters
    public void setName(MeasurementConstants name) {this.name = name;}
    public void setValue(MeasurementConstants value){this.value = value;}

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(this.getClass())){
            return false;
        }
        Measurement m = (Measurement)object;
        return this.name == m.getNameConstant() && this.value == m.getValueConstant();
    }
}
