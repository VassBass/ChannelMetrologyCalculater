package service.impl;

import def.DefaultDepartments;
import repository.DepartmentRepository;
import repository.impl.DepartmentRepositoryImpl;
import service.DepartmentService;

import java.util.ArrayList;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;

    public DepartmentServiceImpl(){
        this.repository = new DepartmentRepositoryImpl();
    }

    public DepartmentServiceImpl(DepartmentRepository repository){
        this.repository = repository;
    }

    @Override
    public ArrayList<String> getAll() {
        return this.repository.getAll();
    }

    @Override
    public String[] getAllInStrings(){
        return this.repository.getAll().toArray(new String[0]);
    }

    @Override
    public boolean add(String object) {
        return this.repository.add(object);
    }

    @Override
    public boolean remove(String object) {
        return this.repository.remove(object);
    }

    @Override
    public boolean set(String oldObject, String newObject) {
        return this.repository.set(oldObject, newObject);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean rewrite(ArrayList<String>departments){
        return this.repository.rewrite(departments);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultDepartments.get());
    }
}