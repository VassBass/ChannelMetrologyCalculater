package util;

import java.util.Arrays;
import java.util.Objects;

public class ArrayHelper {
    private ArrayHelper(){}

    public static boolean isArrayOfDoubles(String[] array) {
        return Arrays.stream(array).allMatch(StringHelper::isDouble);
    }

    public static boolean isArrayOfDoubles(String[][] array) {
        for (String[] a : array) {
            if (Objects.isNull(a) ||
                    !Arrays.stream(a).allMatch(StringHelper::isDouble)) {
                return false;
            }
        }
        return true;
    }
}
