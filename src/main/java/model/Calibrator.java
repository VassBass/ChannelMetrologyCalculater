package model;

import application.Application;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import service.MeasurementService;
import service.impl.MeasurementServiceImpl;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

/**
 * DB table = calibrators
 */
public class Calibrator implements Serializable {
    /**
     * Default calibrators types
     * 
     * @see #type
     * @see #getType()
     * @see #setType(String)
     */
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
     * 
     * @see Certificate
     */
    @Nonnull private Certificate certificate = new Certificate();

    /**
     * DB field = number [TEXT]
     */
    @Nonnull private String number = "";

    /**
     * DB field = measurement [TEXT]
     *
     * @see Measurement
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
     *
     * @see Measurement
     */
    @Nonnull private String value = "";

    /**
     * DB field = error_formula [TEXT]
     * 
     * @see #getError(Channel)
     *
     * R - Measurement range of channel (Диапазон измерения канала)
     * @see Channel#_getRange()
     *
     * r - Measurement range of calibrator (Диапазон измерения калибратора)
     * @see Calibrator#_getRange()
     *
     * convR - Measurement range of calibrator converted by measurement channel value
     * (Диапазон измерения калибратора переконвертированый под измерительную величину канала)
     * @see Measurement#convertTo(String, double) 
     * @see Measurement#convertFrom(String, double)
     */
    @Nonnull private String errorFormula = "";

    public Calibrator(){}

    public Calibrator(@Nonnull String name){
        this.name = name;
    }

    @Nonnull public String getType() {return this.type;}
    @Nonnull public String getName(){return this.name;}
    @Nonnull public String getNumber(){return this.number;}
    @Nonnull public Certificate getCertificate(){return this.certificate;}
    public double getRangeMin(){return this.rangeMin;}
    public double getRangeMax(){return this.rangeMax;}
    public double _getRange(){return this.rangeMax - this.rangeMin;}
    @Nonnull public String getValue(){return this.value;}
    @Nonnull public String getMeasurement(){return this.measurement;}
    @Nonnull public String _getCertificateType(){return this.certificate.getType();}
    @Nonnull public String _getCertificateName(){return this.certificate.getName();}
    @Nonnull public String _getCertificateDate(){return this.certificate.getDate();}
    @Nonnull public String _getCertificateCompany(){return this.certificate.getCompany();}
    @Nonnull public String getErrorFormula(){return this.errorFormula;}

    public void setType(@Nonnull String type) {this.type = type;}
    public void setName(@Nonnull String name){this.name = name;}
    public void setNumber(@Nonnull String number){this.number = number;}
    public void setRangeMin(double rangeMin){this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax){this.rangeMax = rangeMax;}
    public void setValue(@Nonnull String value){this.value = value;}
    public void _setCertificateType(@Nonnull String type){this.certificate.setType(type);}
    public void _setCertificateName(@Nonnull String name){this.certificate.setName(name);}
    public void _setCertificateDate(@Nonnull String date){this.certificate.setDate(date);}
    public void _setCertificateCompany(@Nonnull String company){this.certificate.setCompany(company);}
    public void setErrorFormula(@Nonnull String errorFormula){this.errorFormula = errorFormula;}
    public void setMeasurement(@Nonnull String measurement){this.measurement = measurement;}
    public void setCertificate(@Nonnull Certificate certificate){this.certificate = certificate;}

    /**
     * @return main Certificate info in single string
     *
     * @see Certificate#getName()
     * @see Certificate#getDate()
     * @see Certificate#getCompany()
     */
    public String getCertificateInfo(){
        return this.certificate.getName() + " від " + this.certificate.getDate() + "р " + this.certificate.getCompany();
    }

    /**
     * @param channel against which the calculation is made
     * @return numerical value calculated by the {@link #errorFormula}
     *
     * R - Measurement range of channel (Диапазон измерения канала)
     * @see Channel#_getRange()
     *
     * r - Measurement range of calibrator (Диапазон измерения калибратора)
     * @see Calibrator#_getRange()
     * 
     * convR - Measurement range of calibrator converted by measurement channel value
     * (Диапазон измерения калибратора переконвертированый под измерительную величину канала)
     * @see Measurement#convertTo(String, double)
     * @see Measurement#convertFrom(String, double)
     *
     * conv(...) - number converted by measurement channel value
     * (Число переконвертированное под измерительную величину канала)
     * @see Measurement#getErrorStringAfterConvertNumbers(String, Measurement, Measurement)
     */
    public double getError(@Nonnull Channel channel){
        String formula = VariableConverter.commasToDots(this.errorFormula);
        Measurement input = MeasurementServiceImpl.getInstance().get(this.value);
        formula = Measurement.getErrorStringAfterConvertNumbers(formula, input, channel._getMeasurement());
        Function f = new Function("At(R,r,convR) = " + formula);
        Argument R = new Argument("R = " + channel._getRange());
        double cR = channel._getMeasurement().convertFrom(this.value, this._getRange());
        Argument r = new Argument("r = " + this._getRange());
        Argument convR = new Argument("convR = " + cR);
        Expression expression = new Expression("At(R,r,convR)", f,R,r,convR);
        return expression.calculate();
    }

    public static class Certificate implements Serializable {
        public static final String defaultType = "Сертифікат калібрування";

        @Nonnull private String name = "";
        @Nonnull private String date = "23.03.2022";
        @Nonnull private String company = "";
        @Nonnull private String type = defaultType;

        @Nonnull public String getName(){return this.name;}

        /**
         * @return date string in format = DD.MM.YYYY
         *
         * @see VariableConverter#dateToString(Calendar)
         * @see VariableConverter#stringToDate(String)
         * @see VariableConverter#stringToDateString(String)
         */
        @Nonnull public String getDate(){return this.date;}
        @Nonnull public String getCompany(){return this.company;}
        @Nonnull public String getType(){return this.type;}

        public void setName(@Nonnull String name){this.name = name;}
        public void setDate(@Nonnull String date){this.date = date;}
        public void setCompany(@Nonnull String company){this.company = company;}
        public void setType(@Nonnull String type){this.type = type;}

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.date, this.company, this.type);
        }

        @Override
        public boolean equals(Object object){
            if (!object.getClass().equals(this.getClass())){
                return false;
            }
            Certificate c = (Certificate) object;
            return this.name.equals(c.name)
                    && this.company.equals(c.company)
                    && this.date.equals(c.getDate())
                    && this.type.equals(c.getType());
        }

        /**
         * @return {@link Certificate} in JsonString
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
         * @param json {@link Certificate} in JsonString
         * @see com.fasterxml.jackson.core
         *
         * @return {@link Certificate}
         * @throws JsonProcessingException - if jackson can't transform
         */
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

    /**
     * @return {@link Calibrator} in JsonString
     * if 10 times in a row throws {@link JsonProcessingException} return null
     *
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
     * @param json {@link Calibrator} in JsonString
     *
     * @see com.fasterxml.jackson.core
     *
     * @return {@link Calibrator}
     *
     * @throws JsonProcessingException - if jackson can't transform String to Calibrator
     */
    public static Calibrator fromString(String json) throws JsonProcessingException{
            return new ObjectMapper().readValue(json, Calibrator.class);
    }

    /**
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


