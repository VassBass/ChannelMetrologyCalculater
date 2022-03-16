package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import constants.MeasurementConstants;

import java.io.Serializable;
import java.util.Objects;

public class Measurement implements Serializable {

    protected int id;
    protected MeasurementConstants name, value;

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
    public int getId(){return this.id;}
    public String getName() {return this.name.getValue();}
    public MeasurementConstants getNameConstant(){return this.name;}
    public String getValue() {return this.value.getValue();}
    public MeasurementConstants getValueConstant(){return this.value;}

    //Setters
    public void setId(int id){this.id = id;}
    public void setName(MeasurementConstants name) {this.name = name;}
    public void setValue(MeasurementConstants value){this.value = value;}

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;
        Measurement measurement = (Measurement) obj;
        return this.name == measurement.getNameConstant()
                && this.value == measurement.getValueConstant();
    }

    @Override
    public String toString() {
        int attempt = 0;
        while (attempt < 10) {
            try {
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                return writer.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                attempt++;
            }
        }
        return null;
    }

    public static Measurement fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Measurement.class);
    }
}
