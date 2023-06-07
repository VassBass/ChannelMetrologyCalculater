package model.dto;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = measurements
 */
public class Measurement implements Serializable {
    public static final long serialVersionUID = 6L;

    /**
     * Measurement names
     * 
     * @see #name
     * @see Calibrator#getMeasurementName() {@link Calibrator#setMeasurementName(String)}
     * @see Sensor#getMeasurementName() {@link Sensor#setMeasurementName(String)}
     */
    public static String TEMPERATURE= "Температура";
    public static String PRESSURE = "Тиск";
    public static String CONSUMPTION = "Витрата";

    /**
     * Measurement values
     *
     * @see #value
     * @see Calibrator#getMeasurementValue()  {@link Calibrator#setMeasurementValue(String)}
     * @see Sensor#getMeasurementValue()  {@link Sensor#setMeasurementValue(String)}
     */
    //Temperature
    public static String DEGREE_CELSIUS = "\u2103";
    //Pressure
    public static String KPA = "кПа";
    public static String PA = "Па";
    public static String MM_ACVA = "мм вод ст";
    public static String KGS_SM2 = "кгс/см" + "\u00B2";
    public static String BAR = "бар";
    public static String KGS_MM2 = "кгc/мм" + "\u00B2";
    public static String ML_BAR = "мбар";
    //Consumption
    public static String M3_HOUR = "м" + "\u00B3" + "/h";
    public static String M_S = "м/с";
    public static String CM_S = "см/с";
    //Electric
    public static String Om = "Ом";

    /**
     * DB field = name [TEXT]
     */
    @Nonnull protected String name = EMPTY;

    /**
     * DB field = value (primary key) [TEXT]
     */
    @Nonnull protected String value = EMPTY;

    public Measurement(){}

    public Measurement(@Nonnull String value){
        this.value = value;
    }

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

    @Override
    public String toString() {
        return String.format("%s(%s)", name, value);
    }
}