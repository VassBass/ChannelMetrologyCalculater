package util;

import java.util.Arrays;

public class BooleanHelper {
    private BooleanHelper(){}

    public static boolean isAllTrue(Boolean ... booleans) {
        return Arrays.stream(booleans).allMatch(b -> b);
    }
}
