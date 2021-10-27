package constants;


public enum MeasurementConstants {
    TEMPERATURE("Температура"),
    PRESSURE("Тиск"),

    DEGREE_CELSIUS("\u2103"),
    KPA("кПа"),
    PA("Па"),
    MM_ACVA("мм вод ст"),
    KG_SM2("кг/см" + Strings.SQUARE),
    BAR("бар"),
    KG_MM2("кг/мм" + Strings.SQUARE),
    ML_BAR("мбар"),

    OM("Ом"),
    MV("мВ");

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
