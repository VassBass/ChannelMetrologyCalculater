package service.impl;

import def.DefaultCalibrators;
import model.Calibrator;
import model.Measurement;
import repository.CalibratorRepository;
import repository.impl.CalibratorRepositoryImpl;
import service.CalibratorService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class CalibratorServiceImpl implements CalibratorService {
    private static final Logger LOGGER = Logger.getLogger(CalibratorService.class.getName());

    private final String dbUrl;
    private CalibratorRepository repository;

    public CalibratorServiceImpl(){
        this.dbUrl = null;
    }

    public CalibratorServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = this.dbUrl == null ? new CalibratorRepositoryImpl() : new CalibratorRepositoryImpl(this.dbUrl);
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Calibrator> getAll() {
        return this.repository.getAll();
    }

    @Override
    public String[] getAllNames(Measurement measurement){
        return this.repository.getAllNames(measurement);
    }

    @Override
    public ArrayList<Calibrator> add(Calibrator calibrator) {
        this.repository.add(calibrator);
        return this.repository.getAll();
    }

    @Override
    public void addInCurrentThread(Calibrator calibrator) {
        this.repository.addInCurrentThread(calibrator);
    }

    @Override
    public ArrayList<Calibrator> remove(Calibrator calibrator) {
        this.repository.remove(calibrator);
        return this.repository.getAll();
    }

    @Override
    public ArrayList<Calibrator> remove(int index){
        this.repository.remove(index);
        return this.repository.getAll();
    }

    @Override
    public ArrayList<Calibrator> set(Calibrator oldCalibrator, Calibrator newCalibrator) {
        this.repository.set(oldCalibrator, newCalibrator);
        return this.repository.getAll();
    }

    @Override
    public void setInCurrentThread(Calibrator oldCalibrator, Calibrator newCalibrator) {
        this.repository.setInCurrentThread(oldCalibrator, newCalibrator);
    }

    @Override
    public Calibrator get(String name) {
        return this.repository.get(name);
    }

    @Override
    public Calibrator get(int index) {
        return this.repository.get(index);
    }

    @Override
    public void clear() {
        this.repository.clear();
    }

    @Override
    public void importDataInCurrentThread(ArrayList<Calibrator>newCalibrators, ArrayList<Calibrator>calibratorsForChange){
        this.repository.importDataInCurrentThread(newCalibrators, calibratorsForChange);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Calibrator>calibrators){
        this.repository.rewriteInCurrentThread(calibrators);
    }

    @Override
    public void resetToDefaultInCurrentThread() {
        this.repository.rewriteInCurrentThread(DefaultCalibrators.get());
    }

    @Override
    public boolean isExists(Calibrator calibrator) {
        return this.repository.isExists(calibrator);
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.repository.backgroundTaskIsRun();
    }
}