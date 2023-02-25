package util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateHelper {

    public static boolean isDateValid(String date) {
        if (date == null || date.isEmpty()) return false;

        String splitter = "\\.";
        String[] splittedString = date.split(splitter);

        try {
            int month = Integer.parseInt(splittedString[1]);
            if (month <= 0 || month > 12) return false;

            int year = Integer.parseInt(splittedString[2]);
            if (year < 1900) return false;

            Calendar calendar = new GregorianCalendar(year, month - 1, 1);
            int day = Integer.parseInt(splittedString[0]);
            return day > 0 && day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            return false;
        }
    }

    public static long yearsToMills(double years) {
        return (long) (31536000000L * years);
    }

    public static Calendar stringToDate(String date){
        if (isDateValid(date)) {
            if (date.charAt(1) == '.') date = '0' + date;
            int day = Integer.parseInt(date.substring(0, 2));

            if (date.charAt(4) == '.') date = date.substring(0,3) + '0' + date.substring(3);
            int month = Integer.parseInt(date.substring(3, 5));

            int year = Integer.parseInt(date.substring(6));
            return new GregorianCalendar(year, --month, day);
        } else return null;
    }

    public static String dateToString(Calendar date) {
        if (date == null){
            return null;
        }else {
            StringBuilder builder = new StringBuilder();
            String day;
            String month;
            if (date.get(Calendar.DAY_OF_MONTH) < 10) {
                day = "0" + date.get(Calendar.DAY_OF_MONTH);
            } else {
                day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
            }
            int m = date.get(Calendar.MONTH) + 1;
            if (m < 10) {
                month = "0" + m;
            } else {
                month = String.valueOf(m);
            }
            builder.append(day).append(".").append(month).append(".").append(date.get(Calendar.YEAR));
            return builder.toString();
        }
    }
}
