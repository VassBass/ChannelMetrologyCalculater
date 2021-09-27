package calibrators;

import java.util.Calendar;

import constants.CalibratorType;
import constants.MeasurementConstants;

public class Fluke725 extends Calibrator {

    public Fluke725(){
        super();
        this.name = CalibratorType.FLUKE725;
        this.number = "1988293";
        this.createCertificate();
        this.measurements.add(MeasurementConstants.TEMPERATURE);
    }
    
    @Override
    public void createCertificate(){
        Certificate_calibrator certificate = new Certificate_calibrator();
        certificate.setName("№06/2647К");
        Calendar date = Calendar.getInstance();
        date.set(2020, Calendar.NOVEMBER, 16);
        certificate.setDate(date);
        certificate.setCompany("ДП\"ХарківСтандартМетрологія\"");
        this.certificate = certificate;
    }
}

