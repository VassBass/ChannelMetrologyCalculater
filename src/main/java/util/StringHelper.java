package util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class StringHelper {
    public static final int FOR_LAST_ZERO = Integer.MAX_VALUE;

    public static boolean isDouble(String doubleString) {
        if (Objects.isNull(doubleString)) return false;
        try {
            Double.parseDouble(doubleString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInt(@Nonnull String intString) {
        try {
            Integer.parseInt(intString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Nullable
    public static Integer parseInt(@Nonnull String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String roundingDouble(double value, @Nonnegative int places) {
        if (places == FOR_LAST_ZERO) {
            return roundingForLastZero(value);
        } else {
            return new BigDecimal(value).setScale(places, java.math.RoundingMode.HALF_UP).toString();
        }
    }

    private static String roundingForLastZero(double value) {
        String d = String.valueOf(value);
        if (d.contains("e") || d.contains("E")) d = String.format(Locale.US, "%.50f", value);
        String[] splitted = d.split("\\.");
        if (splitted.length > 1) {
            String prefix = splitted[0];
            String suffix = splitted[1].replaceFirst("(?<=[1-9])0", ".");
            int index = suffix.indexOf('.');
            if (index >= 0) {
                suffix = suffix.substring(0, index);
                if (suffix.isEmpty()) {
                    return prefix;
                } else {
                    return String.format("%s.%s", prefix, suffix);
                }
            } else if (suffix.length() > 0) {
                if (Integer.parseInt(suffix) == 0) {
                    return prefix;
                } else {
                    return String.format("%s.%s", prefix, suffix);
                }
            }
        } else if (splitted.length == 1) return splitted[0];
        return EMPTY;
    }

    public static boolean nonEmpty(String ... strings) {
        return Arrays.stream(strings).noneMatch(Objects::isNull) &&
                Arrays.stream(strings).noneMatch(String::isEmpty);
    }
}
