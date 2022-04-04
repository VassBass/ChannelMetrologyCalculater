package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import converters.ValueConverter;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.io.Serializable;
import java.util.Objects;

//DB table = sensors
public class Sensor implements Serializable {
    public static int TYPE = 0;
    public static int NAME = 1;
    public static int RANGE = 2;
    public static int NUMBER = 3;
    public static int VALUE = 4;
    public static int MEASUREMENT = 5;
    public static int ERROR_FORMULA = 6;

    public static final String YOKOGAWA = "YOKOGAWA";
    public static final String ROSEMOUNT = "ROSEMOUNT";
    public static final String TCM_50M = "ТСМ-50М";
    public static final String Pt100 = "ТОП  Pt 100";

    /**
     * DB field = type [TEXT]
     */
    private String type;

    /**
     * DB field = name (primary key) [TEXT]
     */
    private String name = "Sensor";

    /**
     * DB fields range_min, range_max [REAL]
     */
    private double rangeMin = 0D;
    private double rangeMax = 0D;

    /**
     * DB field = number [TEXT]
     */
    private String number = "";

    /**
     * DB field = value [TEXT]
     */
    private String value = "";

    /**
     * DB field = measurement [TEXT]
     */
    private String measurement = "";

    /**
     * DB field = error_formula [TEXT]
     */
    private String errorFormula = "";

    public void setType(String type) {this.type = type;}
    public void setName(String name){this.name = name;}
    public void setRange(double min, double max) {
        this.rangeMin = min;
        this.rangeMax = max;
    }
    public void setRangeMin(double min) {this.rangeMin = min;}
    public void setRangeMax(double max) {this.rangeMax = max;}
    public void setNumber(String number){this.number = number;}
    public void setValue(String value){this.value = value;}
    public void setMeasurement(String measurement){this.measurement = measurement;}
    public void setErrorFormula(String formula){this.errorFormula = formula;}

    public String getType() {return this.type;}
    public String getName(){return this.name;}
    public double getRangeMin() {return this.rangeMin;}
    public double getRangeMax() {return this.rangeMax;}
    public double _getRange(){return this.rangeMax - this.rangeMin;}
    public String getNumber(){return this.number;}
    public String getValue(){return this.value;}
    public String getMeasurement(){return this.measurement;}
    public String getErrorFormula(){return this.errorFormula;}

    /**
     * R - Диапазон измерения канала
     * r - Диапазон измерения датчика
     * convR - Диапазон измерения датчика переконвертированый под измерительную величину канала
     */
    public double getError(Channel channel){
        String formula = VariableConverter.commasToDots(this.errorFormula);
        Function f = new Function("At(R,r,convR) = " + formula);
        Argument R;
        double cR;
        if (channel == null){
            R = new Argument("R = 0");
            cR = 0D;
        }else {
            R = new Argument("R = " + channel.getRange());
            cR = new ValueConverter(this.value, channel.getMeasurement().getValue()).get(this._getRange());
        }
        Argument r = new Argument("r = " + this._getRange());
        Argument convR = new Argument("convR = " + cR);
        Expression expression = new Expression("At(R,r,convR)", f,R,r,convR);
        return expression.calculate();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.name, this.measurement);
    }

    /**
     *
     * This method has been rewritten to work with ArrayList.
     *
     * @return true if sensors names is equal
     *
     * If you need to compare all fields of Sensors use {@link #isMatch(Sensor)}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;

        Sensor in = (Sensor) obj;
        return in.getName().equals(this.name);
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

    public static Sensor fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Sensor.class);
    }

    /**
     *
     * @param sensor to compare with this
     * @return true if sensors fields equal
     */
    public boolean isMatch(Sensor sensor){
        if (this.measurement.equals(sensor.getMeasurement())){
            if (this.measurement.equals(Measurement.TEMPERATURE) ||
                    this.measurement.equals(Measurement.CONSUMPTION)) {
                return this.name.equals(sensor.getName())
                        && this.type.equals(sensor.getType())
                        && this.rangeMin == sensor.getRangeMin()
                        && this.rangeMax == sensor.getRangeMax()
                        && this.number.equals(sensor.getNumber())
                        && this.value.equals(sensor.getValue())
                        && this.errorFormula.equals(sensor.getErrorFormula());
            } else if (this.measurement.equals(Measurement.PRESSURE)) {
                return this.name.equals(sensor.getName())
                        && this.type.equals(sensor.getType())
                        && this.number.equals(sensor.getNumber())
                        && this.errorFormula.equals(sensor.getErrorFormula());
            } else return false;
        } else return false;
    }
}
