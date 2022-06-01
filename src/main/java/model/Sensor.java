package model;

import application.Application;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

/**
 * DB table = sensors
 */
public class Sensor implements Serializable {
    /**
     * @see repository.impl.ChannelRepositoryImpl#changeSensorInCurrentThread(Sensor, Sensor, int...)
     */
    public static int TYPE = 0;
    public static int NAME = 1;
    public static int RANGE = 2;
    public static int NUMBER = 3;
    public static int VALUE = 4;
    public static int MEASUREMENT = 5;
    public static int ERROR_FORMULA = 6;

    /**
     * Default sensors types
     * @see #getType()
     * @see #setType(String)
     */
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
     *
     * @see Measurement
     */
    private String value = "";

    /**
     * DB field = measurement [TEXT]
     *
     * @see Measurement
     */
    private String measurement = "";

    /**
     * DB field = error_formula [TEXT]
     *
     * @see #getError(Channel)
     *
     * R - Measurement range of channel (Диапазон измерения канала)
     * @see Channel#_getRange()
     *
     * r - Measurement range of sensor (Диапазон измерения датчика)
     //* @see Sensor#getRange()
     *
     * convR - Measurement range of sensor converted by measurement channel value
     * (Диапазон измерения датчика переконвертированый под измерительную величину канала)
     * @see Measurement#convertTo(String, double)
     * @see Measurement#convertFrom(String, double)
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
     * @param channel against which the calculation is made
     * @return numerical value calculated by the {@link #errorFormula}
     *
     * R - Measurement range of channel (Диапазон измерения канала)
     * @see Channel#_getRange()
     *
     * r - Measurement range of sensor (Диапазон измерения датчика)
     * @see Calibrator#_getRange()
     *
     * convR - Measurement range of sensor converted by measurement channel value
     * (Диапазон измерения датчика переконвертированый под измерительную величину канала)
     * @see Measurement#convertTo(String, double)
     * @see Measurement#convertFrom(String, double)
     * conv(...) - number converted by measurement channel value
     * (Число переконвертированное под измерительную величину канала)
     * @see Measurement#getErrorStringAfterConvertNumbers(String, Measurement, Measurement)
     */
    public double getError(@Nonnull Channel channel){
        String formula = VariableConverter.commasToDots(this.errorFormula);
        Measurement input = Application.context.measurementService.get(this.measurement);
        formula = Measurement.getErrorStringAfterConvertNumbers(formula, input, channel.getMeasurement());
        Function f = new Function("At(R,r,convR) = " + formula);
        Argument R = new Argument("R = " + channel._getRange());
        double cR = channel.getMeasurement().convertFrom(this.value, this._getRange());
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
     * If you need to compare all fields of Sensors use {@link #isMatch(Sensor, int...)}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;

        Sensor in = (Sensor) obj;
        return in.getName().equals(this.name);
    }

    /**
     * @return {@link Sensor} in JsonString
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
     * @param json {@link Sensor} in JsonString
     *
     * @see com.fasterxml.jackson.core
     *
     * @return {@link Sensor}
     *
     * @throws JsonProcessingException - if jackson can't transform String to Ыутыщк
     */
    public static Sensor fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Sensor.class);
    }

    /**
     * @param sensor to compare
     * @param ignored ignored fields
     * @see #NAME
     * @see #TYPE
     * @see #RANGE
     * @see #NUMBER
     * @see #VALUE
     * @see #ERROR_FORMULA
     *
     * @return true if sensors fields equal
     */
    public boolean isMatch(@Nonnull Sensor sensor, int ... ignored){
        if (this.measurement.equals(sensor.getMeasurement())){
            StringBuilder builderThis = new StringBuilder();
            StringBuilder builderSensor = new StringBuilder();
            if (!contain(ignored, Sensor.NAME)){
                builderThis.append(this.name);
                builderSensor.append(sensor.getName());
            }
            if (!contain(ignored, Sensor.TYPE)){
                builderThis.append(this.type);
                builderSensor.append(sensor.getType());
            }
            if (!contain(ignored, Sensor.RANGE)){
                builderThis.append(this.rangeMin);
                builderThis.append(this.rangeMax);
                builderSensor.append(sensor.getRangeMin());
                builderSensor.append(sensor.getRangeMax());
            }
            if (!contain(ignored, Sensor.NUMBER)){
                builderThis.append(this.number);
                builderSensor.append(sensor.getNumber());
            }
            if (!contain(ignored, Sensor.VALUE)){
                builderThis.append(this.value);
                builderSensor.append(sensor.getValue());
            }
            if (!contain(ignored, Sensor.ERROR_FORMULA)){
                builderThis.append(this.errorFormula);
                builderSensor.append(sensor.getErrorFormula());
            }
            return builderThis.toString().equals(builderSensor.toString());
        } else return false;
    }

    private boolean contain(int[]source, int value){
        if (source == null || source.length == 0) return false;
        for (int i : source){
            if (i == value) return true;
        }
        return false;
    }
}