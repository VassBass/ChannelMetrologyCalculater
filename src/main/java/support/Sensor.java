package support;

import constants.MeasurementConstants;
import converters.PressureConverter;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.io.Serializable;

public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String type;
    protected String name = "Sensor";
    protected double rangeMin = 0D;
    protected double rangeMax = 0D;
    protected String number = "";
    protected String value = "";
    protected String measurement = "";
    protected String errorFormula;

    //Setters
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

    //Getters
    public String getType() {return this.type;}
    public String getName(){return this.name;}
    public double getRangeMin() {return this.rangeMin;}
    public double getRangeMax() {return this.rangeMax;}
    public double getRange(){return this.rangeMax - this.rangeMin;}
    public String getNumber(){return this.number;}
    public String getValue(){return this.value;}
    public String getMeasurement(){return this.measurement;}
    public String getErrorFormula(){return this.errorFormula;}

    /*
    R - Диапазон измерения канала
    r - Диапазон измерения датчика
     */
    public double getError(Channel channel){
        String formula = VariableConverter.commasToDots(this.errorFormula);
        Function f = new Function("At(R,r,convR) = " + formula);
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
        return this.name.equals(s.getName());
    }
}
