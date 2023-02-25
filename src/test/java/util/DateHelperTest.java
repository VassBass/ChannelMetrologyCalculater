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
    }
}