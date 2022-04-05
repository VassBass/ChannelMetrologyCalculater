package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * DB table = control_points
 */
public class ControlPointsValues implements Serializable {

    /**
     * DB field = sensor_type [TEXT]
     */
    private String sensorType;

    /**
     * DB fields = range_min, range_max [REAL]
     */
    private double rangeMin, rangeMax;

    /**
     * DB field = points [TEXT]
     * 
     * @see converters.VariableConverter#arrayToString(double[])
     * @see converters.VariableConverter#stringToArray(String)
     */
    private double[] values;

    public ControlPointsValues(){}

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

    public void setSensorType(String sensorType){this.sensorType = sensorType;}
    public void setValues(double[]values){this.values = values;}
    public void setRangeMin(double rangeMin){this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax){this.rangeMax = rangeMax;}

    @Override
    public int hashCode() {
        return Objects.hash(this.sensorType, this.rangeMin, this.rangeMax, Arrays.hashCode(this.values));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;
        ControlPointsValues cpv = (ControlPointsValues) obj;
        return cpv.getSensorType().equals(this.sensorType)
                && cpv.getRangeMin() == this.rangeMin
                && cpv.getRangeMax() == this.rangeMax;
    }

    public boolean equalsBy(String sensorType, double rangeMin, double rangeMax){
        return this.sensorType.equals(sensorType)
                && this.rangeMin == rangeMin
                && this.rangeMax == rangeMax;
    }

    /**
     * @return {@link ControlPointsValues} in JsonString
     * if 10 times in a row throws {@link JsonProcessingException} return null
     *
     * @see com.fasterxml.jackson.core
     */
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

    /**
     * @param json {@link ControlPointsValues} in JsonString
     *
     * @see com.fasterxml.jackson.core
     *
     * @return {@link ControlPointsValues}
     *
     * @throws JsonProcessingException - if jackson can't transform String to ControlPointsValues
     */
    public static ControlPointsValues fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, ControlPointsValues.class);
    }
}