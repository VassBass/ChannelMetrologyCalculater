package calibrators;

import constants.CalibratorType;
import constants.MeasurementConstants;

import java.util.Calendar;

public class Fluke750PD2_small extends Calibrator{

    public Fluke750PD2_small(){
        super();

        this.name = CalibratorType.FLUKE750PD2_small;
        this.number = "4043273";
        this.createCertificate();
        this.rangeMin = -4.5;
        this.rangeMax = 4.5;
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
