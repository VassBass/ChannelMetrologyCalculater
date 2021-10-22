package support;

import constants.MeasurementConstants;
import constants.SensorType;
import converters.PressureConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.io.Serializable;

public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    protected SensorType type;
    protected String name = "Sensor";
    protected double rangeMin = 0D;
    protected double rangeMax = 0D;
    protected String number = "";
    protected String value = "";
    protected String measurement = "";
    protected String errorFormula;

    //Setters
    public void setType(SensorType type) {this.type = type;}
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

    //Getters
    public SensorType getType() {return this.type;}
    public String getName(){return this.name;}
    public double getRangeMin() {return this.rangeMin;}
    public double getRangeMax() {return this.rangeMax;}
    public double getRange(){return this.rangeMax - this.rangeMin;}
    public String getNumber(){return this.number;}
    public String getValue(){return this.value;}
    public String getMeasurement(){return this.measurement;}

    /*
    R - Диапазон измерения канала
    r - Диапазон измерения датчика
     */
    public double getError(Channel channel){
        Function f = new Function("At(R,r,convR) = " + this.errorFormula);
        Argument R = new Argument("R = " + channel.getRange());
        Argument r = new Argument("r = " + this.getRange());
        double cR = new PressureConverter(MeasurementConstants.getConstantFromString(this.value),
                channel.getMeasurement().getValueConstant()).get(this.getRange());
        Argument convR = new Argument("convR = " + cR);
        Expression expression = new Expression("At(R,r,convR)", f,R,r,convR);
        return expression.calculate();
    }

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(this.getClass())){
            return false;
        }
        Sensor s = (Sensor)object;
        if (this.type.equals(s.getType())
                && this.rangeMin==s.getRangeMin()
                && this.rangeMax==s.getRangeMax()
                && this.value.equals(s.getValue())) {
            if (this.number.length() == 0) {
                return s.getNumber().length() == 0;
            }else {
                if (s.getNumber().length() == 0){
                    return false;
                }else {
                    return this.number.equals(s.getNumber());
                }
            }
        }else {
            return false;
        }
    }
}
