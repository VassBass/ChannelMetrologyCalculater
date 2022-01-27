package controller;

import constants.CalibratorType;
import constants.MeasurementConstants;
import measurements.Measurement;
import model.Calibrator;
import model.Model;
import repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalibratorsController {
    private static final String ERROR = "Помилка";

    private Window window;
    private ArrayList<Calibrator> calibrators;

    private String exportFileName(Calendar date){
        return "export_calibrators ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].cal";
    }

    public void init(Window window){
        try {
            this.calibrators = new Repository<Calibrator>(null, Model.CALIBRATOR).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_CALIBRATORS.getName() + "\" is empty");
            this.calibrators = this.resetToDefault();
        }
        this.window = window;
    }

    public ArrayList<Calibrator> resetToDefault() {
        if (this.calibrators == null){
            this.calibrators = new ArrayList<>();
        }else this.calibrators.clear();

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
        ROSEMOUNT_8714DQ4.setType(CalibratorType.ROSEMOUNT_8714DQ4);
        ROSEMOUNT_8714DQ4.setName(CalibratorType.ROSEMOUNT_8714DQ4);
        ROSEMOUNT_8714DQ4.setNumber("14972506");
        ROSEMOUNT_8714DQ4.setMeasurement(MeasurementConstants.CONSUMPTION.getValue());
        ROSEMOUNT_8714DQ4.setCertificateName("відповідно до стандарту ISO 10474.3.1B");
        ROSEMOUNT_8714DQ4.setCertificateDate(new GregorianCalendar(2019, Calendar.NOVEMBER, 22));
        ROSEMOUNT_8714DQ4.setCertificateCompany("\"EMERSON\"");
        ROSEMOUNT_8714DQ4.setErrorFormula("(R / 100) * 0.1");
        this.calibrators.add(ROSEMOUNT_8714DQ4);

        this.save();
        return this.calibrators;
    }

    public ArrayList<Calibrator> getAll() {
        return this.calibrators;
    }

    public String[] getAllNames(Measurement measurement){
        ArrayList<String>cal = new ArrayList<>();
        for (Calibrator c : this.calibrators){
            if (c.getMeasurement().equals(measurement.getName())){
                cal.add(c.getName());
            }
        }
        return cal.toArray(new String[0]);
    }

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

    public ArrayList<Calibrator> remove(Calibrator calibrator) {
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
        return this.calibrators;
    }

    public ArrayList<Calibrator> remove(int index){
        if (index >= 0 && index<this.calibrators.size()){
            this.calibrators.remove(index);
            this.save();
        }
        return this.calibrators;
    }

    public ArrayList<Calibrator> set(Calibrator oldCalibrator, Calibrator newCalibrator) {
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
        return this.calibrators;
    }

    public Calibrator get(String calibratorName) {
        for (Calibrator calibrator : this.calibrators) {
            if (calibrator.getName().equals(calibratorName)) {
                return calibrator;
            }
        }
        this.showNotFoundMessage();
        return null;
    }

    public Calibrator get(int index) {
        if (index >= 0) {
            return this.calibrators.get(index);
        }else {
            return null;
        }
    }

    public void clear() {
        this.calibrators.clear();
        this.save();
    }

    private void save() {
        new Repository<Calibrator>(this.window, Model.CALIBRATOR).writeList(this.calibrators);
    }

    public boolean exportData(){
        try {
            String fileName = this.exportFileName(Calendar.getInstance());
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), this.calibrators);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public void importData(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator>calibratorsForChange){
        for (Calibrator calibrator : calibratorsForChange){
            for (int index=0;index<this.calibrators.size();index++){
                if (calibrator.getName().equals(this.calibrators.get(index).getName())){
                    this.calibrators.set(index, calibrator);
                    break;
                }
            }
        }
        this.calibrators.addAll(newCalibrators);
        new Repository<Calibrator>(null,Model.CALIBRATOR).writeListInCurrentThread(this.calibrators);
    }

    public void rewriteAll(ArrayList<Calibrator>calibrators){
        this.calibrators = calibrators;
        new Repository<Calibrator>(null, Model.CALIBRATOR).writeListInCurrentThread(calibrators);
    }

    private void showNotFoundMessage() {
        String message = "Калібратор з данною назвою не знайдено в списку калібраторів.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    private void showExistMessage() {
        String message = "Калібратор з данною назвою вже існує в списку калібраторів. Змініть будь ласка назву.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
