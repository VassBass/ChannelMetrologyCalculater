package util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

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
}
