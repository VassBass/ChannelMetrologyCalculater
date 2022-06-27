package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * DB table = measurements
 */
public class Measurement implements Serializable {
    /**
     * Measurement names
     * 
     * @see #name
     * @see Calibrator#getMeasurement() {@link Calibrator#setMeasurement(String)}
     * @see Sensor#getMeasurement() {@link Sensor#setMeasurement(String)}
     */
    public static String TEMPERATURE= "Температура";
    public static String PRESSURE = "Тиск";
    public static String CONSUMPTION = "Витрата";

    /**
     * Measurement values
     *
     * @see #value
     * @see Calibrator#getValue() {@link Calibrator#setValue(String)}
     * @see Sensor#getValue() {@link Sensor#setValue(String)}
     */
    public static String DEGREE_CELSIUS = "\u2103";

    public static String KPA = "кПа";
    public static String PA = "Па";
    public static String MM_ACVA = "мм вод ст";
    public static String KGS_SM2 = "кгс/см" + "\u00B2";
    public static String BAR = "бар";
    public static String KGS_MM2 = "кгc/мм" + "\u00B2";
    public static String ML_BAR = "мбар";

    public static String M3_HOUR = "м" + "\u00B3" + "/h";
    public static String M_S = "м/с";
    public static String CM_S = "см/с";

    /**
     * DB field = name [TEXT]
     */
    @Nonnull protected String name = "measurement";

    /**
     * DB field = value (primary key) [TEXT]
     */
    @Nonnull protected String value = "value";

    /**
     * DB field = factors [TEXT{Json}]
     * values for converting
     * Use for keys Measurement constants like:
     * @see #DEGREE_CELSIUS
     * @see #M3_HOUR
     * @see #KPA
     * and other
     */
    @Nonnull protected HashMap<String, Double>factors = new HashMap<>();

    public Measurement(){}

    public Measurement(@Nonnull String name, @Nonnull String value){
            this.name = name;
            this.value = value;
    }

    @Nonnull public String getName() {return this.name;}
    @Nonnull public String getValue() {return this.value;}
    @Nonnull public HashMap<String,Double>getFactors(){return this.factors;}
    @Nonnull public String _getFactorsJson() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(this.factors);
    }
    public Double _getFactor(String value){return this.factors.get(value);}

    public void setName(@Nonnull String name) {this.name = name;}
    public void setValue(@Nonnull String value){this.value = value;}
    public void setFactors(@Nonnull HashMap<String, Double>factors){this.factors = factors;}

    public void addFactor(@Nonnull String measurementValue, @Nonnull Double factor){
        if (!measurementValue.equals(this.value)) this.factors.put(measurementValue, factor);
    }

    public void removeFactor(@Nonnull String measurementValue){
        this.factors.remove(measurementValue);
    }

    /**
     * Convert from current measurement value to given
     * @param measurementValue the value to which is converted
     * @param quantity of measurement value
     * @return transformed value
     */
    public Double convertTo(@Nonnull String measurementValue, double quantity){
        double factor = measurementValue.equals(this.value) ? 1D : this.factors.get(measurementValue);
        return quantity * factor;
    }

    /**
     * Convert from given measurement value to current
     * @param measurementValue the value from which is converted
     * @param quantity of measurement value
     * @return transformed value
     */
    public Double convertFrom(@Nonnull String measurementValue, double quantity){
        if (measurementValue.equals("-") || this.value.equals("-") || measurementValue.equals(this.value)){
            return quantity;
        }else {
            double factor = this.factors.get(measurementValue);
            return quantity / factor;
        }
    }

    /**
     * Convert all numbers in brackets after "conv" chars from input measurementValue to output measurementValue
     * @param errorString to convert
     * @param input value to be converted
     * @param output value to which the conversion takes place
     * @return converted String
     * example:
     * {@code
     *      String errorString = "conv(50) * 10 / convR + conv(10)";
     *      String afterConv = Measurement.getErrorStringAfterConvertNumbers(errorString, Measurement.kPa, Measurement.pa);
     *      result: afterConv == "50000.0 * 10 / convR + 10000";
     *  }
     */
    public static String getErrorStringAfterConvertNumbers(String errorString, Measurement input, Measurement output){
            if (errorString != null && input != null && output != null) {
                char[] chars = errorString.toCharArray();
                int index;

                for (int i = 0; i < chars.length; ) {
                    if (chars[i] == 'c') {
                        index = i;

                        if (chars[++i] == 'o' && chars[++i] == 'n' && chars[++i] == 'v' && chars[++i] == '(') {
                            char c = chars[++i];
                            StringBuilder b = new StringBuilder();

                            while (c != ')') {
                                b.append(c);
                                c = chars[++i];
                            }
                            double d = Double.parseDouble(b.toString());
                            double converted = input.convertTo(output.getValue(), d);

                            String f = errorString.substring(0, index);
                            return getErrorStringAfterConvertNumbers(f + converted + errorString.substring(++i), input, output);
                        }

                    } else ++i;
                }
            }

            return errorString;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) return false;
        if (obj == this) return true;
        Measurement measurement = (Measurement) obj;
        return this.value.equals(measurement.getValue());
    }

    public Measurement copy(){
        Measurement m = new Measurement(this.name, this.value);
        m.setFactors(this.factors);
        return m;
    }

    /**
     * @return {@link Measurement} in JsonString
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
     * @param json {@link Measurement} in JsonString
     *
     * @see com.fasterxml.jackson.core
     *
     * @return {@link Measurement}
     *
     * @throws JsonProcessingException - if jackson can't transform String to Measurement
     */
    public static Measurement fromString(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, Measurement.class);
    }
}