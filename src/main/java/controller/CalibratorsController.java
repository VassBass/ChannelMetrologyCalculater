package controller;

import constants.MeasurementConstants;
import constants.Strings;
import model.Calibrator;
import model.Model;
import repository.Repository;
import ui.main.MainScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalibratorsController implements Controller<Calibrator> {
    private final MainScreen mainScreen;
    private final ArrayList<Calibrator> calibrators;

    public CalibratorsController(MainScreen mainScreen){
        this.mainScreen = mainScreen;
        this.calibrators = new Repository<Calibrator>(null, Model.CALIBRATOR).readList();
    }

    @Override
    public void resetToDefault() {
        this.calibrators.clear();

        Calibrator fluke725 = new Calibrator();
        fluke725.setType("Fluke 725");
        fluke725.setName("Fluke 725");
        fluke725.setNumber("1988293");
        fluke725.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        fluke725.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        fluke725.setCertificateName("№06/2647К");
        fluke725.setCertificateDate(new GregorianCalendar(2020, Calendar.NOVEMBER, 16));
        fluke725.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke725.setErrorFormula("0.7");
        this.calibrators.add(fluke725);

        Calibrator fluke724 = new Calibrator();
        fluke724.setType("Fluke 724");
        fluke724.setName("Fluke 724");
        fluke724.setNumber("1988293");
        fluke724.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        fluke724.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        fluke724.setCertificateName("№06/1777К");
        fluke724.setCertificateDate(new GregorianCalendar(2019, Calendar.OCTOBER, 22));
        fluke724.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke724.setErrorFormula("0.7");
        this.calibrators.add(fluke724);

        Calibrator prova123_minus = new Calibrator();
        prova123_minus.setType("Prova-123");
        prova123_minus.setName("Prova-123 t < 0" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        prova123_minus.setNumber("13180302");
        prova123_minus.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        prova123_minus.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        prova123_minus.setCertificateName("№06/2315К");
        prova123_minus.setCertificateDate(new GregorianCalendar(2020, Calendar.JULY, 21));
        prova123_minus.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        prova123_minus.setErrorFormula("1.1");
        this.calibrators.add(prova123_minus);

        Calibrator prova123 = new Calibrator();
        prova123.setType("Prova-123");
        prova123.setName("Prova-123 t > 0" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        prova123.setNumber("13180302");
        prova123.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        prova123.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        prova123.setCertificateName("№06/2315К");
        prova123.setCertificateDate(new GregorianCalendar(2020, Calendar.JULY, 21));
        prova123.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        prova123.setErrorFormula("0.8");
        this.calibrators.add(prova123);

        Calibrator fluke718_30g = new Calibrator();
        fluke718_30g.setType("Fluke 718 30G");
        fluke718_30g.setName("Fluke 718 30G");
        fluke718_30g.setNumber("2427047");
        fluke718_30g.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fluke718_30g.setRangeMin(-83D);
        fluke718_30g.setRangeMax(207D);
        fluke718_30g.setValue(MeasurementConstants.KPA.getValue());
        fluke718_30g.setCertificateName("№05/3570К");
        fluke718_30g.setCertificateDate(new GregorianCalendar(2020, Calendar.SEPTEMBER, 2));
        fluke718_30g.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke718_30g.setErrorFormula("(convR / 100) * 0.05");
        this.calibrators.add(fluke718_30g);

        Calibrator fluke750pd2 = new Calibrator();
        fluke750pd2.setType("Fluke 750PD2");
        fluke750pd2.setName("Fluke 750PD2");
        fluke750pd2.setNumber("4043273");
        fluke750pd2.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fluke750pd2.setRangeMin(-7D);
        fluke750pd2.setRangeMax(7D);
        fluke750pd2.setValue(MeasurementConstants.KPA.getValue());
        fluke750pd2.setCertificateName("№05/3570К");
        fluke750pd2.setCertificateDate(new GregorianCalendar(2020, Calendar.SEPTEMBER, 2));
        fluke750pd2.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke750pd2.setErrorFormula("(convR / 100) * 0.05");
        this.calibrators.add(fluke750pd2);

        Calibrator fluke750pd2_smallPressure = new Calibrator();
        fluke750pd2_smallPressure.setType("Fluke 750PD2");
        fluke750pd2_smallPressure.setName("Fluke 750PD2 (для Р<60 мм вод ст)");
        fluke750pd2_smallPressure.setNumber("4043273");
        fluke750pd2_smallPressure.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fluke750pd2_smallPressure.setRangeMin(-4.5);
        fluke750pd2_smallPressure.setRangeMax(4.5);
        fluke750pd2_smallPressure.setValue(MeasurementConstants.KPA.getValue());
        fluke750pd2_smallPressure.setCertificateName("№05/3570К");
        fluke750pd2_smallPressure.setCertificateDate(new GregorianCalendar(2020, Calendar.SEPTEMBER, 2));
        fluke750pd2_smallPressure.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke750pd2_smallPressure.setErrorFormula("(convR / 100) * 0.05");
        this.calibrators.add(fluke750pd2_smallPressure);

        Calibrator YAKOGAWA_AM012 = new Calibrator();
        YAKOGAWA_AM012.setType("AM012");
        YAKOGAWA_AM012.setName("YAKOGAWA AM012");
        YAKOGAWA_AM012.setNumber("S5T800358");
        YAKOGAWA_AM012.setMeasurement(MeasurementConstants.CONSUMPTION.getValue());
        YAKOGAWA_AM012.setCertificateName("№UA/24/200717/265");
        YAKOGAWA_AM012.setCertificateDate(new GregorianCalendar(2020, Calendar.JULY, 17));
        YAKOGAWA_AM012.setCertificateCompany("ДП\"Укрметртестстандарт\"");
        YAKOGAWA_AM012.setErrorFormula("(R / 100) * 0.06");
        this.calibrators.add(YAKOGAWA_AM012);

        Calibrator ROSEMOUNT_8714DQ4 = new Calibrator();
        ROSEMOUNT_8714DQ4.setType(Strings.CALIBRATOR_ROSEMOUNT_8714DQ4);
        ROSEMOUNT_8714DQ4.setName(Strings.CALIBRATOR_ROSEMOUNT_8714DQ4);
        ROSEMOUNT_8714DQ4.setNumber("14972506");
        ROSEMOUNT_8714DQ4.setMeasurement(MeasurementConstants.CONSUMPTION.getValue());
        ROSEMOUNT_8714DQ4.setCertificateName("відповідно до стандарту ISO 10474.3.1B");
        ROSEMOUNT_8714DQ4.setCertificateDate(new GregorianCalendar(2019, Calendar.NOVEMBER, 22));
        ROSEMOUNT_8714DQ4.setCertificateCompany("\"EMERSON\"");
        ROSEMOUNT_8714DQ4.setErrorFormula("(R / 100) * 0.1");
        this.calibrators.add(ROSEMOUNT_8714DQ4);

        this.save();
    }

    @Override
    public ArrayList<Calibrator> getAll() {
        return this.calibrators;
    }

    @Override
    public ArrayList<Calibrator> add(Calibrator calibrator) {
        boolean exist = false;
        for (Calibrator cal : this.calibrators){
            if (cal.getName().equals(calibrator.getName())){
                exist = true;
                break;
            }
        }
        if (exist){
            this.showExistMessage();
        }else {
            this.calibrators.add(calibrator);
            this.save();
        }
        return this.calibrators;
    }

    @Override
    public void remove(Calibrator calibrator) {
        boolean removed = false;

        for (Calibrator cal : this.calibrators){
            if (cal.getName().equals(calibrator.getName())){
                this.calibrators.remove(cal);
                removed = true;
                break;
            }
        }

        if (removed){
            this.save();
        }else {
            this.showNotFoundMessage();
        }
    }

    @Override
    public void set(Calibrator oldCalibrator, Calibrator newCalibrator) {
        if (oldCalibrator != null){
            if (newCalibrator == null){
                this.remove(oldCalibrator);
            }else {
                for (int c=0;c<this.calibrators.size();c++){
                    String calibratorName = this.calibrators.get(c).getName();
                    if (calibratorName.equals(oldCalibrator.getName())){
                        this.calibrators.set(c, newCalibrator);
                        break;
                    }
                }
            }
            this.save();
        }
    }

    @Override
    public Calibrator get(String calibratorName) {
        for (Calibrator calibrator : this.calibrators) {
            if (calibrator.getName().equals(calibratorName)) {
                return calibrator;
            }
        }
        this.showNotFoundMessage();
        return null;
    }

    @Override
    public Calibrator get(int index) {
        if (index >= 0) {
            return this.calibrators.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.calibrators.clear();
        this.save();
    }

    @Override
    public void save() {
        new Repository<Calibrator>(this.mainScreen, Model.CALIBRATOR).writeList(this.calibrators);
    }

    @Override
    public void showNotFoundMessage() {
        String message = "Калібратор з данною назвою не знайдено в списку калібраторів.";
        JOptionPane.showMessageDialog(this.mainScreen, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }

    public void showExistMessage() {
        String message = "Калібратор з данною назвою вже існує в списку калібраторів. Змініть будь ласка назву.";
        JOptionPane.showMessageDialog(this.mainScreen, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
