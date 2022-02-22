package service.impl;

import service.FileBrowser;
import def.DefaultDepartments;
import model.Model;
import repository.Repository;
import service.DepartmentService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class DepartmentServiceImpl implements DepartmentService {
    private static final Logger LOGGER = Logger.getLogger(DepartmentService.class.getName());

    private static final String ERROR = "Помилка";

    private Window window;
    private ArrayList<String> departments;

    private String exportFileName(Calendar date){
        return "export_departments ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].dep";
    }

    @Override
    public void init(Window window){
        LOGGER.info("DepartmentService: initialization start ...");
        try {
            this.departments = new Repository<String>(null, Model.DEPARTMENT).readList();
        }catch (Exception e){
            LOGGER.info("DepartmentService: file \"" + FileBrowser.FILE_DEPARTMENTS.getName() + "\" is empty");
            LOGGER.info("DepartmentService: set default list");
            this.departments = DefaultDepartments.get();
            this.save();
        }
        this.window = window;
        LOGGER.info("DepartmentService: initialization SUCCESS");
    }

    @Override
    public ArrayList<String> getAll() {
        return this.departments;
    }

    @Override
    public String[] getAllInStrings(){
        return this.departments.toArray(new String[0]);
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
    public ArrayList<String> remove(String object) {
        if (this.departments.contains(object)){
            this.departments.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.departments;
    }

    @Override
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
        new Repository<String>(this.window, Model.DEPARTMENT).writeList(this.departments);
    }

    @Override
    public boolean exportData(){
        try {
            String fileName = this.exportFileName(Calendar.getInstance());
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), this.departments);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>departments){
        this.departments = departments;
        new Repository<String>(null, Model.DEPARTMENT).writeListInCurrentThread(departments);
    }

    private void showNotFoundMessage() {
        String message = "Цех з такою назвою не знайдено в списку цехів.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
