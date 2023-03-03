package util;

public class StringHelper {
    public static boolean isDouble(String doubleString) {
        try {
            Double.parseDouble(doubleString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
