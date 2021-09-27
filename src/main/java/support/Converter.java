package support;

import constants.MeasurementConstants;
import constants.Strings;

import java.awt.*;
import java.util.Calendar;
import java.util.Locale;

public class Converter {

    private final MeasurementConstants inValue;
    private final MeasurementConstants outValue;

    public Converter(MeasurementConstants inValue, MeasurementConstants outValue){
        this.inValue = inValue;
        this.outValue = outValue;
    }

    public Double get(double from){
        switch (this.outValue){

            case MM_ACVA:
                switch (this.inValue){
                    case MM_ACVA:
                        return from;
                    case KPA:
                        return from * 101.9716;
                    case PA:
                        return from * 0.1019716;
                    case KG_SM2:
                        return from * 10000D;
                    case BAR:
                        return from * 10197.16;
                    case KG_MM2:
                        return from * 1000027D;
                    case ML_BAR:
                        return from * 10.19744288922;
                    default:
                        return null;
                }

            case KPA:
                switch (this.inValue){
                    case MM_ACVA:
                        return from * (9.807750 * 0.001);
                    case KPA:
                        return from;
                    case PA:
                        return from * 0.001;
                    case KG_SM2:
                        return from * 98.0665;
                    case BAR:
                        return from * 100D;
                    case KG_MM2:
                        return from * 9806.65;
                    case ML_BAR:
                        return from * 0.1;
                    default:
                        return null;
                }

            case PA:
                switch (this.inValue){
                    case MM_ACVA:
                        return from * 9.80665;
                    case KPA:
                        return from * 1000D;
                    case PA:
                        return from;
                    case KG_SM2:
                        return from * 98066.5;
                    case BAR:
                        return from * 100000D;
                    case KG_MM2:
                        return from * 9806650D;
                    case ML_BAR:
                        return from * 100;
                    default:
                        return null;
                }

            case KG_SM2:
                switch (this.inValue){
                    case KG_SM2:
                        return from;
                    case MM_ACVA:
                        return from * 0.0001;
                    case KPA:
                        return from * 0.01019716;
                    case PA:
                        return from * (10.19716 * 0.000001);
                    case BAR:
                        return from * 1.019716;
                    case KG_MM2:
                        return from * 100;
                    case ML_BAR:
                        return from * 0.001019716212978;
                    default:
                        return null;
                }

            case BAR:
                switch (this.inValue){
                    case BAR:
                        return from;
                    case KG_SM2:
                        return from * 0.980665;
                    case MM_ACVA:
                        return from * (98.0665 * 0.000001);
                    case KPA:
                        return from * 0.01;
                    case PA:
                        return from * 0.00001;
                    case KG_MM2:
                        return from * 98.0665;
                    case ML_BAR:
                        return from * 0.001;
                    default:
                        return null;
                }

            case KG_MM2:
                switch (this.inValue){
                    case BAR:
                        return from * 0.01019716212978;
                    case KG_MM2:
                        return from;
                    case KG_SM2:
                        return from * 0.01;
                    case MM_ACVA:
                        return from * (9.999724776623 * 0.0000001);
                    case KPA:
                        return from * 0.0001019716212978;
                    case PA:
                        return from * (1.019716212978 * 0.0000001);
                    case ML_BAR:
                        return from * 0.00001019716212978;
                    default:
                        return null;
                }

            case ML_BAR:
                switch (this.inValue){
                    case BAR:
                        return from * 1000;
                    case KG_MM2:
                        return from * 98066.5;
                    case KG_SM2:
                        return from * 980.665;
                    case MM_ACVA:
                        return from * 0.0980638;
                    case KPA:
                        return from * 10;
                    case PA:
                        return from * 0.01;
                    default:
                        return null;
                }

            default:
                return null;
        }
    }

    public static Point POINT_CENTER (Component parent, Component child) {

        int x0 = parent.getLocation().x;
        int pw = parent.getWidth();
        int cw = child.getWidth();

        int y0 = parent.getLocation().y;
        int ph = parent.getHeight();
        int ch = child.getHeight();

        int x = x0 + (pw/2) - (cw/2);
        int y = y0 + (ph/2) - (ch/2);

        return new Point(x,y);
    }

    public static Point POINT_CENTER (Dimension parent, Component child) {

        int pw = parent.width;
        int cw = child.getWidth();

        int ph = parent.height;
        int ch = child.getHeight();

        int x = (pw/2) - (cw/2);
        int y = (ph/2) - (ch/2);

        return new Point(x,y);
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

    public static double getErrorFrom(double error, boolean inPercent, double range){
        if (error == 0D || range == 0D){
            return 0D;
        }else {
            if (error < 0){
                error = -1 * error;
            }
            if (inPercent) {
                return (range / 100) * error;
            } else {
                return (error / range) * 100;
            }
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
