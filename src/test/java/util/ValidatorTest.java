package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void isDateValid() {
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

        assertTrue(Validator.isDateValid(validDate1));
        assertTrue(Validator.isDateValid(validDate2));
        assertTrue(Validator.isDateValid(validDate3));

        assertFalse(Validator.isDateValid(invalidDate1));
        assertFalse(Validator.isDateValid(invalidDate2));
        assertFalse(Validator.isDateValid(invalidDate3));
        assertFalse(Validator.isDateValid(invalidDate4));
        assertFalse(Validator.isDateValid(invalidDate5));
        assertFalse(Validator.isDateValid(invalidDate6));
        assertFalse(Validator.isDateValid(invalidDate7));
        assertFalse(Validator.isDateValid(invalidDate8));
        assertFalse(Validator.isDateValid(invalidDate9));
        assertFalse(Validator.isDateValid(invalidDate10));
        assertFalse(Validator.isDateValid(invalidDate11));
        assertFalse(Validator.isDateValid(invalidDate12));
        assertFalse(Validator.isDateValid(invalidDate13));
        assertFalse(Validator.isDateValid(invalidDate14));
        assertFalse(Validator.isDateValid(invalidDate15));
        assertFalse(Validator.isDateValid(invalidDate16));
        assertFalse(Validator.isDateValid(invalidDate17));
        assertFalse(Validator.isDateValid(invalidDate18));
        assertFalse(Validator.isDateValid(null));
    }
}