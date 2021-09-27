package calibrators;

import constants.CalibratorType;
import constants.MeasurementConstants;

import java.util.Calendar;

public class Fluke750PD2 extends Calibrator {

    public Fluke750PD2(){
        super();

        this.name = CalibratorType.FLUKE750PD2;
        this.number = "4043273";
        this.createCertificate();
        this.rangeMin = -7D;
        this.rangeMax = 7D;
        this.value = MeasurementConstants.KPA.getValue();
        this.measurements.add(MeasurementConstants.PRESSURE);
    }

    @Override
    public void createCertificate(){
        Certificate_calibrator certificate = new Certificate_calibrator();
        certificate.setName("№05/3570К");
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.SEPTEMBER, 2);
        certificate.setDate(date);
        certificate.setCompany("ДП\"ХарківСтандартМетрологія\"");
        this.certificate = certificate;
    }
}
