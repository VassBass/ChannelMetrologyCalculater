package model.dto.builder;

import model.dto.Calibrator;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CalibratorBuilder {
    private final Calibrator calibrator;

    public CalibratorBuilder() {
        calibrator = new Calibrator();
    }

    public CalibratorBuilder(@Nonnull String name) {
        calibrator = new Calibrator(name);
    }

    public CalibratorBuilder setName(@Nonnull String name) {
        calibrator.setName(name);
        return this;
    }

    public CalibratorBuilder setType(String type) {
        calibrator.setType(type == null ? EMPTY : type);
        return this;
    }

    public CalibratorBuilder setNumber(String number) {
        calibrator.setNumber(number == null ? EMPTY : number);
        return this;
    }

    public CalibratorBuilder setCertificate(@Nonnull Calibrator.Certificate certificate) {
        calibrator.setCertificate(certificate);
        return this;
    }

    public CalibratorBuilder setCertificate(String type, String name, String date, String company) {
        calibrator.setCertificate(new Calibrator.Certificate.CertificateBuilder()
                .setType(type)
                .setName(name)
                .setDate(date)
                .setCompany(company)
                .build());
        return this;
    }

    public CalibratorBuilder setMeasurementName(String name) {
        calibrator.setMeasurementName(name == null ? EMPTY : name);
        return this;
    }

    public CalibratorBuilder setMeasurementValue(String value) {
        calibrator.setMeasurementValue(value == null ? EMPTY : value);
        return this;
    }

    public CalibratorBuilder setErrorFormula(String errorFormula) {
        calibrator.setErrorFormula(errorFormula == null ? EMPTY : errorFormula);
        return this;
    }

    public CalibratorBuilder setRangeMin(double rangeMin) {
        calibrator.setRangeMin(rangeMin);
        return this;
    }

    public CalibratorBuilder setRangeMax(double rangeMax) {
        calibrator.setRangeMax(rangeMax);
        return this;
    }

    public CalibratorBuilder setRange(double r1, double r2) {
        if (r1 > r2) {
            r1 = r1 + r2;
            r2 = r1 - r2;
            r1 -= r2;
        }
        calibrator.setRangeMin(r1);
        calibrator.setRangeMax(r2);

        return this;
    }

    public Calibrator build() {
        return calibrator;
    }

}
