package service.impl;

import def.DefaultControlPointsValues;
import model.ControlPointsValues;
import repository.ControlPointsValuesRepository;
import service.ControlPointsValuesService;
import service.FileBrowser;

import java.util.ArrayList;
import java.util.Collections;

public class ControlPointsValuesServiceImpl implements ControlPointsValuesService {
    private ArrayList<ControlPointsValues> mainList;

    @Override
    public void init() {
        try {
            this.mainList = new ControlPointsValuesRepository().readList();
        }catch (Exception ex){
            System.out.println("File \"" + FileBrowser.FILE_CONTROL_POINTS_VALUES.getName() + "\" is empty");
            this.mainList = DefaultControlPointsValues.get();
            this.save();
        }
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
        int index = -1;
        for (int i=0;i<this.mainList.size();i++){
            ControlPointsValues cpv = this.mainList.get(i);
            if (cpv.isMatch(controlPointsValues)){
                index = i;
                break;
            }
        }
        if (index >= 0){
            this.mainList.set(index, controlPointsValues);
        }else this.mainList.add(controlPointsValues);

        this.save();
    }

    @Override
    public void remove(ControlPointsValues controlPointsValues) {
        for (int i=0;i<this.mainList.size();i++){
            ControlPointsValues cpv = this.mainList.get(i);
            if (cpv.isMatch(controlPointsValues)){
                this.mainList.remove(i);
                break;
            }
        }

        this.save();
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
        new ControlPointsValuesRepository().writeListInCurrentThread(this.mainList);
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
        this.save();
    }

    @Override
    public void save(){
        new ControlPointsValuesRepository().writeList(this.mainList);
    }
}
