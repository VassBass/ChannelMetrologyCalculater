package model.dto.fields;

public class SensorField {
    public static final String TYPE = "type";
    public static final String CHANNEL_CODE = "channelCode";
    public static final String RANGE_MIN = "rangeMin";
    public static final String RANGE_MAX = "rangeMax";
    public static final String SERIAL_NUMBER = "serialNumber";
    public static final String MEASUREMENT_NAME = "measurementName";
    public static final String MEASUREMENT_VALUE = "measurementValue";
    public static final String ERROR_FORMULA = "errorFormula";

    public static class UI {
        public static final String TYPE = "Тип ПВП";
        public static final String CHANNEL_CODE = "Код ВК";
        public static final String RANGE_MIN = "Мінімальна величина діапазону ПВП";
        public static final String RANGE_MAX = "Максимальна величина діапазону ПВП";
        public static final String SERIAL_NUMBER = "Серійний номер ПВП";
        public static final String MEASUREMENT_NAME = "Назва вимірювання";
        public static final String MEASUREMENT_VALUE = "Величина вимірювання";
        public static final String ERROR_FORMULA = "Формула розрахунку похибки ПВП";
    }
}
