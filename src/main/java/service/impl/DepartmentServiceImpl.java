package service.impl;

import def.DefaultDepartments;
import repository.Repository;
import repository.impl.DepartmentRepositorySQLite;
import service.DepartmentService;

import java.util.Collection;

public class DepartmentServiceImpl implements DepartmentService {
    private final Repository<String> repository;

    public DepartmentServiceImpl(){
        this.repository = new DepartmentRepositorySQLite();
    }

    public DepartmentServiceImpl(Repository<String> repository){
        this.repository = repository;
    }

    @Override
    public Collection<String> getAll() {
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
    public boolean rewrite(Collection<String>departments){
        return this.repository.rewrite(departments);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultDepartments.get());
    }
}