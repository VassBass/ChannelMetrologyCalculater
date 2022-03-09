package service.impl;

import model.ControlPointsValues;
import repository.ControlPointsValuesRepository;
import repository.impl.ControlPointsValuesRepositoryImpl;
import service.ControlPointsValuesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class ControlPointsValuesServiceImpl implements ControlPointsValuesService {
    private static final Logger LOGGER = Logger.getLogger(ControlPointsValuesService.class.getName());

    private final ControlPointsValuesRepository repository;
    private ArrayList<ControlPointsValues> mainList;

    public ControlPointsValuesServiceImpl(){
        this.repository = new ControlPointsValuesRepositoryImpl();
    }

    public ControlPointsValuesServiceImpl(String dbUrl){
        this.repository = new ControlPointsValuesRepositoryImpl(dbUrl);
    }

    @Override
    public void init() {
        this.mainList = this.repository.getAll();
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<ControlPointsValues> getBySensorType(String sensorType) {
        ArrayList<ControlPointsValues>list = new ArrayList<>();
        for (ControlPointsValues cpv : this.mainList){
            if (cpv.getSensorType().equals(sensorType)){
                list.add(cpv);
            }
        }
        return list;
    }

    @Override
    public double[] getValues(String sensorType, double rangeMin, double rangeMax) {
        for (ControlPointsValues cpv : this.mainList){
            if (cpv.isMatch(sensorType, rangeMin, rangeMax)){
                return cpv.getValues();
            }
        }
        return null;
    }

    @Override
    public ControlPointsValues getControlPointsValues(String sensorType, int index) {
        int i = 0;
        for (ControlPointsValues cpv : this.mainList){
            if (cpv.getSensorType().equals(sensorType)){
                if (i == index){
                    return cpv;
                }else {
                    i++;
                }
            }
        }
        return null;
    }

    @Override
    public void put(ControlPointsValues controlPointsValues) {
        if (controlPointsValues != null){
            int index = this.mainList.indexOf(controlPointsValues);
            if (index >= 0) {
                this.mainList.set(index, controlPointsValues);
            }else {
                this.mainList.add(controlPointsValues);
            }
            this.repository.put(controlPointsValues);
        }
    }

    @Override
    public void remove(ControlPointsValues controlPointsValues) {
        int index = this.mainList.indexOf(controlPointsValues);
        if (index >= 0){
            this.mainList.remove(index);
            this.repository.remove(controlPointsValues.getId());
        }
    }

    @Override
    public void removeAllInCurrentThread(String sensorType) {
        ArrayList<Integer>indexes = new ArrayList<>();
        for (int i=0;i<this.mainList.size();i++){
            ControlPointsValues cpv = this.mainList.get(i);
            if (cpv.getSensorType().equals(sensorType)){
                indexes.add(i);
            }
        }
        Collections.reverse(indexes);
        for (int i : indexes){
            this.mainList.remove(i);
        }
        this.repository.removeAllInCurrentThread(sensorType);
    }

    @Override
    public void clear(String sensorType) {
        ArrayList<Integer>indexes = new ArrayList<>();
        for (int i=0;i<this.mainList.size();i++){
            ControlPointsValues cpv = this.mainList.get(i);
            if (cpv.getSensorType().equals(sensorType)){
                indexes.add(i);
            }
        }
        Collections.reverse(indexes);
        for (int i : indexes){
            this.mainList.remove(i);
        }
        this.repository.clear(sensorType);
    }
}
