package util;

import java.util.Arrays;
import java.util.Objects;

public class ObjectHelper {
    public static boolean nonNull(Object ... o) {
        return Arrays.stream(o).noneMatch(Objects::isNull);
    }
}
