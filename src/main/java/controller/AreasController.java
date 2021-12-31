package controller;

import constants.Strings;
import model.Model;
import repository.Repository;
import ui.main.MainScreen;

import javax.swing.*;
import java.util.ArrayList;

public class AreasController implements Controller<String> {
    private final MainScreen mainScreen;
    private final ArrayList<String>areas;

    public AreasController(MainScreen mainScreen){
        this.mainScreen = mainScreen;
        this.areas = new Repository<String>(null, Model.AREA).readList();
    }

    @Override
    public void resetToDefault() {
        this.areas.clear();

        String OPU1 = "ОВДЗ-1";
        String OPU2 = "ОВДЗ-2";
        String OPU3 = "ОВДЗ-3";
        String OPU4 = "ОВДЗ-4";
        String CPO1 = "ЦВО-1";
        String CPO2 = "ЦВО-2";

        this.areas.add(OPU1);
        this.areas.add(OPU2);
        this.areas.add(OPU3);
        this.areas.add(OPU4);
        this.areas.add(CPO1);
        this.areas.add(CPO2);

        this.save();
    }

    @Override
    public ArrayList<String> getAll() {
        return this.areas;
    }

    @Override
    public ArrayList<String> add(String object) {
        if (!this.areas.contains(object)){
            this.areas.add(object);
            this.save();
        }
        return this.areas;
    }

    @Override
    public void remove(String object) {
        if (this.areas.contains(object)){
            this.areas.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
    }

    @Override
    public void set(String oldObject, String newObject) {
        if (oldObject != null){
            if (newObject == null){
                this.remove(oldObject);
            }else {
                int index = this.areas.indexOf(oldObject);
                this.areas.set(index, newObject);
            }
            this.save();
        }
    }

    @Override
    public String get(String object) {
        int index = this.areas.indexOf(object);
        if (index >= 0) {
            return this.areas.get(index);
        }else {
            this.showNotFoundMessage();
            return null;
        }
    }

    @Override
    public String get(int index) {
        if (index >= 0) {
            return this.areas.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.areas.clear();
        this.save();
    }

    @Override
    public void save() {
        new Repository<String>(this.mainScreen, Model.AREA).writeList(this.areas);
    }

    @Override
    public void showNotFoundMessage() {
        String message = "Ділянка з такою назвою не знайдена в списку ділянок.";
        JOptionPane.showMessageDialog(this.mainScreen, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
