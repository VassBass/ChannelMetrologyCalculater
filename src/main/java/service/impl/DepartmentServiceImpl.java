package service.impl;

import application.Application;
import repository.DepartmentRepository;
import repository.impl.DepartmentRepositoryImpl;
import service.FileBrowser;
import def.DefaultDepartments;
import service.DepartmentService;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class DepartmentServiceImpl implements DepartmentService {
    private static final Logger LOGGER = Logger.getLogger(DepartmentService.class.getName());

    private static final String ERROR = "Помилка";

    private final DepartmentRepository repository;

    private ArrayList<String> departments;

    public DepartmentServiceImpl(){
        this.repository = new DepartmentRepositoryImpl();
    }

    public DepartmentServiceImpl(String dbUrl){
        this.repository = new DepartmentRepositoryImpl(dbUrl);
    }

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
    public void init(){
        LOGGER.info("DepartmentService: initialization start ...");
        this.departments = repository.getAll();
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
            this.repository.add(object);
        }
        return this.departments;
    }

    @Override
    public ArrayList<String> remove(String object) {
        if (this.departments.contains(object)){
            this.departments.remove(object);
            this.repository.remove(object);
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
                this.repository.set(oldObject, newObject);
            }
        }
        return this.departments;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.departments.size() ? null : this.departments.get(index);
    }

    @Override
    public void clear() {
        this.departments.clear();
        this.repository.clear();
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
        this.repository.rewrite(departments);
    }

    @Override
    public void resetToDefault() {
        this.departments = DefaultDepartments.get();
        this.repository.rewrite(this.departments);
    }

    private void showNotFoundMessage() {
        String message = "Цех з такою назвою не знайдено в списку цехів.";
        JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
