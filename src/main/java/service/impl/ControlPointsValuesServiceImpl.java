package service.impl;

import model.ControlPointsValues;
import repository.ControlPointsValuesRepository;
import repository.impl.ControlPointsValuesRepositorySQLite;
import service.ControlPointsValuesService;

import java.util.Collection;

public class ControlPointsValuesServiceImpl implements ControlPointsValuesService {
    private static ControlPointsValuesServiceImpl service;

    private final ControlPointsValuesRepository repository;

    private ControlPointsValuesServiceImpl(){
        this.repository = new ControlPointsValuesRepositorySQLite();
    }

    public ControlPointsValuesServiceImpl(ControlPointsValuesRepository repository){
        this.repository = repository;
    }

    public static ControlPointsValuesServiceImpl getInstance() {
        if (service == null) service = new ControlPointsValuesServiceImpl();

        return service;
    }

    @Override
    public Collection<ControlPointsValues> getAll() {
        return this.repository.getAll();
    }

    @Override
    public boolean add(ControlPointsValues cpv) {
        return repository.add(cpv);
    }

    @Override
    public Collection<ControlPointsValues> getBySensorType(String sensorType) {
        return this.repository.getBySensorType(sensorType);
    }

    @Override
    public ControlPointsValues getControlPointsValues(int id) {
        return this.repository.getControlPointsValues(id);
    }

    @Override
    public int addReturnId(ControlPointsValues controlPointsValues) {
        return this.repository.addReturnId(controlPointsValues);
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
    public boolean set(ControlPointsValues cpv, ControlPointsValues ignored) {
        return repository.set(cpv, ignored);
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
    public boolean rewrite(Collection<ControlPointsValues> cpvList) {
        return repository.rewrite(cpvList);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.resetToDefault();
    }
}
