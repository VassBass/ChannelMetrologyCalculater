package util;

import model.dto.Calibrator;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NotNullObject {
    public static String get(String s) {
        return s == null ? EMPTY : s;
    }

    public static Calibrator.Certificate get(Calibrator.Certificate c) {
        return c == null ?
                new Calibrator.Certificate.CertificateBuilder()
                        .setDate(EMPTY)
                        .setCompany(EMPTY)
                        .setType(EMPTY)
                        .setName(EMPTY)
                        .build() :
                c;
    }
}
