package util;

import java.util.Arrays;
import java.util.Objects;

public class ObjectHelper {
    private ObjectHelper(){}

    public static boolean nonNull(Object ... o) {
        return Arrays.stream(o).noneMatch(Objects::isNull);
    }

    public static boolean anyNull(Object ... o) {
        return Arrays.stream(o).anyMatch(Objects::isNull);
    }
}
