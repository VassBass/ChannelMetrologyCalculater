package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringHelperTest {

    @Test
    public void testIsDouble() {
        String valid1 = "225";
        String valid2 = "2.25";
        String invalid1 = "2,25";
        String invalid2 = "invalid";

        assertTrue(StringHelper.isDouble(valid1));
        assertTrue(StringHelper.isDouble(valid2));
        assertFalse(StringHelper.isDouble(invalid1));
        assertFalse(StringHelper.isDouble(invalid2));
        assertFalse(StringHelper.isDouble(""));
    }

    @Test
    public void testIsInt() {
        String valid = "222";
        String invalid1 = "2.25";
        String invalid2 = "2,25";
        String invalid3 = "invalid";
        String invalid4 = "55555555555555555555555555555";

        assertTrue(StringHelper.isInt(valid));
        assertFalse(StringHelper.isInt(invalid1));
        assertFalse(StringHelper.isInt(invalid2));
        assertFalse(StringHelper.isInt(invalid3));
        assertFalse(StringHelper.isInt(invalid4));
        assertFalse(StringHelper.isInt(""));
    }

    @Test
    public void testParseInt() {
        String valid = "222";
        String invalid1 = "2.25";
        String invalid2 = "2,25";
        String invalid3 = "invalid";
        String invalid4 = "55555555555555555555555555555";

        int expected = 222;

        Integer actual = StringHelper.parseInt(valid);
        assertNotNull(actual);
        assertEquals(expected, actual.intValue());

        assertNull(StringHelper.parseInt(invalid1));
        assertNull(StringHelper.parseInt(invalid2));
        assertNull(StringHelper.parseInt(invalid3));
        assertNull(StringHelper.parseInt(invalid4));
    }
}