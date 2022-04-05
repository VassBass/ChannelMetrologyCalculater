package def;

import model.Calibrator;
import model.Measurement;

import java.util.ArrayList;

public class DefaultCalibrators {
    public static ArrayList<Calibrator> get() {
        ArrayList<Calibrator>calibrators = new ArrayList<>();

        Calibrator fluke725 = new Calibrator();
        fluke725.setType("Fluke 725");
        fluke725.setName("Fluke 725");
        fluke725.setNumber("1988293");
        fluke725.setMeasurement(Measurement.TEMPERATURE);
        fluke725.setValue(Measurement.DEGREE_CELSIUS);
        fluke725.setCertificateName("№06/2647К");
        fluke725.setCertificateDate("16.11.2020");
        fluke725.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke725.setErrorFormula("0.7");
        calibrators.add(fluke725);

        Calibrator fluke724 = new Calibrator();
        fluke724.setType("Fluke 724");
        fluke724.setName("Fluke 724");
        fluke724.setNumber("1988293");
        fluke724.setMeasurement(Measurement.TEMPERATURE);
        fluke724.setValue(Measurement.DEGREE_CELSIUS);
        fluke724.setCertificateName("№06/1777К");
        fluke724.setCertificateDate("22.10.2019");
        fluke724.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke724.setErrorFormula("0.7");
        calibrators.add(fluke724);

        Calibrator prova123_minus = new Calibrator();
        prova123_minus.setType("Prova-123");
        prova123_minus.setName("Prova-123 t < 0" + Measurement.DEGREE_CELSIUS);
        prova123_minus.setNumber("13180302");
        prova123_minus.setMeasurement(Measurement.TEMPERATURE);
        prova123_minus.setValue(Measurement.DEGREE_CELSIUS);
        prova123_minus.setCertificateName("№06/2315К");
        prova123_minus.setCertificateDate("21.07.2020");
        prova123_minus.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        prova123_minus.setErrorFormula("1.1");
        calibrators.add(prova123_minus);

        Calibrator prova123 = new Calibrator();
        prova123.setType("Prova-123");
        prova123.setName("Prova-123 t > 0" + Measurement.DEGREE_CELSIUS);
        prova123.setNumber("13180302");
        prova123.setMeasurement(Measurement.TEMPERATURE);
        prova123.setValue(Measurement.DEGREE_CELSIUS);
        prova123.setCertificateName("№06/2315К");
        prova123.setCertificateDate("21.07.2020");
        prova123.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        prova123.setErrorFormula("0.8");
        calibrators.add(prova123);

        Calibrator fluke718_30g = new Calibrator();
        fluke718_30g.setType("Fluke 718 30G");
        fluke718_30g.setName("Fluke 718 30G");
        fluke718_30g.setNumber("2427047");
        fluke718_30g.setMeasurement(Measurement.PRESSURE);
        fluke718_30g.setRangeMin(-83D);
        fluke718_30g.setRangeMax(207D);
        fluke718_30g.setValue(Measurement.KPA);
        fluke718_30g.setCertificateName("№05/3570К");
        fluke718_30g.setCertificateDate("02.09.2020");
        fluke718_30g.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke718_30g.setErrorFormula("(convR / 100) * 0.05");
        calibrators.add(fluke718_30g);

        Calibrator fluke750pd2 = new Calibrator();
        fluke750pd2.setType("Fluke 750PD2");
        fluke750pd2.setName("Fluke 750PD2");
        fluke750pd2.setNumber("4043273");
        fluke750pd2.setMeasurement(Measurement.PRESSURE);
        fluke750pd2.setRangeMin(-7D);
        fluke750pd2.setRangeMax(7D);
        fluke750pd2.setValue(Measurement.KPA);
        fluke750pd2.setCertificateName("№05/3570К");
        fluke750pd2.setCertificateDate("02.09.2020");
        fluke750pd2.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke750pd2.setErrorFormula("(convR / 100) * 0.05");
        calibrators.add(fluke750pd2);

        Calibrator fluke750pd2_smallPressure = new Calibrator();
        fluke750pd2_smallPressure.setType("Fluke 750PD2");
        fluke750pd2_smallPressure.setName("Fluke 750PD2 (для Р<60 мм вод ст)");
        fluke750pd2_smallPressure.setNumber("4043273");
        fluke750pd2_smallPressure.setMeasurement(Measurement.PRESSURE);
        fluke750pd2_smallPressure.setRangeMin(-4.5);
        fluke750pd2_smallPressure.setRangeMax(4.5);
        fluke750pd2_smallPressure.setValue(Measurement.KPA);
        fluke750pd2_smallPressure.setCertificateName("№05/3570К");
        fluke750pd2_smallPressure.setCertificateDate("02.09.2020");
        fluke750pd2_smallPressure.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke750pd2_smallPressure.setErrorFormula("(convR / 100) * 0.05");
        calibrators.add(fluke750pd2_smallPressure);

        Calibrator YAKOGAWA_AM012 = new Calibrator();
        YAKOGAWA_AM012.setType("AM012");
        YAKOGAWA_AM012.setName("YAKOGAWA AM012");
        YAKOGAWA_AM012.setNumber("S5T800358");
        YAKOGAWA_AM012.setMeasurement(Measurement.CONSUMPTION);
        YAKOGAWA_AM012.setCertificateName("№UA/24/200717/265");
        YAKOGAWA_AM012.setCertificateDate("17.07.2020");
        YAKOGAWA_AM012.setCertificateCompany("ДП\"Укрметртестстандарт\"");
        YAKOGAWA_AM012.setErrorFormula("(R / 100) * 0.06");
        calibrators.add(YAKOGAWA_AM012);

        Calibrator ROSEMOUNT_8714DQ4 = new Calibrator();
        ROSEMOUNT_8714DQ4.setType(Calibrator.ROSEMOUNT_8714DQ4);
        ROSEMOUNT_8714DQ4.setName(Calibrator.ROSEMOUNT_8714DQ4);
        ROSEMOUNT_8714DQ4.setNumber("14972506");
        ROSEMOUNT_8714DQ4.setMeasurement(Measurement.CONSUMPTION);
        ROSEMOUNT_8714DQ4.setCertificateName("відповідно до стандарту ISO 10474.3.1B");
        ROSEMOUNT_8714DQ4.setCertificateDate("22.11.2019");
        ROSEMOUNT_8714DQ4.setCertificateCompany("\"EMERSON\"");
        ROSEMOUNT_8714DQ4.setErrorFormula("(R / 100) * 0.1");
        calibrators.add(ROSEMOUNT_8714DQ4);

        return calibrators;
    }
}
