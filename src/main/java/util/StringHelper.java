package util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import localization.Labels;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class StringHelper {
    private StringHelper(){}

    public static final int FOR_LAST_ZERO = Integer.MAX_VALUE;

    public static boolean isDouble(String ... doubleString) {
        if (Objects.isNull(doubleString)) return false;
        try {
            Arrays.stream(doubleString).forEach(Double::parseDouble);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInt(String intString) {
        if (Objects.isNull(intString)) return false;
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
            return BigDecimal.valueOf(value).setScale(places, java.math.RoundingMode.HALF_UP).toString();
        }
    }

    private static String roundingForLastZero(double value) {
        String d = String.valueOf(value);
        if (d.contains("e") || d.contains("E")) d = String.format(Locale.US, "%.50f", value);
        String[] splitted = d.split(RegexHelper.DOT_REGEX);
        if (splitted.length > 1) {
            String prefix = splitted[0];
            String suffix = splitted[1].replaceFirst("(?<=[1-9])0", Labels.DOT);
            int index = suffix.indexOf(Labels.dot);
            if (index >= 0) {
                suffix = suffix.substring(0, index);
                if (suffix.isEmpty()) {
                    return prefix;
                } else {
                    return String.format("%s.%s", prefix, suffix);
                }
            } else if (suffix.length() > 0) {
                if (Long.parseLong(suffix) == 0) {
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

    public static boolean containsEachOtherIgnoreCase(String s1, String s2) {
        String s1Lower = s1.toLowerCase(Locale.ROOT);
        String s2Lower = s2.toLowerCase(Locale.ROOT);
        String s1Upper = s1.toUpperCase(Locale.ROOT);
        String s2Upper = s2.toUpperCase(Locale.ROOT);
        return s1Lower.contains(s2Lower) || s2Lower.contains(s1Lower) || s1Upper.contains(s2Upper) || s2Upper.contains(s1Upper);
    }

    public static boolean containsIgnoreCaseAndSpaces(String base, String containValue) {
        String baseLow = base.toLowerCase(Locale.ROOT).replaceAll(RegexHelper.SPACE_REGEX, EMPTY);
        String baseUp = base.toUpperCase(Locale.ROOT).replaceAll(RegexHelper.SPACE_REGEX, EMPTY);
        String valLow = containValue.toLowerCase(Locale.ROOT).replaceAll(RegexHelper.SPACE_REGEX, EMPTY);
        String valUp = containValue.toUpperCase(Locale.ROOT).replaceAll(RegexHelper.SPACE_REGEX, EMPTY);
        return baseLow.contains(valLow) && baseUp.contains(valUp);
    }

    public static boolean isBlank(String s) {
        return s.isEmpty() || s.replaceAll(RegexHelper.SPACE_REGEX, EMPTY).isEmpty();
    }
}
