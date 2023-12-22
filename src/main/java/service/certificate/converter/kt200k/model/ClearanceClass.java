package service.certificate.converter.kt200k.model;

import util.Symbol;

import java.util.Locale;

public enum ClearanceClass {
    B("B", "B/" + Symbol.PLUS_MINUS + "(0.3+0.005|t|)");

    private static final String B_MARK = "B";

    private final String mark;
    private final String value;
    ClearanceClass(String mark, String value) {
        this.mark = mark.toUpperCase(Locale.ROOT);
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public String getMark() {
        return mark;
    }

    public static ClearanceClass create(String s) {
        if (s == null) return null;

        switch (s) {
            case B_MARK:
                return B;
            default:
                return null;
        }
    }
}
