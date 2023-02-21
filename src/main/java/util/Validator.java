package util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Validator {
    public static boolean isDateValid(String date) {
        if (date == null || date.isEmpty()) return false;

        String splitter = "\\.";
        String[] splittedString = date.split(splitter);

        try {
            int month = Integer.parseInt(splittedString[1]);
            if (month <= 0 || month > 12) return false;

            int year = Integer.parseInt(splittedString[2]);

            Calendar calendar = new GregorianCalendar(year, month - 1, 1);
            int day = Integer.parseInt(splittedString[0]);
            return day > 0 && day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            return false;
        }
    }
}
