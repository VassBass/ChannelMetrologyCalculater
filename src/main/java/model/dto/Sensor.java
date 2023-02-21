package model.dto;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = sensors
 */
public class Sensor implements Serializable {
    /**
     * Default sensors types
     * @see #getType()
     * @see #setType(String)
     */
    public static final String YOKOGAWA = "YOKOGAWA";
    public static final String ROSEMOUNT = "ROSEMOUNT";
    public static final String TCM_50M = "ТСМ-50М";
    public static final String Pt100 = "ТОП  Pt 100";

    public Sensor(){}
    public Sensor(String name){
        this.name = name;
    }

    /**
     * DB field = type [TEXT]
     */
    private String type = EMPTY;

    /**
     * DB field = name (primary key) [TEXT]
     */
    private String name = EMPTY;

    /**
     * DB fields range_min, range_max [REAL]
     */
    private double rangeMin = 0D;
    private double rangeMax = 0D;

    /**
     * DB field = number [TEXT]
     */
    private String number = EMPTY;

    /**
     * DB field = value [TEXT]
     *
     * @see Measurement
     */
    private String value = EMPTY;

    /**
     * DB field = measurement [TEXT]
     *
     * @see Measurement
     */
    private String measurement = EMPTY;

    /**
     * DB field = error_formula [TEXT]
     *
     * @see #getError(Channel)
     *
     * R - Measurement range of channel (Диапазон измерения канала)
     * @see Channel#calculateRange()
     *
     * r - Measurement range of sensor (Диапазон измерения датчика)
     //* @see Sensor#getRange()
     *
     * convR - Measurement range of sensor converted by measurement channel value
     * (Диапазон измерения датчика переконвертированый под измерительную величину канала
     */
    private String errorFormula = EMPTY;

    public void setType(String type) {this.type = type;}
    public void setName(String name){this.name = name;}
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
    public String getNumber(){return this.number;}
    public String getValue(){return this.value;}
    public String getMeasurement(){return this.measurement;}
    public String getErrorFormula(){return this.errorFormula;}

    public double calculateRange(){return this.rangeMax - this.rangeMin;}

    /**
     * @param channel against which the calculation is made
     * @return numerical value calculated by the {@link #errorFormula}
     *
     * R - Measurement range of channel (Диапазон измерения канала)
     * @see Channel#calculateRange()
     *
     * r - Measurement range of sensor (Диапазон измерения датчика)
     * @see Calibrator#calculateRange()
     *
     * convR - Measurement range of sensor converted by measurement channel value
     * (Диапазон измерения датчика переконвертированый под измерительную величину канала)
     * conv(...) - number converted by measurement channel value
     * (Число переконвертированное под измерительную величину канала)
     * @see Measurement#getErrorStringAfterConvertNumbers(String, Measurement, Measurement)
     */
    public double getError(@Nonnull Channel channel){
//        String formula = VariableConverter.commasToDots(this.errorFormula);
//        Optional<Measurement> m = MeasurementRepositorySQLite.getInstance().get(value);
//        Measurement input = m.orElseGet(channel::getMeasurement);
//        formula = Measurement.getErrorStringAfterConvertNumbers(formula, input, channel.getMeasurement());
//        Function f = new Function("At(R,r,convR) = " + formula);
//        Argument R = new Argument("R = " + channel._getRange());
//        double cR = channel.getMeasurement().convertFrom(this.value, this._getRange());
//        Argument r = new Argument("r = " + this._getRange());
//        Argument convR = new Argument("convR = " + cR);
//        Expression expression = new Expression("At(R,r,convR)", f,R,r,convR);
//        return expression.calculate();
        return 0D;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.name, this.number);
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
        return String.format("%s[%s]", name, number);
    }

    /**
     * @param sensor to compare
     *
     * @return true if sensors fields equal
     */
    public boolean isMatch(Sensor sensor){
        if (sensor == null) return false;
        if (this == sensor) return true;

        return name.equals(sensor.getName()) &&
                type.equals(sensor.getType()) &&
                rangeMin == sensor.getRangeMin() &&
                rangeMax == sensor.getRangeMax() &&
                number.equals(sensor.getNumber()) &&
                value.equals(sensor.getValue()) &&
                measurement.equals(sensor.getMeasurement()) &&
                errorFormula.equals(sensor.getErrorFormula());

    }
}