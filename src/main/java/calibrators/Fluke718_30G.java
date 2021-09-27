package calibrators;

import constants.CalibratorType;
import constants.MeasurementConstants;

import java.util.Calendar;

public class Fluke718_30G extends Calibrator {

    public Fluke718_30G(){
        super();

        this.name = CalibratorType.FLUKE718_30G;
        this.number = "2427047";
        this.createCertificate();
        this.measurements.add(MeasurementConstants.PRESSURE);
        this.rangeMin = -83D;
        this.rangeMax = 207D;
        this.value = MeasurementConstants.KPA.getValue();
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
