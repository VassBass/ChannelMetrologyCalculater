package service.impl;

import def.DefaultMeasurements;
import model.Measurement;
import repository.MeasurementRepository;
import repository.impl.MeasurementRepositoryImpl;
import service.MeasurementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class MeasurementServiceImpl implements MeasurementService {
    private static final Logger LOGGER = Logger.getLogger(MeasurementService.class.getName());

    private final String dbUrl;
    private  MeasurementRepository repository;

    public MeasurementServiceImpl(){
        this.dbUrl = null;
    }

    public MeasurementServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = this.dbUrl == null ? new MeasurementRepositoryImpl() : new MeasurementRepositoryImpl(dbUrl);
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public void addInCurrentThread(Measurement measurement) {
        this.repository.addInCurrentThread(measurement);
    }

    @Override
    public String[]getAllNames(){
        return this.repository.getAllNames();
    }

    @Override
    public String[]getAllValues(){
        return this.repository.getAllValues();
    }

    @Override
    public String[]getValues(Measurement measurement){
        return this.repository.getValues(measurement);
    }

    @Override
    public String[]getValues(String name){
        return this.repository.getValues(name);
    }

    @Override
    public ArrayList<Measurement> getAll() {
        return this.repository.getAll();
    }

    @Override
    public Measurement get(String value){
        return this.repository.get(value);
    }

    @Override
    public void delete(Measurement measurement) {
        this.repository.delete(measurement);
    }

    @Override
    public void changeFactors(String measurementValue, HashMap<String, Double> factors) {
        this.repository.changeFactors(measurementValue, factors);
    }

    @Override
    public void changeInCurrentThread(Measurement oldMeasurement, Measurement newMeasurement) {
        this.repository.changeInCurrentThread(oldMeasurement, newMeasurement);
    }

    @Override
    public void clear() {
        this.repository.clear();
    }

    @Override
    public ArrayList<Measurement>getMeasurements(String name){
        return this.repository.getMeasurements(name);
    }

    @Override
    public void resetToDefaultInCurrentThread() {
        this.repository.rewriteInCurrentThread(DefaultMeasurements.get());
    }

    @Override
    public boolean isLastInMeasurement(String measurementValue) {
        return this.repository.isLastInMeasurement(measurementValue);
    }

    @Override
    public boolean exists(String measurementValue) {
        return this.repository.exists(measurementValue);
    }

    @Override
    public boolean exists(String oldValue, String newValue) {
        return this.repository.exists(oldValue, newValue);
    }

}