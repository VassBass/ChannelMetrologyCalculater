package util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@SuppressWarnings("MagicConstant")
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
            return EMPTY;
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

    public static String getDayFromDateString(String date) {
        if (isDateValid(date)) {
            String[] splittedDate = date.split("\\.");
            String day = splittedDate[0];
            return day.length() == 1 ?
                    "0" + day :
                    day;
        }
        return EMPTY;
    }

    public static String getMonthFromDateString(String date) {
        if (isDateValid(date)) {
            String[] splittedDate = date.split("\\.");
            String month = splittedDate[1];
            return month.length() == 1 ?
                    "0" + month :
                    month;
        }
        return EMPTY;
    }

    public static String getYearFromDateString(String date) {
        if (isDateValid(date)) {
            String[] splittedDate = date.split("\\.");
            return splittedDate[2];
        }
        return EMPTY;
    }

    /**
     * @param date to splitt
     * @return array of Strings with length = 3
     * [0] - day of month
     * [1] - month (1 or 01 equal January)
     * [2] - year
     * if date in string not valid returns array with same length but all strings will be empty
     */
    public static String[] getSplittedDate(String date) {
        if (isDateValid(date)) {
            String[] splittedDate = date.split("\\.");
            if (splittedDate[0].length() == 1) splittedDate[0] = "0" + splittedDate[0];
            if (splittedDate[1].length() == 1) splittedDate[1] = "0" + splittedDate[1];
            return splittedDate;
        } else return new String[] { EMPTY, EMPTY, EMPTY };
    }

    public static String getNextDate(String date, double period) {
        Calendar dateCal = stringToDate(date);
        if (dateCal != null) {
            long yearInMills = 31_536_000_000L;
            long nextDateInMills = dateCal.getTimeInMillis() + ((long) (yearInMills * period));
            dateCal.setTimeInMillis(nextDateInMills);
            return dateToString(dateCal);
        }
        return EMPTY;
    }
}
