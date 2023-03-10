package model.dto;

import java.io.Serializable;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = control_points
 */
public class ControlPoints implements Serializable {
    public static final long serialVersionUID = 6L;

    public static String createName(String sensorType, double channelRangeMin, double channelRangeMax) {
        return String.format("%s [%s...%s]", sensorType, channelRangeMin, channelRangeMax);
    }

    /**
     * DB field = name (primary key) [TEXT]
     */
    private String name = EMPTY;

    /**
     * DB field = sensor_type [TEXT]
     */
    private String sensorType = EMPTY;

    /**
     * DB field = points [TEXT]
     * key - percent value of point
     * value - value of point
     */
    private Map<Double, Double> values = new HashMap<>();

    public ControlPoints(){}

    public ControlPoints(String name){
        this.name = name;
    }

    public String getName(){return this.name;}
    public String getSensorType(){return this.sensorType;}
    public Map<Double, Double>getValues(){
        return this.values;
    }

    public void setName(String name){this.name = name;}
    public void setSensorType(String sensorType){this.sensorType = sensorType;}
    public void setValues(Map<Double, Double>values){this.values = values;}

    @Override
    public int hashCode() {
        return Objects.hash(name, sensorType, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;
        ControlPoints cp = (ControlPoints) obj;
        return cp.getName().equalsIgnoreCase(this.name);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(String.format("%s(%s) = [", name, sensorType));
        for (Double v : values.keySet()) {
            builder
                    .append(v)
                    .append("%-")
                    .append(values.get(v))
                    .append(",");
        }
        builder.setCharAt(builder.length()-1, ']');
        return builder.toString();
    }
}