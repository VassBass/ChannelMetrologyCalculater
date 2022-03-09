package service.impl;

import application.Application;
import def.DefaultDepartments;
import repository.DepartmentRepository;
import repository.impl.DepartmentRepositoryImpl;
import service.DepartmentService;

import javax.swing.*;
import java.util.ArrayList;
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

    @Override
    public void init(){
        LOGGER.fine("DepartmentService: initialization start ...");
        this.departments = repository.getAll();
        LOGGER.info("Initialization SUCCESS");
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
        if (object != null && !this.departments.contains(object)){
            this.departments.add(object);
            this.repository.add(object);
        }
        return this.departments;
    }

    @Override
    public ArrayList<String> remove(String object) {
        if (object != null) {
            if (this.departments.contains(object)) {
                this.departments.remove(object);
                this.repository.remove(object);
            } else {
                this.showNotFoundMessage();
            }
        }
        return this.departments;
    }

    @Override
    public ArrayList<String> set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null){
            int index = this.departments.indexOf(oldObject);
            if (index >= 0) {
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
    public void exportData(){
        this.repository.export(this.departments);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>departments){
        if (departments != null) {
            this.departments = departments;
            this.repository.rewrite(departments);
        }
    }

    @Override
    public void resetToDefault() {
        this.departments = DefaultDepartments.get();
        this.repository.rewrite(this.departments);
    }

    private void showNotFoundMessage() {
        if (Application.context != null) {
            String message = "Цех з такою назвою не знайдено в списку цехів.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
