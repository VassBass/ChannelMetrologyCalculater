package util;

import java.util.Arrays;

public class DoubleHelper {
    public static boolean nonNaN(double ... doubles) {
        return Arrays.stream(doubles).noneMatch(Double::isNaN);
    }
}
