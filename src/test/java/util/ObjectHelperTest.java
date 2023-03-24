package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectHelperTest {

    @Test
    public void testNonNull() {
        assertTrue(ObjectHelper.nonNull("null"));
        assertTrue(ObjectHelper.nonNull("non null", "null", ""));
        assertFalse(ObjectHelper.nonNull(null, "null"));
    }

    @Test
    public void testAnyNull() {
        assertFalse(ObjectHelper.anyNull("null"));
        assertFalse(ObjectHelper.anyNull("non null", "null", ""));
        assertTrue(ObjectHelper.anyNull(null, "null"));
    }
}