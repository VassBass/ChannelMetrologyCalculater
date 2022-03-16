package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import constants.MeasurementConstants;
import converters.ValueConverter;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import support.Comparator;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

public class Calibrator implements Serializable {

    private String type = "";
    private String name = "";
    private Certificate certificate;
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
    public String getCertificateName(){return this.certificate.getName();}
    public Calendar getCertificateDate(){return this.certificate.getDate();}
    public String getCertificateCompany(){return this.certificate.getCompany();}
    public String getErrorFormula(){return this.errorFormula;}

    //Setters
    public void setType(String type) {this.type = type;}
    public void setName(String name){this.name = name;}
    public void setNumber(String number){this.number = number;}
    public void setRangeMin(double rangeMin){this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax){this.rangeMax = rangeMax;}
    public void setValue(String value){this.value = value;}
    public void setCertificateName(String name){this.certificate.setName(name);}
    public void setCertificateDate(Calendar date){this.certificate.setDate(date);}
    public void setCertificateCompany(String company){this.certificate.setCompany(company);}
    public void setErrorFormula(String errorFormula){this.errorFormula = errorFormula;}
    public void setMeasurement(String measurement){this.measurement = measurement;}
    public void setCertificate(Certificate certificate){this.certificate = certificate;}

    public String getCertificateInfo(){
        return this.certificate.getInfoString();
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

    public static class Certificate implements Serializable {
        private String name = "";
        private Calendar date = Calendar.getInstance();
        private String company = "";

        public String getName(){return this.name;}
        public Calendar getDate(){return this.date;}
        public String getCompany(){return this.company;}

        public void setName(String name){this.name = name;}
        public void setDate(Calendar date){this.date = date;}
        public void setCompany(String company){this.company = company;}
        public String getInfoString(){
            return this.name + " від " + VariableConverter.dateToString(this.date) + "р " + this.company;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.date, this.company);
        }

        @Override
        public boolean equals(Object object){
            if (!object.getClass().equals(this.getClass())){
                return false;
            }
            Certificate c = (Certificate) object;
            return this.name.equals(c.name) &&
                    this.company.equals(c.company) &&
                    Comparator.datesMatch(this.date, c.date);
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

        public static Certificate fromString (String json) throws JsonProcessingException {
            return new ObjectMapper().readValue(json, Certificate.class);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.name, this.measurement);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;

        Calibrator in = (Calibrator) obj;
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

    public static Calibrator fromString(String json){
        try {
            return new ObjectMapper().readValue(json, Calibrator.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}


