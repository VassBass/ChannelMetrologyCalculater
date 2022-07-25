package service.impl;

import def.DefaultMeasurements;
import model.Measurement;
import repository.MeasurementRepository;
import repository.impl.MeasurementRepositorySQLite;
import service.MeasurementService;

import java.util.List;
import java.util.Map;

public class MeasurementServiceImpl implements MeasurementService {
    private final MeasurementRepository repository;

    public MeasurementServiceImpl(){
        this.repository = new MeasurementRepositorySQLite();
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
    public List<Measurement> getAll() {
        return this.repository.getAll();
    }

    @Override
    public Measurement get(String value){
        return this.repository.get(value);
    }

    @Override
    public boolean remove(Measurement measurement) {
        return this.repository.remove(measurement);
    }

    @Override
    public boolean changeFactors(String measurementValue, Map<String, Double> factors) {
        return this.repository.changeFactors(measurementValue, factors);
    }

    @Override
    public boolean set(Measurement oldMeasurement, Measurement newMeasurement) {
        return this.repository.set(oldMeasurement, newMeasurement);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean rewrite(List<Measurement> measurements) {
        return repository.rewrite(measurements);
    }

    @Override
    public List<Measurement>getMeasurements(String name){
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