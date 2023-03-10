package model.dto;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = sensors
 */
public class Sensor implements Serializable {
    public static final long serialVersionUID = 6L;

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
    public Sensor(String channelCode){
        this.channelCode = channelCode;
    }

    /**
     * DB field = type [TEXT]
     */
    private String type = EMPTY;

    /**
     * DB field = channel_code (primary key) [TEXT]
     */
    private String channelCode = EMPTY;

    /**
     * DB fields range_min, range_max [REAL]
     */
    private double rangeMin = 0D;
    private double rangeMax = 0D;

    /**
     * DB field = serial_number [TEXT]
     */
    private String serialNumber = EMPTY;

    /**
     * DB field = measurement_value [TEXT]
     *
     * @see Measurement
     */
    private String measurementValue = EMPTY;

    /**
     * DB field = measurement_name [TEXT]
     *
     * @see Measurement
     */
    private String measurementName = EMPTY;

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
    public void setChannelCode(String channelCode){this.channelCode = channelCode;}
    public void setRangeMin(double min) {this.rangeMin = min;}
    public void setRangeMax(double max) {this.rangeMax = max;}
    public void setSerialNumber(String serialNumber){this.serialNumber = serialNumber;}
    public void setMeasurementValue(String value){this.measurementValue = value;}
    public void setMeasurementName(String name){this.measurementName = name;}
    public void setErrorFormula(String formula){this.errorFormula = formula;}

    public String getType() {return this.type;}
    public String getChannelCode(){return this.channelCode;}
    public double getRangeMin() {return this.rangeMin;}
    public double getRangeMax() {return this.rangeMax;}
    public String getSerialNumber(){return this.serialNumber;}
    public String getMeasurementValue(){return this.measurementValue;}
    public String getMeasurementName(){return this.measurementName;}
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
        return Objects.hash(this.type, this.channelCode, this.serialNumber);
    }

    /**
     *
     * This method has been rewritten to work with Collections.
     * @see java.util.Collection#contains(Object)
     *
     * @return true if sensors names is equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;

        Sensor in = (Sensor) obj;
        return in.getChannelCode().equals(this.channelCode);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", type, serialNumber);
    }
}