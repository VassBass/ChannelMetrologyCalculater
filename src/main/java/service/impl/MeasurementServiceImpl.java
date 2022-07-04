package service.impl;

import def.DefaultMeasurements;
import model.Measurement;
import repository.MeasurementRepository;
import repository.impl.MeasurementRepositoryImpl;
import service.MeasurementService;

import java.util.ArrayList;
import java.util.HashMap;

public class MeasurementServiceImpl implements MeasurementService {
    private final MeasurementRepository repository;

    public MeasurementServiceImpl(){
        this.repository = new MeasurementRepositoryImpl();
    }

    public MeasurementServiceImpl(MeasurementRepository repository){
        this.repository = repository;
    }

    @Override
    public boolean add(Measurement measurement) {
        return this.repository.add(measurement);
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
    public boolean delete(Measurement measurement) {
        return this.repository.delete(measurement);
    }

    @Override
    public boolean changeFactors(String measurementValue, HashMap<String, Double> factors) {
        return this.repository.changeFactors(measurementValue, factors);
    }

    @Override
    public boolean change(Measurement oldMeasurement, Measurement newMeasurement) {
        return this.repository.change(oldMeasurement, newMeasurement);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public ArrayList<Measurement>getMeasurements(String name){
        return this.repository.getMeasurements(name);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultMeasurements.get());
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