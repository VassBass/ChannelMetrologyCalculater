package util;

import exception.ValidationException;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class ValidationUtil {
    private static final String SPACE_REGEX = "\\s";
    private static final String DOT_REGEX = "\\.";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    //Formula constants
    private static final String NOT_NUMBERS_REGEX = "conv\\(.*?\\)|R|r|conv";
    private static final String DEGREE0_REGEX = "⁰";
    private static final String DEGREE1_REGEX = "¹";
    private static final String DEGREE2_REGEX = "²";
    private static final String DEGREE3_REGEX = "³";
    private static final String DEGREE4_REGEX = "⁴";
    private static final String DEGREE5_REGEX = "⁵";
    private static final String DEGREE6_REGEX = "⁶";
    private static final String DEGREE7_REGEX = "⁷";
    private static final String DEGREE8_REGEX = "⁸";
    private static final String DEGREE9_REGEX = "⁹";
    private static final String SQRT_REGEX = "√";
    private static final String UNNECESSARY_DEGREE_SYMBOLS_REGEX = "(?<=\\^(?-)\\d)\\^";
    //Formula constants

    public static void validateRequired(String logFieldName, String uiFieldName, String value) {
        if (value == null || value.isEmpty()) {
            throw ValidationException.A01(logFieldName, uiFieldName);
        }

        if (value.replaceAll(SPACE_REGEX, "").isEmpty()) {
            throw ValidationException.A01(logFieldName, uiFieldName);
        }
    }

    public static void validateDouble(String logFieldName, String uiFieldName, String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw ValidationException.A02(logFieldName, uiFieldName);
        }
    }

    @SuppressWarnings("MagicConstant")
    public static void validateDate(String logFieldName, String uiFieldName, String value) {
        String[] separateDate = value.split(DOT_REGEX);
        if (separateDate.length != 3) {
            throw ValidationException.A02(logFieldName, uiFieldName);
        }

        for (String val : separateDate) {
            validateInteger(logFieldName, uiFieldName, val);
        }

        int day = Integer.parseInt(separateDate[0]);
        int month = Integer.parseInt(separateDate[1]);
        int year = Integer.parseInt(separateDate[2]);

        if (year < 1800 || year > 3000) throw ValidationException.A02(logFieldName, uiFieldName);
        if (month < 1 || month > 12) throw ValidationException.A02(logFieldName, uiFieldName);

        Calendar calendar = new GregorianCalendar(year, month, 1);
        if (day < 1 || day > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) throw ValidationException.A02(logFieldName, uiFieldName);
    }

    public static void validateInteger(String logFieldName, String uiFieldName, String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw ValidationException.A02(logFieldName, uiFieldName);
        }
    }

    public static void validateBoolean(String logFieldName, String uiFieldName, String value) {
        if (value == null || value.isEmpty()) {
            throw ValidationException.A02(logFieldName, uiFieldName);
        }

        if (!TRUE.equalsIgnoreCase(value) && !FALSE.equalsIgnoreCase(value)) {
            throw ValidationException.A02(logFieldName, uiFieldName);
        }
    }

    public static void validateFormula(String logFieldName, String uiFieldName, String value) {
        String f = value.replaceAll(NOT_NUMBERS_REGEX, "1");
        Expression expression = new Expression(preparedFormula(f));
        if (Double.isNaN(expression.calculate())) {
            throw ValidationException.A02(logFieldName, uiFieldName);
        }
    }

    private static String preparedFormula(String f) {
        return f.replaceAll(",", ".")
                .replaceAll(DEGREE0_REGEX, "^0")
                .replaceAll(DEGREE1_REGEX, "^1")
                .replaceAll(DEGREE2_REGEX, "^2")
                .replaceAll(DEGREE3_REGEX, "^3")
                .replaceAll(DEGREE4_REGEX, "^4")
                .replaceAll(DEGREE5_REGEX, "^5")
                .replaceAll(DEGREE6_REGEX, "^6")
                .replaceAll(DEGREE7_REGEX, "^7")
                .replaceAll(DEGREE8_REGEX, "^8")
                .replaceAll(DEGREE9_REGEX, "^9")
                .replaceAll(UNNECESSARY_DEGREE_SYMBOLS_REGEX, "")
                .replaceAll(SQRT_REGEX, "sqrt");
    }
}
