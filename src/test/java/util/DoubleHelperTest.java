package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class DoubleHelperTest {

    @Test
    public void testNonNaN() {
        assertFalse(DoubleHelper.nonNaN(Double.NaN));
        assertFalse(DoubleHelper.nonNaN(Double.NaN, 5.36, 58));
        assertTrue(DoubleHelper.nonNaN(5.36));
        assertTrue(DoubleHelper.nonNaN(5.36, 58));
    }
}