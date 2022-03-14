package service.impl;

import model.ControlPointsValues;
import repository.ControlPointsValuesRepository;
import repository.impl.ControlPointsValuesRepositoryImpl;
import service.ControlPointsValuesService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ControlPointsValuesServiceImpl implements ControlPointsValuesService {
    private static final Logger LOGGER = Logger.getLogger(ControlPointsValuesService.class.getName());

    private final String dbUrl;
    private ControlPointsValuesRepository repository;

    public ControlPointsValuesServiceImpl(){
        this.dbUrl = null;
    }

    public ControlPointsValuesServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init() {
        this.repository = this.dbUrl == null ? new ControlPointsValuesRepositoryImpl() : new ControlPointsValuesRepositoryImpl(this.dbUrl);
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<ControlPointsValues> getBySensorType(String sensorType) {
        return this.repository.getBySensorType(sensorType);
    }

    @Override
    public double[] getValues(String sensorType, double rangeMin, double rangeMax) {
        return this.repository.getValues(sensorType, rangeMin, rangeMax);
    }

    @Override
    public ControlPointsValues getControlPointsValues(String sensorType, int index) {
        return this.repository.getControlPointsValues(sensorType, index);
    }

    @Override
    public void put(ControlPointsValues controlPointsValues) {
        this.repository.put(controlPointsValues);
    }

    @Override
    public void remove(ControlPointsValues controlPointsValues) {
        this.repository.remove(controlPointsValues);
    }

    @Override
    public void removeAllInCurrentThread(String sensorType) {
        this.repository.removeAllInCurrentThread(sensorType);
    }

    @Override
    public void clear(String sensorType) {
        this.repository.clear(sensorType);
    }
}
