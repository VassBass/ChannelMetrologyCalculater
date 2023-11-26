package util;

import java.util.Arrays;

public class DoubleHelper {
    private DoubleHelper(){}

    public static boolean nonNaN(double ... doubles) {
        return Arrays.stream(doubles).noneMatch(Double::isNaN);
    }

    public static boolean anyNaN(double ... doubles) {
        return Arrays.stream(doubles).anyMatch(Double::isNaN);
    }
}
