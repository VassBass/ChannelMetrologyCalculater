package constants;


public enum MeasurementConstants {

    TEMPERATURE("Температура"),
    PRESSURE("Тиск"),
    CONSUMPTION("Витрата"),

    DEGREE_CELSIUS("\u2103"),

    KPA("кПа"),
    PA("Па"),
    MM_ACVA("мм вод ст"),
    KG_SM2("кг/см" + "\u00B2"),
    BAR("бар"),
    KG_MM2("кг/мм" + "\u00B2"),
    ML_BAR("мбар"),

    M3_HOUR("м" + "\u00B3" + "/h"),

    M_S("м/с"),
    CM_S("см/с");

    private final String value;
    MeasurementConstants(String value){this.value = value;}
    public String getValue(){return this.value;}

    public static MeasurementConstants getConstantFromString(String value){
        for (MeasurementConstants constant : MeasurementConstants.values()){
            if (value.equals(constant.getValue())){
                return constant;
            }
        }
        return null;
    }
}
