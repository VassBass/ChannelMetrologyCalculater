package util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StringHelper {
    public static boolean isDouble(@Nonnull String doubleString) {
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
