package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import converters.ValueConverter;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

//DB table = calibrators
public class Calibrator implements Serializable {
    public static final String FLUKE718_30G = "Fluke 718 30G";
    public static final String ROSEMOUNT_8714DQ4 = "ROSEMOUNT 8714DQ4";

    /**
     * DB field = type [TEXT]
     */
    @Nonnull private String type = "";

    /**
     * DB field = name (primary key) [TEXT]
     */
    @Nonnull private String name = "";

    /**
     * DB field = certificate [TEXT{Json}]
     */
    @Nonnull private Certificate certificate;

    /**
     * DB field = number [TEXT]
     */
    @Nonnull private String number = "";

    /**
     * DB field = measurement [TEXT]
     */
    @Nonnull private String measurement = "";

    /**
     * DB field = range_min [REAL]
     */
    private double rangeMin = 0D;

    /**
     * DB field = range_max [REAL]
     */
    private double rangeMax = 0D;

    /**
     * DB field = value [TEXT]
     */
    @Nonnull private String value = "";

    /**
     * DB field = error_formula [TEXT]
     */
    @Nonnull private String errorFormula = "";

    public Calibrator(){
        this.certificate = new Certificate();
    }

    //Getters
    @Nonnull public String getType() {return this.type;}
    @Nonnull public String getName(){return this.name;}
    @Nonnull public String getNumber(){return this.number;}
    @Nonnull public Certificate getCertificate(){return this.certificate;}
    public double getRangeMin(){return this.rangeMin;}
    public double getRangeMax(){return this.rangeMax;}
    public double getRange(){return this.rangeMax - this.rangeMin;}
    @Nonnull public String getValue(){return this.value;}
    @Nonnull public String getMeasurement(){return this.measurement;}
    @Nonnull public String getCertificateName(){return this.certificate.getName();}
    @Nonnull public String getCertificateDate(){return this.certificate.getDate();}
    @Nonnull public String getCertificateCompany(){return this.certificate.getCompany();}
    @Nonnull public String getErrorFormula(){return this.errorFormula;}

    //Setters
    public void setType(@Nonnull String type) {this.type = type;}
    public void setName(@Nonnull String name){this.name = name;}
    public void setNumber(@Nonnull String number){this.number = number;}
    public void setRangeMin(double rangeMin){this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax){this.rangeMax = rangeMax;}
    public void setValue(@Nonnull String value){this.value = value;}
    public void setCertificateName(@Nonnull String name){this.certificate.setName(name);}
    public void setCertificateDate(@Nonnull String date){this.certificate.setDate(date);}
    public void setCertificateCompany(@Nonnull String company){this.certificate.setCompany(company);}
    public void setErrorFormula(@Nonnull String errorFormula){this.errorFormula = errorFormula;}
    public void setMeasurement(@Nonnull String measurement){this.measurement = measurement;}
    public void setCertificate(@Nonnull Certificate certificate){this.certificate = certificate;}

    public String getCertificateInfo(){
        return this.certificate.getName() + " від " + this.certificate.getDate() + "р " + this.certificate.getCompany();
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
            cR = new ValueConverter(this.value, channel.getMeasurement().getValue()).get(this.getRange());
        }
        Argument r = new Argument("r = " + this.getRange());
        Argument convR = new Argument("convR = " + cR);
        Expression expression = new Expression("At(R,r,convR)", f,R,r,convR);
        return expression.calculate();
    }

    public static class Certificate implements Serializable {
        @Nonnull private String name = "";
        @Nonnull private String date = "23.03.2022";
        @Nonnull private String company = "";

        @Nonnull public String getName(){return this.name;}
        //Format = DD.MM.YYYY
        @Nonnull public String getDate(){return this.date;}
        @Nonnull public String getCompany(){return this.company;}

        public void setName(@Nonnull String name){this.name = name;}
        public void setDate(@Nonnull String date){this.date = date;}
        public void setCompany(@Nonnull String company){this.company = company;}

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
                    this.date.equals(c.getDate());
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

    /**
     *
     * This method has been rewritten to work with ArrayList.
     *
     * @return true if calibrators names is equal
     *
     * If you need to compare all fields of Calibrators use {@link #isMatch(Calibrator)}
     */
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

    /**
     *
     * @param calibrator to compare with this
     * @return true if calibrators fields equal
     */
    public boolean isMatch(Calibrator calibrator){
        return this.name.equals(calibrator.getName())
                && this.type.equals(calibrator.getType())
                && this.number.equals(calibrator.getNumber())
                && this.measurement.equals(calibrator.getMeasurement())
                && this.certificate.equals(calibrator.getCertificate())
                && this.rangeMin == calibrator.getRangeMin()
                && this.rangeMax == calibrator.getRangeMax()
                && this.value.equals(calibrator.getValue())
                && this.errorFormula.equals(calibrator.getErrorFormula());
    }
}


