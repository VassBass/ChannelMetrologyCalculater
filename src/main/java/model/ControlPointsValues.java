package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DB table = control_points
 */
public class ControlPointsValues implements Serializable {

    /**
     * DB field = id (primary key) [INTEGER]
     */
    private int id;

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
     */
    private List<Double> values;

    public ControlPointsValues(){}

    public ControlPointsValues(int id){
        this.id = id;
    }

    public ControlPointsValues(String sensorType, double rangeMin, double rangeMax, List<Double>values){
        this.sensorType = sensorType;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.values = values;
    }

    public int getId(){return this.id;}
    public String getSensorType(){return this.sensorType;}
    public double getRangeMin(){return this.rangeMin;}
    public double getRangeMax(){return this.rangeMax;}
    public List<Double>getValues(){return this.values;}

    public String _getValuesString() {
        StringBuilder builder = new StringBuilder();
        for (double v : this.values) {
            builder.append(v);
            builder.append("|");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

    public void setId(int id){this.id = id;}
    public void setSensorType(String sensorType){this.sensorType = sensorType;}
    public void setValues(List<Double>values){this.values = values;}
    public void setRangeMin(double rangeMin){this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax){this.rangeMax = rangeMax;}

    public void _setValuesFromString(String str) {
        this.values = Arrays.stream(str.split("\\|"))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sensorType, rangeMin, rangeMax, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;
        ControlPointsValues cpv = (ControlPointsValues) obj;
        return cpv.getId() == this.id;
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