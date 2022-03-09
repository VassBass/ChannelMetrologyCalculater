package service.impl;

import application.Application;
import def.DefaultCalibrators;
import measurements.Measurement;
import model.Calibrator;
import repository.CalibratorRepository;
import repository.impl.CalibratorRepositoryImpl;
import service.CalibratorService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CalibratorServiceImpl implements CalibratorService {
    private static final Logger LOGGER = Logger.getLogger(CalibratorService.class.getName());

    private static final String ERROR = "Помилка";

    private final CalibratorRepository repository;
    private ArrayList<Calibrator> calibrators;

    public CalibratorServiceImpl(){
        this.repository = new CalibratorRepositoryImpl();
    }

    public CalibratorServiceImpl(String dbUrl){
        this.repository = new CalibratorRepositoryImpl(dbUrl);
    }

    @Override
    public void init(){
        this.calibrators = this.repository.getAll();
        LOGGER.info("Initialization SUCCESS");
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
        if (this.calibrators.contains(calibrator)){
            this.showExistMessage();
        }else {
            this.calibrators.add(calibrator);
            this.repository.add(calibrator);
        }
        return this.calibrators;
    }

    @Override
    public ArrayList<Calibrator> remove(Calibrator calibrator) {
        int index = this.calibrators.indexOf(calibrator);
        if (index >= 0){
            this.repository.remove(calibrator.getName());
            this.calibrators.remove(index);
        }else {
            this.showNotFoundMessage();
        }
        return this.calibrators;
    }

    @Override
    public ArrayList<Calibrator> remove(int index){
        if (index >= 0 && index<this.calibrators.size()){
            String name = this.calibrators.get(index).getName();
            this.repository.remove(name);
            this.calibrators.remove(index);
        }
        return this.calibrators;
    }

    @Override
    public ArrayList<Calibrator> set(Calibrator oldCalibrator, Calibrator newCalibrator) {
        if (oldCalibrator != null && newCalibrator != null){
            int index = this.calibrators.indexOf(oldCalibrator);
            if (index >= 0) {
                this.calibrators.set(index, newCalibrator);
                this.repository.set(oldCalibrator, newCalibrator);
            }
        }
        return this.calibrators;
    }

    @Override
    public Calibrator get(String name) {
        Calibrator calibrator = new Calibrator();
        calibrator.setName(name);
        int index = this.calibrators.indexOf(calibrator);
        if (index >= 0){
            return this.calibrators.get(index);
        }else {
            this.showNotFoundMessage();
            return null;
        }
    }

    @Override
    public Calibrator get(int index) {
        if (index >= 0 && index < this.calibrators.size()) {
            return this.calibrators.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.calibrators.clear();
        this.repository.clear();
    }

    @Override
    public void exportData(){
        this.repository.export(this.calibrators);
    }

    @Override
    public void importData(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator>calibratorsForChange){
        for (Calibrator calibrator : calibratorsForChange){
            int index = this.calibrators.indexOf(calibrator);
            if (index >= 0) this.calibrators.set(index, calibrator);
        }
        this.calibrators.addAll(newCalibrators);
        this.repository.rewriteInCurrentThread(this.calibrators);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Calibrator>calibrators){
        this.calibrators = calibrators;
        this.repository.rewriteInCurrentThread(calibrators);
    }

    @Override
    public void resetToDefault() {
        this.calibrators = DefaultCalibrators.get();
        this.repository.rewrite(this.calibrators);
    }

    private void showNotFoundMessage() {
        if (Application.context != null) {
            String message = "Калібратор з данною назвою не знайдено в списку калібраторів.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showExistMessage() {
        if (Application.context != null) {
            String message = "Калібратор з данною назвою вже існує в списку калібраторів. Змініть будь ласка назву.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}