package service.impl;

import service.FileBrowser;
import def.DefaultCalibrators;
import measurements.Measurement;
import model.Calibrator;
import model.Model;
import repository.Repository;
import service.CalibratorService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class CalibratorServiceImpl implements CalibratorService {
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

    @Override
    public void init(Window window){
        try {
            this.calibrators = new Repository<Calibrator>(null, Model.CALIBRATOR).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_CALIBRATORS.getName() + "\" is empty");
            this.calibrators = DefaultCalibrators.get();
            this.save();
        }
        this.window = window;
    }

    @Override
    public ArrayList<Calibrator> getAll() {
        return this.calibrators;
    }

    @Override
    public String[] getAllNames(Measurement measurement){
        ArrayList<String>cal = new ArrayList<>();
        for (Calibrator c : this.calibrators){
            if (c.getMeasurement().equals(measurement.getName())){
                cal.add(c.getName());
            }
        }
        return cal.toArray(new String[0]);
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

    @Override
    public ArrayList<Calibrator> remove(int index){
        if (index >= 0 && index<this.calibrators.size()){
            this.calibrators.remove(index);
            this.save();
        }
        return this.calibrators;
    }

    @Override
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

    @Override
    public Calibrator get(String name) {
        for (Calibrator calibrator : this.calibrators) {
            if (calibrator.getName().equals(name)) {
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
        new Repository<Calibrator>(this.window, Model.CALIBRATOR).writeList(this.calibrators);
    }

    @Override
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

    @Override
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

    @Override
    public void rewriteInCurrentThread(ArrayList<Calibrator>calibrators){
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
