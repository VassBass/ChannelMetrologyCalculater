package service.impl;

import def.DefaultControlPointsValues;
import model.ControlPointsValues;
import repository.ControlPointsValuesRepository;
import repository.impl.ControlPointsValuesRepositorySQLite;
import service.ControlPointsValuesService;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

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
    public boolean add(@Nonnull ControlPointsValues cpv) {
        return repository.add(cpv);
    }

    @Override
    public Collection<ControlPointsValues> getBySensorType(@Nonnull String sensorType) {
        return this.repository.getBySensorType(sensorType);
    }

    @Override
    public Optional<ControlPointsValues> getById(@Nonnegative int id) {
        return this.repository.getById(id);
    }

    @Override
    public Optional<Integer> addReturnId(@Nonnull ControlPointsValues controlPointsValues) {
        return this.repository.addReturnId(controlPointsValues);
    }

    @Override
    public boolean set(@Nonnull ControlPointsValues cpv) {
        return this.repository.set(cpv);
    }

    @Override
    public boolean changeSensorType(@Nonnull String oldSensorType, @Nonnull String newSensorType) {
        return this.repository.changeSensorType(oldSensorType, newSensorType);
    }

    @Override
    public boolean remove(@Nonnull ControlPointsValues controlPointsValues) {
        return this.repository.remove(controlPointsValues);
    }

    @Override
    public boolean set(@Nonnull ControlPointsValues cpv, @Nonnull ControlPointsValues ignored) {
        return repository.set(cpv, ignored);
    }

    @Override
    public boolean removeAll(@Nonnull String sensorType) {
        return this.repository.removeAll(sensorType);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<ControlPointsValues> cpvList) {
        return repository.rewrite(cpvList);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultControlPointsValues.get());
    }
}
