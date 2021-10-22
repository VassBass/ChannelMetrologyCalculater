package converters;

import constants.Strings;

import java.util.Calendar;
import java.util.Locale;

public class VariableConverter {

    public static String commasToDots(String string){
        char[] chars = string.toCharArray();
        for (int x=0;x<chars.length;x++){
            if (chars[x] == ','){
                chars[x] = '.';
            }
        }
        return String.valueOf(chars);
    }

    public static String dateToString(Calendar date) {
        if (date == null){
            return Strings.EXTRAORDINARY;
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

    public static String doubleString(String string) {
        StringBuilder checkedString = new StringBuilder();
        boolean dot = false;
        for (int x=0;x<string.length();x++) {
            switch (string.charAt(x)) {
                case '.':
                    if (!dot) {
                        dot = true;
                        checkedString.append(string.charAt(x));
                    }
                    break;
                case ',':
                    if (!dot) {
                        dot = true;
                        checkedString.append('.');
                    }
                    break;
                case '-':
                    if (checkedString.length()==0) {
                        checkedString.append(string.charAt(x));
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    checkedString.append(string.charAt(x));
                    break;
            }
        }
        if (checkedString.toString().length()==0) {
            return "0.00";
        }else {
            return checkedString.toString();
        }
    }

    public static String intString(String string) {
        StringBuilder checkedString = new StringBuilder();
        for (int x=0;x<string.length();x++) {
            switch (string.charAt(x)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    checkedString.append(string.charAt(x));
                    break;
            }
        }
        if (checkedString.toString().length()==0) {
            return "0";
        }else {
            return checkedString.toString();
        }
    }

    public static String roundingDouble(double d, Locale locale) {
        String s = String.format(locale, "%.1f", d);

        char[] chars = s.toCharArray();
        int index = chars.length - 1;
        if (chars[index] == '0') {
            return String.format(locale, "%.0f", d);
        }else {
            return s;
        }
    }

    public static String roundingDouble2(double d, Locale locale) {
        String s = String.format(locale, "%.2f", d);
        char[] chars = s.toCharArray();
        int index = chars.length - 1;
        if (chars[index] == '0') {
            if (chars[index-1] == '0') {
                return String.format(locale, "%.0f", d);
            }else {
                return String.format(locale, "%.1f", d);
            }
        }else {
            return s;
        }
    }

    public static String roundingDouble3(double d, Locale locale){
        String s = String.format(locale, "%.3f", d);

        char[] chars = s.toCharArray();
        int index = chars.length - 1;
        if (chars[index] == '0') {
            if (chars[index - 1] == '0') {
                if (chars[index - 2] == '0'){
                    return String.format(locale, "%.0f", d);
                }else {
                    return String.format(locale, "%.1f", d);
                }
            }else {
                return String.format(locale, "%.2f", d);
            }
        } else {
            return s;
        }
    }
}
