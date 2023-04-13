package util;

import org.junit.Test;

import static org.junit.Assert.*;
import static util.StringHelper.FOR_LAST_ZERO;

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

        assertTrue(StringHelper.isDouble(valid1, valid2));
        assertFalse(StringHelper.isDouble(valid1, invalid1));
        assertFalse(StringHelper.isDouble(invalid1, invalid2));
        assertFalse(StringHelper.isDouble(valid1, ""));
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

    @Test
    public void testRoundingDouble() {
        double d1 = 0.123456789;
        double d2 = 0.987654321;
        double d3 = 0.000999;
        double d4 = 0.000111;
        double d5 = 1.05;
        double d6 = 6D;

        assertEquals("0.123", StringHelper.roundingDouble(d1, 3));
        assertEquals("0.988", StringHelper.roundingDouble(d2, 3));
        assertEquals("0.001", StringHelper.roundingDouble(d3, 3));
        assertEquals("0.000", StringHelper.roundingDouble(d4, 3));
        assertEquals("1.050", StringHelper.roundingDouble(d5, 3));
        assertEquals("6.000", StringHelper.roundingDouble(d6, 3));
        assertEquals("1", StringHelper.roundingDouble(d5, 0));
    }

    @Test
    public void testRoundingDoubleForLastZero() {
        double d1 = 0.123456089;
        double d2 = 0.987654320;
        double d3 = 0.000909;
        double d4 = 0.000110;
        double d5 = 1.05;
        double d6 = 6.00;

        assertEquals("0.123456", StringHelper.roundingDouble(d1, FOR_LAST_ZERO));
        assertEquals("0.98765432", StringHelper.roundingDouble(d2, FOR_LAST_ZERO));
        assertEquals("0.0009", StringHelper.roundingDouble(d3, FOR_LAST_ZERO));
        assertEquals("0.00011", StringHelper.roundingDouble(d4, FOR_LAST_ZERO));
        assertEquals("1.05", StringHelper.roundingDouble(d5, FOR_LAST_ZERO));
        assertEquals("6", StringHelper.roundingDouble(d6, FOR_LAST_ZERO));
    }

    @Test
    public void testNonEmpty() {
        assertTrue(StringHelper.nonEmpty("ihygfjyadgsf", "lkhfkasfe", "65432"));
        assertTrue(StringHelper.nonEmpty("ihygfjyadgsf"));
        assertFalse(StringHelper.nonEmpty(""));
        assertFalse(StringHelper.nonEmpty("ihygfjyadgsf", "", "65432"));
        assertFalse(StringHelper.nonEmpty("ihygfjyadgsf", null, "65432"));
    }

    @Test
    public void testContainsEachOtherIgnoreCase() {
        String s1 = "AbCdEfG";
        String s1reverse = "aBcDeFg";
        String s1contains = "AbCdEfGhIjK";
        String s1reverseContains = "aBcDeFgHiJk";
        String s2 = "LmNoP";

        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1, s1));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1, s1reverse));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1reverse, s1));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1, s1contains));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1contains, s1));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1, s1reverseContains));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1reverseContains, s1));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1reverse, s1reverseContains));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1reverseContains, s1reverse));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1reverse, s1contains));
        assertTrue(StringHelper.containsEachOtherIgnoreCase(s1contains, s1reverse));

        assertFalse(StringHelper.containsEachOtherIgnoreCase(s1, s2));
        assertFalse(StringHelper.containsEachOtherIgnoreCase(s2, s1));
    }
}