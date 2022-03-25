package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.Serializable;
import java.util.Objects;

public class Measurement implements Serializable {

    protected String name, value;

    public Measurement(){}

    public Measurement(String name, String value){
        if (name == null || value == null){
            throw new NullPointerException();
        }else {
            this.name = name;
            this.value = value;
        }
    }

    //Getters
    public String getName() {return this.name;}
    public String getValue() {return this.value;}

    //Setters
    public void setName(String name) {this.name = name;}
    public void setValue(String value){this.value = value;}

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;
        Measurement measurement = (Measurement) obj;
        return this.name.equals(measurement.getName())
                && this.value.equals(measurement.getValue());
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
