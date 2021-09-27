package calibrators;

import constants.CalibratorType;
import constants.MeasurementConstants;

import java.util.Calendar;

public class Prova_123 extends Calibrator {

    public Prova_123(){
        super();

        this.name = CalibratorType.PROVA123;
        this.number = "13180302";
        this.createCertificate();
        this.measurements.add(MeasurementConstants.TEMPERATURE);
    }

    @Override
    public void createCertificate(){
        Certificate_calibrator certificate = new Certificate_calibrator();
        certificate.setName("№06/2315К");
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.JULY, 21);
        certificate.setDate(date);
        certificate.setCompany("ДП\"ХарківСтандартМетрологія\"");
        this.certificate = certificate;
    }
}
