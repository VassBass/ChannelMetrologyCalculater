package util;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class DateHelperTest {

    @Test
    public void testIsDateValid() {
        String validDate1 = "23.02.2022";
        String validDate2 = "2.02.2022";
        String validDate3 = "23.2.2022";

        String invalidDate1 = "52.02.2022";
        String invalidDate2 = "23.00.2022";
        String invalidDate3 = "23.02.22";
        String invalidDate4 = "23.00.2022";
        String invalidDate5 = "00.02.2022";
        String invalidDate6 = "23.02.0000";
        String invalidDate7 = "23.02.220";
        String invalidDate8 = "23.02.2";
        String invalidDate9 = "23.2.";
        String invalidDate10 = "23.2";
        String invalidDate11 = "23.";
        String invalidDate12 = "23";
        String invalidDate13 = "23..2022";
        String invalidDate14 = ".02.2022";
        String invalidDate15 = "..2022";
        String invalidDate16 = "...";
        String invalidDate17 = "not date";
        String invalidDate18 = "";

        assertTrue(DateHelper.isDateValid(validDate1));
        assertTrue(DateHelper.isDateValid(validDate2));
        assertTrue(DateHelper.isDateValid(validDate3));

        assertFalse(DateHelper.isDateValid(invalidDate1));
        assertFalse(DateHelper.isDateValid(invalidDate2));
        assertFalse(DateHelper.isDateValid(invalidDate3));
        assertFalse(DateHelper.isDateValid(invalidDate4));
        assertFalse(DateHelper.isDateValid(invalidDate5));
        assertFalse(DateHelper.isDateValid(invalidDate6));
        assertFalse(DateHelper.isDateValid(invalidDate7));
        assertFalse(DateHelper.isDateValid(invalidDate8));
        assertFalse(DateHelper.isDateValid(invalidDate9));
        assertFalse(DateHelper.isDateValid(invalidDate10));
        assertFalse(DateHelper.isDateValid(invalidDate11));
        assertFalse(DateHelper.isDateValid(invalidDate12));
        assertFalse(DateHelper.isDateValid(invalidDate13));
        assertFalse(DateHelper.isDateValid(invalidDate14));
        assertFalse(DateHelper.isDateValid(invalidDate15));
        assertFalse(DateHelper.isDateValid(invalidDate16));
        assertFalse(DateHelper.isDateValid(invalidDate17));
        assertFalse(DateHelper.isDateValid(invalidDate18));
        assertFalse(DateHelper.isDateValid(null));
    }

    @Test
    public void testYearsToMills() {
        long expected1 = 31_536_000_000L;
        long expected2_5 = 78_840_000_000L;

        assertEquals(expected1, DateHelper.yearsToMills(1));
        assertEquals(expected2_5, DateHelper.yearsToMills(2.5));
    }

    @Test
    public void testStringToDate() {
        String validDate1 = "23.02.2022";
        String validDate2 = "2.02.2022";
        String validDate3 = "23.2.2022";

        String invalidDate1 = "52.02.2022";
        String invalidDate2 = "23.00.2022";
        String invalidDate3 = "23.02.22";
        String invalidDate4 = "23.00.2022";
        String invalidDate5 = "00.02.2022";
        String invalidDate6 = "23.02.0000";
        String invalidDate7 = "23.02.220";
        String invalidDate8 = "23.02.2";
        String invalidDate9 = "23.2.";
        String invalidDate10 = "23.2";
        String invalidDate11 = "23.";
        String invalidDate12 = "23";
        String invalidDate13 = "23..2022";
        String invalidDate14 = ".02.2022";
        String invalidDate15 = "..2022";
        String invalidDate16 = "...";
        String invalidDate17 = "not date";
        String invalidDate18 = "";

        assertEquals(new GregorianCalendar(2022, Calendar.FEBRUARY, 23),
                DateHelper.stringToDate(validDate1));
        assertEquals(new GregorianCalendar(2022, Calendar.FEBRUARY, 2),
                DateHelper.stringToDate(validDate2));
        assertEquals(new GregorianCalendar(2022, Calendar.FEBRUARY, 23),
                DateHelper.stringToDate(validDate3));

        assertNull(DateHelper.stringToDate(invalidDate1));
        assertNull(DateHelper.stringToDate(invalidDate2));
        assertNull(DateHelper.stringToDate(invalidDate3));
        assertNull(DateHelper.stringToDate(invalidDate4));
        assertNull(DateHelper.stringToDate(invalidDate5));
        assertNull(DateHelper.stringToDate(invalidDate6));
        assertNull(DateHelper.stringToDate(invalidDate7));
        assertNull(DateHelper.stringToDate(invalidDate8));
        assertNull(DateHelper.stringToDate(invalidDate9));
        assertNull(DateHelper.stringToDate(invalidDate10));
        assertNull(DateHelper.stringToDate(invalidDate11));
        assertNull(DateHelper.stringToDate(invalidDate12));
        assertNull(DateHelper.stringToDate(invalidDate13));
        assertNull(DateHelper.stringToDate(invalidDate14));
        assertNull(DateHelper.stringToDate(invalidDate15));
        assertNull(DateHelper.stringToDate(invalidDate16));
        assertNull(DateHelper.stringToDate(invalidDate17));
        assertNull(DateHelper.stringToDate(invalidDate18));
    }

    @Test
    public void testDateToString() {
        Calendar calendar = new GregorianCalendar(2022, Calendar.FEBRUARY, 23);
        String expected = "23.02.2022";

        assertEquals(expected, DateHelper.dateToString(calendar));
        assertNotNull(DateHelper.dateToString(null));
    }

    @Test
    public void testGetDayFromDateString() {
        String validDate1 = "23.02.2022";
        String validDate2 = "2.02.2022";
        String invalidDate1 = "68.02.2022";
        String invalidDate2 = "23.22.2022";
        String invalidDate3 = "23.02.0";

        assertEquals("23", DateHelper.getDayFromDateString(validDate1));
        assertEquals("02", DateHelper.getDayFromDateString(validDate2));
        assertNotNull(DateHelper.getDayFromDateString(invalidDate1));
        assertNotNull(DateHelper.getDayFromDateString(invalidDate2));
        assertNotNull(DateHelper.getDayFromDateString(invalidDate3));
        assertNotNull(DateHelper.getDayFromDateString(null));
    }

    @Test
    public void testGetMonthFromDateString() {
        String validDate1 = "23.02.2022";
        String validDate2 = "23.2.2022";
        String invalidDate1 = "68.02.2022";
        String invalidDate2 = "23.22.2022";
        String invalidDate3 = "23.02.0";

        assertEquals("02", DateHelper.getMonthFromDateString(validDate1));
        assertEquals("02", DateHelper.getMonthFromDateString(validDate2));
        assertNotNull(DateHelper.getMonthFromDateString(invalidDate1));
        assertNotNull(DateHelper.getMonthFromDateString(invalidDate2));
        assertNotNull(DateHelper.getMonthFromDateString(invalidDate3));
        assertNotNull(DateHelper.getMonthFromDateString(null));
    }

    @Test
    public void testGetYearFromDateString() {
        String validDate = "23.02.2022";
        String invalidDate1 = "68.02.2022";
        String invalidDate2 = "23.22.2022";
        String invalidDate3 = "23.02.22";

        assertEquals("2022", DateHelper.getYearFromDateString(validDate));
        assertNotNull(DateHelper.getYearFromDateString(invalidDate1));
        assertNotNull(DateHelper.getYearFromDateString(invalidDate2));
        assertNotNull(DateHelper.getYearFromDateString(invalidDate3));
        assertNotNull(DateHelper.getYearFromDateString(null));
    }

    @Test
    public void testGetSplittedDate() {
        String validDate1 = "2.02.2022";
        String validDate2 = "23.2.2022";
        String validDate3 = "2.2.2022";
        String invalidDate1 = "68.02.2022";
        String invalidDate2 = "23.22.2022";
        String invalidDate3 = "23.02.22";

        String[] expected = new String[] { "02", "02", "2022" };
        String[] actual = DateHelper.getSplittedDate(validDate1);
        assertArrayEquals(expected, actual);

        expected = new String[] { "23", "02", "2022" };
        actual = DateHelper.getSplittedDate(validDate2);
        assertArrayEquals(expected, actual);

        expected = new String[] { "02", "02", "2022" };
        actual = DateHelper.getSplittedDate(validDate3);
        assertArrayEquals(expected, actual);

        actual = DateHelper.getSplittedDate(invalidDate1);
        assertEquals(3, actual.length);
        assertNotNull(actual[0]);
        assertNotNull(actual[1]);
        assertNotNull(actual[2]);

        actual = DateHelper.getSplittedDate(invalidDate2);
        assertEquals(3, actual.length);
        assertNotNull(actual[0]);
        assertNotNull(actual[1]);
        assertNotNull(actual[2]);

        actual = DateHelper.getSplittedDate(invalidDate3);
        assertEquals(3, actual.length);
        assertNotNull(actual[0]);
        assertNotNull(actual[1]);
        assertNotNull(actual[2]);
    }

    @Test
    public void getNextDate() {
        long yearInMills = 31_536_000_000L;

        Calendar nowadays = DateHelper.stringToDate(DateHelper.dateToString(Calendar.getInstance()));
        assertNotNull(nowadays);

        long expectedMills = nowadays.getTimeInMillis() + yearInMills;
        Calendar expectedCalendar = new GregorianCalendar();
        expectedCalendar.setTimeInMillis(expectedMills);
        String expected = DateHelper.dateToString(expectedCalendar);
        assertEquals(expected, DateHelper.getNextDate(DateHelper.dateToString(nowadays), 1));

        expectedMills = nowadays.getTimeInMillis() + (yearInMills / 2);
        expectedCalendar.setTimeInMillis(expectedMills);
        expected = DateHelper.dateToString(expectedCalendar);
        assertEquals(expected, DateHelper.getNextDate(DateHelper.dateToString(nowadays), 0.5));
    }
}