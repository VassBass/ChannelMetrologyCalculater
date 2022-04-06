package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.Serializable;
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

    public Measurement(){}

    public Measurement(@Nonnull String name, @Nonnull String value){
            this.name = name;
            this.value = value;
    }

    @Nonnull public String getName() {return this.name;}
    @Nonnull public String getValue() {return this.value;}

    public void setName(@Nonnull String name) {this.name = name;}
    public void setValue(@Nonnull String value){this.value = value;}

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