package model.dto;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DB table = measurements
 */
public class Measurement implements Serializable {
    /**
     * Measurement names
     * 
     * @see #name
     * @see Calibrator#getMeasurement() {@link Calibrator#setMeasurement(String)}
     * @see Sensor#getMeasurementName() {@link Sensor#setMeasurementName(String)}
     */
    public static String TEMPERATURE= "Температура";
    public static String PRESSURE = "Тиск";
    public static String CONSUMPTION = "Витрата";

    /**
     * Measurement values
     *
     * @see #value
     * @see Calibrator#getValue() {@link Calibrator#setValue(String)}
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
//            if (errorString != null && input != null && output != null) {
//                char[] chars = errorString.toCharArray();
//                int index;
//
//                for (int i = 0; i < chars.length; ) {
//                    if (chars[i] == 'c') {
//                        index = i;
//
//                        if (chars[++i] == 'o' && chars[++i] == 'n' && chars[++i] == 'v' && chars[++i] == '(') {
//                            char c = chars[++i];
//                            StringBuilder b = new StringBuilder();
//
//                            while (c != ')') {
//                                b.append(c);
//                                c = chars[++i];
//                            }
//                            double d = Double.parseDouble(b.toString());
//                            double converted = input.convertTo(output.getValue(), d);
//
//                            String f = errorString.substring(0, index);
//                            return getErrorStringAfterConvertNumbers(f + converted + errorString.substring(++i), input, output);
//                        }
//
//                    } else ++i;
//                }
//            }
//
//            return errorString;
        return EMPTY;
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
        return this.name.equalsIgnoreCase(measurement.getName()) &&
                this.value.equals(measurement.getValue());
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, value);
    }
}