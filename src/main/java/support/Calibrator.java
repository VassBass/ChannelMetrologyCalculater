package support;

import constants.MeasurementConstants;
import converters.ValueConverter;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.io.Serializable;
import java.util.Calendar;

public class Calibrator implements Serializable {

    private String type = "";
    private String name = "";
    private final Certificate certificate;
    private String number = "";
    private String measurement = "";
    private double rangeMin = 0D;
    private double rangeMax = 0D;
    private String value = "";
    private String errorFormula = "";

    public Calibrator(){
        this.certificate = new Certificate();
    }

    //Getters
    public String getType() {return this.type;}
    public String getName(){return this.name;}
    public String getNumber(){return this.number;}
    public Certificate getCertificate(){return this.certificate;}
    public double getRangeMin(){return this.rangeMin;}
    public double getRangeMax(){return this.rangeMax;}
    public double getRange(){return this.rangeMax - this.rangeMin;}
    public String getValue(){return this.value;}
    public String getMeasurement(){return this.measurement;}
    public String getCertificateName(){return this.certificate.name;}
    public Calendar getCertificateDate(){return this.certificate.date;}
    public String getCertificateCompany(){return this.certificate.company;}
    public String getErrorFormula(){return this.errorFormula;}

    //Setters
    public void setType(String type) {this.type = type;}
    public void setName(String name){this.name = name;}
    public void setNumber(String number){this.number = number;}
    public void setRangeMin(double rangeMin){this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax){this.rangeMax = rangeMax;}
    public void setValue(String value){this.value = value;}
    public void setCertificateName(String name){this.certificate.name = name;}
    public void setCertificateDate(Calendar date){this.certificate.date = date;}
    public void setCertificateCompany(String company){this.certificate.company = company;}
    public void setErrorFormula(String errorFormula){this.errorFormula = errorFormula;}
    public void setMeasurement(String measurement){this.measurement = measurement;}

    public String getCertificateToString(){
        return this.certificate.name
                + " від "
                + VariableConverter.dateToString(this.certificate.date)
                + "р "
                + this.certificate.company;
    }

    /*
    R - Диапазон измерения канала
    r - Диапазон измерения калибратора
    convR - Диапазон измерения калибратора переконвертированый под измерительную величину канала
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
            cR = new ValueConverter(MeasurementConstants.getConstantFromString(this.value),
                    channel.getMeasurement().getValueConstant()).get(this.getRange());
        }
        Argument r = new Argument("r = " + this.getRange());
        Argument convR = new Argument("convR = " + cR);
        Expression expression = new Expression("At(R,r,convR)", f,R,r,convR);
        return expression.calculate();
    }

    protected static class Certificate implements Serializable {
        protected String name = "";
        protected Calendar date = Calendar.getInstance();
        protected String company = "";

        @Override
        public boolean equals(Object object){
            if (!object.getClass().equals(this.getClass())){
                return false;
            }
            Certificate c = (Certificate) object;
            return this.name.equals(c.name) &&
                    this.company.equals(c.company) &&
                    VariableConverter.dateToString(this.date).equals(VariableConverter.dateToString(c.date));
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(this.getClass())){
            return false;
        }
        Calibrator c = (Calibrator)object;
        return this.type.equals(c.getType()) &&
                this.name.equals(c.getName()) &&
                this.number.equals(c.getNumber()) &&
                this.measurement.equals(c.getMeasurement()) &&
                this.errorFormula.equals(c.getErrorFormula()) &&
                this.value.equals(c.getValue()) &&
                this.certificate.equals(c.getCertificate()) &&
                this.rangeMin == c.getRangeMin() &&
                this.rangeMax == c.getRangeMax();
    }

}


