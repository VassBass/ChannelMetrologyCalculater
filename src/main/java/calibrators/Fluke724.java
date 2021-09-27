package calibrators;

import constants.CalibratorType;
import constants.MeasurementConstants;

import java.util.Calendar;

public class Fluke724 extends Calibrator {

    public Fluke724(){
        super();
        this.name = CalibratorType.FLUKE724;
        this.number = "1988293";
        this.createCertificate();
        this.measurements.add(MeasurementConstants.TEMPERATURE);
    }
    
    @Override
    public void createCertificate(){
        Certificate_calibrator certificate = new Certificate_calibrator();
        certificate.setName("№06/1777К");
        Calendar date = Calendar.getInstance();
        date.set(2019, Calendar.OCTOBER, 22);
        certificate.setDate(date);
        certificate.setCompany("ДП\"ХарківСтандартМетрологія\"");
        this.certificate = certificate;
    }
}

