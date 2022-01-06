package controller;

import constants.Strings;
import model.Model;
import repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DepartmentsController {
    private Window window;
    private ArrayList<String>departments;

    public void init(Window window){
        this.window = window;
        try {
            this.departments = new Repository<String>(null, Model.DEPARTMENT).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_DEPARTMENTS.getName() + "\" is empty");
            this.departments = this.resetToDefault();
        }
    }

    public ArrayList<String> resetToDefault() {
        if (this.departments == null){
            this.departments = new ArrayList<>();
        }else this.departments.clear();

        String CPO = "ЦВО";
        String DOF = "ДЗФ";

        this.departments.add(CPO);
        this.departments.add(DOF);

        this.save();
        return this.departments;
    }

    public ArrayList<String> getAll() {
        return this.departments;
    }

    public String[] getAllInStrings(){
        return this.departments.toArray(new String[0]);
    }

    public ArrayList<String> add(String object) {
        if (!this.departments.contains(object)){
            this.departments.add(object);
            this.save();
        }
        return this.departments;
    }

    public ArrayList<String> remove(String object) {
        if (this.departments.contains(object)){
            this.departments.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.departments;
    }

    public ArrayList<String> set(String oldObject, String newObject) {
        if (oldObject != null){
            if (newObject == null){
                this.remove(oldObject);
            }else {
                int index = this.departments.indexOf(oldObject);
                this.departments.set(index, newObject);
            }
            this.save();
        }
        return this.departments;
    }

    public int getIndex(String object) {
        return this.departments.indexOf(object);
    }

    public String get(int index) {
        if (index >= 0) {
            return this.departments.get(index);
        }else {
            return null;
        }
    }

    public void clear() {
        this.departments.clear();
        this.save();
    }

    private void save() {
        new Repository<String>(this.window, Model.DEPARTMENT).writeList(this.departments);
    }

    private void showNotFoundMessage() {
        String message = "Цех з такою назвою не знайдено в списку цехів.";
        JOptionPane.showMessageDialog(this.window, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
