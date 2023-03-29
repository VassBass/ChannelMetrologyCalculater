package util;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class StringHelper {
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
        return new BigDecimal(value).setScale(places, java.math.RoundingMode.HALF_UP).toString();
    }
}
