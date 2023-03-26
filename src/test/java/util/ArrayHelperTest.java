package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayHelperTest {

    @Test
    public void testIsArrayOfDoubles() {
        String[] validArray = new String[] { "0.0", "-56.5", "8" };
        String[] invalidArray1 = new String[] { "0.0", "abc", "8" };
        String[] invalidArray2 = new String[] { null, "-56.5", "8" };
        String[] invalidArray3 = new String[] { "", "-56.5", "8" };
        assertTrue(ArrayHelper.isArrayOfDoubles(validArray));
        assertFalse(ArrayHelper.isArrayOfDoubles(invalidArray1));
        assertFalse(ArrayHelper.isArrayOfDoubles(invalidArray2));
        assertFalse(ArrayHelper.isArrayOfDoubles(invalidArray3));

        String[][] validSquareArray = new String[][] {
                new String[] { "0.0", "-56.5", "8"},
                new String[] { "0.0", "8"},
                new String[] { "8"}
        };
        String[][] invalidSquareArray1 = new String[][] {
                new String[] { "0.0", "abs", "8"},
                new String[] { "0.0", "8"},
                new String[] { "8"}
        };
        String[][] invalidSquareArray2 = new String[][] {
                new String[] { "0.0", null, "8"},
                new String[] { "0.0", "8"},
                new String[] { "8"}
        };
        String[][] invalidSquareArray3 = new String[][] {
                new String[] { "0.0", "", "8"},
                new String[] { "0.0", "8"},
                new String[] { "8"}
        };
        String[][] invalidSquareArray4 = new String[][] {
                null,
                new String[] { "0.0", "8"},
                new String[] { "8"}
        };
        assertTrue(ArrayHelper.isArrayOfDoubles(validSquareArray));
        assertFalse(ArrayHelper.isArrayOfDoubles(invalidSquareArray1));
        assertFalse(ArrayHelper.isArrayOfDoubles(invalidSquareArray2));
        assertFalse(ArrayHelper.isArrayOfDoubles(invalidSquareArray3));
        assertFalse(ArrayHelper.isArrayOfDoubles(invalidSquareArray4));
    }
}