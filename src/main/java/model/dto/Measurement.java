package model.dto;

import java.util.Map;
import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;
import localization.Labels;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = measurements
 */
public class Measurement implements Serializable {
    private static final long serialVersionUID = 6L;

    private static final Map<String, String> LABELS = Labels.getRootLabels();
    /**
     * Measurement names
     * 
     * @see #name
     * @see Calibrator#getMeasurementName() {@link Calibrator#setMeasurementName(String)}
     * @see Sensor#getMeasurementName() {@link Sensor#setMeasurementName(String)}
     */
    public static final String TEMPERATURE= LABELS.get("temperature");
    public static final String PRESSURE = LABELS.get("pressure");
    public static final String CONSUMPTION = LABELS.get("consumption");

    /**
     * Measurement values
     *
     * @see #value
     * @see Calibrator#getMeasurementValue()  {@link Calibrator#setMeasurementValue(String)}
     * @see Sensor#getMeasurementValue()  {@link Sensor#setMeasurementValue(String)}
     */
    //Temperature
    public static final String DEGREE_CELSIUS = LABELS.get("degreeCelsius");
    //Pressure
    public static final String KPA = LABELS.get("kPa");
    public static final String PA = LABELS.get("pa");
    public static final String MM_ACVA = LABELS.get("mmAcva");
    public static final String KGS_SM2 = LABELS.get("kgsSm2");
    public static final String BAR = LABELS.get("bar");
    public static final String KGS_MM2 = LABELS.get("kgsMm2");
    public static final String ML_BAR = LABELS.get("mBar");
    //Consumption
    public static final String M3_HOUR = LABELS.get("m3H");
    public static final String M_S = LABELS.get("mS");
    public static final String CM_S = LABELS.get("smS");
    //Electric
    public static final String OM = LABELS.get("om");
    //Relative
    public static final String PERCENT = LABELS.get("percent");

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