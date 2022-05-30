package converters;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    public static Calendar stringToDate(String date){
        int day = Integer.parseInt(date.substring(0,2));
        int month = Integer.parseInt(date.substring(3,5));
        int year = Integer.parseInt(date.substring(6));
        return new GregorianCalendar(year, --month, day);
    }

    public static Double parseToDouble(String str){
        try {
            String s = commasToDots(str);
            return Double.parseDouble(s);
        }catch (Exception e){
            return null;
        }
    }

    public static String doubleString(String string) {
        try {
            double d = Double.parseDouble(string);
            return String.valueOf(d);
        }catch (Exception ignored){}
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

    public static String kirillToLatinLetters(String in){
        StringBuilder builder = new StringBuilder();
        char[]chars = in.toCharArray();
        for (char ch : chars){
            switch (ch) {
                case 'Е':
                    builder.append('E');
                    break;
                case 'е':
                    builder.append('e');
                    break;
                case 'Т':
                    builder.append('T');
                    break;
                case 'І':
                    builder.append('I');
                    break;
                case 'і':
                    builder.append('i');
                    break;
                case 'О':
                    builder.append('O');
                    break;
                case 'о':
                    builder.append('o');
                    break;
                case 'Р':
                    builder.append('P');
                    break;
                case 'р':
                    builder.append('p');
                    break;
                case 'А':
                    builder.append('A');
                    break;
                case 'а':
                    builder.append('a');
                    break;
                case 'Н':
                    builder.append('H');
                    break;
                case 'К':
                    builder.append('K');
                    break;
                case 'Х':
                    builder.append('X');
                    break;
                case 'х':
                    builder.append('x');
                    break;
                case 'С':
                    builder.append('C');
                    break;
                case 'с':
                    builder.append('c');
                    break;
                case 'В':
                    builder.append('B');
                    break;
                case 'М':
                    builder.append('M');
                    break;
                default:
                    builder.append(ch);
                    break;
            }
        }
        return builder.toString();
    }

    public static String arrayToString(double[]array){
        StringBuilder builder = new StringBuilder();
        for (double v : array) {
            builder.append(v);
            builder.append("|");
        }
        return builder.toString();
    }

    public static double[]stringToArray(String stringArray){
        StringBuilder builder = new StringBuilder();
        char[]chars = stringArray.toCharArray();
        ArrayList<Double>array = new ArrayList<>();
        for (char ch : chars){
            if (ch == '|'){
                Double d = Double.parseDouble(builder.toString());
                array.add(d);
                builder.setLength(0);
            }else {
                builder.append(ch);
            }
        }
        Double[]arrr = array.toArray(new Double[0]);
        return ArrayUtils.toPrimitive(arrr);
    }

    public static boolean isItInt(String str){
        try {
            Integer.parseInt(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String superscript(String s){
        StringBuilder builder = new StringBuilder();
        char[] chars = s.toCharArray();
        for (char c : chars){
            switch (c){
                case '-':
                    builder.append("\u207B");
                    break;
                case '0':
                    builder.append("\u2070");
                    break;
                case '1':
                    builder.append("\u00B9");
                    break;
                case '2':
                    builder.append("\u00B2");
                    break;
                case '3':
                    builder.append("\u00B3");
                    break;
                case '4':
                    builder.append("\u2074");
                    break;
                case '5':
                    builder.append("\u2075");
                    break;
                case '6':
                    builder.append("\u2076");
                    break;
                case '7':
                    builder.append("\u2077");
                    break;
                case '8':
                    builder.append("\u2078");
                    break;
                case '9':
                    builder.append("\u2079");
                    break;
            }
        }
        return builder.toString();
    }
}
