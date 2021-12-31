package controller;

import constants.Strings;
import model.Model;
import repository.Repository;
import ui.main.MainScreen;

import javax.swing.*;
import java.util.ArrayList;

public class DepartmentsController implements Controller<String> {
    private final MainScreen mainScreen;
    private final ArrayList<String>departments;

    public DepartmentsController(MainScreen mainScreen){
        this.mainScreen = mainScreen;
        this.departments = new Repository<String>(null, Model.DEPARTMENT).readList();
    }

    @Override
    public void resetToDefault() {
        this.departments.clear();

        String CPO = "ЦВО";
        String DOF = "ДЗФ";

        this.departments.add(CPO);
        this.departments.add(DOF);

        this.save();
    }

    @Override
    public ArrayList<String> getAll() {
        return this.departments;
    }

    @Override
    public ArrayList<String> add(String object) {
        if (!this.departments.contains(object)){
            this.departments.add(object);
            this.save();
        }
        return this.departments;
    }

    @Override
    public void remove(String object) {
        if (this.departments.contains(object)){
            this.departments.remove(object);
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
                int index = this.departments.indexOf(oldObject);
                this.departments.set(index, newObject);
            }
            this.save();
        }
    }

    @Override
    public String get(String object) {
        int index = this.departments.indexOf(object);
        if (index >= 0) {
            return this.departments.get(index);
        }else {
            this.showNotFoundMessage();
            return null;
        }
    }

    @Override
    public String get(int index) {
        if (index >= 0) {
            return this.departments.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.departments.clear();
        this.save();
    }

    @Override
    public void save() {
        new Repository<String>(this.mainScreen, Model.DEPARTMENT).writeList(this.departments);
    }

    @Override
    public void showNotFoundMessage() {
        String message = "Цех з такою назвою не знайдено в списку цехів.";
        JOptionPane.showMessageDialog(this.mainScreen, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
