package converters;

import java.util.Calendar;
import java.util.Locale;

public class VariableConverter {
    private static final String EXTRAORDINARY = "Позачерговий";

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
            return EXTRAORDINARY;
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

    public static String stringToDateString(String string){
        if (string == null) return null;
        String input = commasToDots(string);
        char[]in = input.toCharArray();
        char[]output = new char[10];

        char[]day = new char[]{in[0], in[1]};
        char[]month = new char[]{in[2], in[3], in[4]};

        char DOT = '.';
        char ZERO = '0';

        if (day[1] == DOT){
            output[0] = ZERO;
            output[1] = day[0];
        }else {
            output[0] = day[0];
            output[1] = day[1];
        }
        output[2] = DOT;

        int marker = 0;
        if (month[marker] == DOT){
            marker = 1;
        }
        if (month[marker + 1] == DOT){
            output[3] = ZERO;
            output[4] = month[marker];
        }else {
            output[3] = month[marker];
            output[4] = month[marker+1];
        }
        output[5] = DOT;

        output[6] = '2';
        output[7] = ZERO;
        output[8] = ZERO;
        output[9] = ZERO;
        marker = 0;
        for (int i=0;i<in.length;i++){
            if (in[i] == DOT){
                marker = i+1;
            }
        }
        int rightMarker = 1;
        while ((in.length - rightMarker) >= marker){
            output[output.length - rightMarker] = in[in.length - rightMarker];
            rightMarker++;
        }

        return String.valueOf(output);
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

    public static String roundingDouble4(double d, Locale locale){
        String s = String.format(locale, "%.4f", d);

        char[] chars = s.toCharArray();
        int index = chars.length - 1;
        if (chars[index] == '0') {
            if (chars[index - 1] == '0') {
                if (chars[index - 2] == '0') {
                    if (chars[index - 3] == '0') {
                        return String.format(locale, "%.0f", d);
                    } else {
                        return String.format(locale, "%.1f", d);
                    }
                } else {
                    return String.format(locale, "%.2f", d);
                }
            } else {
                return String.format(locale, "%.3f", d);
            }
        } else {
            return s;
        }
    }
}
