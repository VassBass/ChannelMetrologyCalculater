package service.impl;

import model.ControlPointsValues;
import repository.ControlPointsValuesRepository;
import repository.impl.ControlPointsValuesRepositoryImpl;
import service.ControlPointsValuesService;

import java.util.ArrayList;

public class ControlPointsValuesServiceImpl implements ControlPointsValuesService {
    private final ControlPointsValuesRepository repository;

    public ControlPointsValuesServiceImpl(){
        this.repository = new ControlPointsValuesRepositoryImpl();
    }

    public ControlPointsValuesServiceImpl(ControlPointsValuesRepository repository){
        this.repository = repository;
    }

    @Override
    public ArrayList<ControlPointsValues> getAll() {
        return this.repository.getAll();
    }

    @Override
    public ArrayList<ControlPointsValues> getBySensorType(String sensorType) {
        return this.repository.getBySensorType(sensorType);
    }

    @Override
    public ControlPointsValues getControlPointsValues(int id) {
        return this.repository.getControlPointsValues(id);
    }

    @Override
    public int add(ControlPointsValues controlPointsValues) {
        return this.repository.add(controlPointsValues);
    }

    @Override
    public boolean set(ControlPointsValues cpv) {
        return this.repository.set(cpv);
    }

    @Override
    public boolean changeSensorType(String oldSensorType, String newSensorType) {
        return this.repository.changeSensorType(oldSensorType, newSensorType);
    }

    @Override
    public boolean remove(ControlPointsValues controlPointsValues) {
        return this.repository.remove(controlPointsValues);
    }

    @Override
    public boolean removeAll(String sensorType) {
        return this.repository.removeAll(sensorType);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.resetToDefault();
    }
}
