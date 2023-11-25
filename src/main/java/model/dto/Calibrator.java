package model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import localization.Labels;
import util.DateHelper;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = calibrators
 */
public class Calibrator implements Serializable {
    private static final long serialVersionUID = 6L;

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
    @Nonnull private String type = EMPTY;

    /**
     * DB field = name (primary key) [TEXT]
     */
    @Nonnull private String name = EMPTY;

    /**
     * DB field = certificate [TEXT{Json}]
     * 
     * @see Certificate
     */
    @Nonnull private Certificate certificate = new Certificate();

    /**
     * DB field = number [TEXT]
     */
    @Nonnull private String number = EMPTY;

    /**
     * DB field = measurement_name [TEXT]
     *
     * @see Measurement
     */
    @Nonnull private String measurementName = EMPTY;

    /**
     * DB field = range_min [REAL]
     */
    private double rangeMin = 0D;

    /**
     * DB field = range_max [REAL]
     */
    private double rangeMax = 0D;

    /**
     * DB field = measurement_value [TEXT]
     *
     * @see Measurement
     */
    @Nonnull private String measurementValue = EMPTY;

    /**
     * DB field = error_formula [TEXT]
     * R - Measurement range of channel (Діапазон вимірювання каналу)
     * @see Channel#calculateRange()
     *
     * r - Measurement range of calibrator (Діапазон вимірювання калібратора)
     * @see Calibrator#calculateRange()
     *
     * convR - Measurement range of calibrator converted by measurement channel value
     * (Діапазон вимірювання калібратора переконвертований під вимірювальну величину каналу)
     */
    @Nonnull private String errorFormula = EMPTY;

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
    @Nonnull public String getMeasurementValue(){return this.measurementValue;}
    @Nonnull public String getMeasurementName(){return this.measurementName;}
    @Nonnull public String getErrorFormula(){return this.errorFormula;}

    public void setType(@Nonnull String type) {this.type = type;}
    public void setName(@Nonnull String name){this.name = name;}
    public void setNumber(@Nonnull String number){this.number = number;}
    public void setRangeMin(double rangeMin){this.rangeMin = rangeMin;}
    public void setRangeMax(double rangeMax){this.rangeMax = rangeMax;}
    public void setMeasurementValue(@Nonnull String value){this.measurementValue = value;}
    public void setErrorFormula(@Nonnull String errorFormula){this.errorFormula = errorFormula;}
    public void setMeasurementName(@Nonnull String name){this.measurementName = name;}
    public void setCertificate(@Nonnull Certificate certificate){this.certificate = certificate;}

    public double calculateRange(){return this.rangeMax - this.rangeMin;}

    public static class Certificate implements Serializable {
        private static final long serialVersionUID = 6L;

        private static final String DEFAULT_TYPE = Labels.getLabels(Calibrator.class).get("certificate");
        private static final String DEFAULT_DATE = "23.03.2022";

        @Nonnull private String name = EMPTY;
        @Nonnull private String date = DEFAULT_DATE;
        @Nonnull private String company = EMPTY;
        @Nonnull private String type = DEFAULT_TYPE;

        @Nonnull public String getName(){return this.name;}

        /**
         * @return date string in format = DD.MM.YYYY
         *
         * @see DateHelper#dateToString(Calendar)
         * @see DateHelper#stringToDate(String)
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
            if (object == null || object.getClass() != this.getClass()) return false;
            if (this == object) return true;

            Certificate c = (Certificate) object;
            return this.name.equals(c.getName())
                    && this.company.equals(c.getCompany())
                    && this.date.equals(c.getDate())
                    && this.type.equals(c.getType());
        }

        @Override
        public String toString() {
            return String.format("%s %s %sр %s", name, Labels.getRootLabels().get("from"), date, company);
        }

        public static class CertificateBuilder {
            private final Certificate certificate;

            public CertificateBuilder() {
                certificate = new Certificate();
            }

            public CertificateBuilder setName(String name) {
                certificate.setName(name == null ? EMPTY : name);
                return this;
            }

            public CertificateBuilder setDate(String date) {
                certificate.setDate(DateHelper.isDateValid(date) ? date : DEFAULT_DATE);
                return this;
            }

            public CertificateBuilder setCompany(String company) {
                certificate.setCompany(company == null ? EMPTY : company);
                return this;
            }

            public CertificateBuilder setType(String type) {
                certificate.setType(type == null || type.isEmpty() ? DEFAULT_TYPE : type);
                return this;
            }

            public Certificate build() {
                return certificate;
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.name, this.number);
    }

    /**
     *
     * This method has been rewritten to work with ArrayList.
     *
     * @return true if calibrators names is equal
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
        return String.format("[%s]%s(%s)", number, type, name);
    }
}


