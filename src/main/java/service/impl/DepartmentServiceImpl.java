package service.impl;

import def.DefaultDepartments;
import repository.Repository;
import repository.impl.DepartmentRepositorySQLite;
import service.DepartmentService;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class DepartmentServiceImpl implements DepartmentService {
    private static DepartmentServiceImpl service;

    private final Repository<String> repository;

    private DepartmentServiceImpl(){
        repository = new DepartmentRepositorySQLite();
    }

    public DepartmentServiceImpl(@Nonnull Repository<String> repository){
        this.repository = repository;
        service = this;
    }

    public static DepartmentServiceImpl getInstance(){
        if (service == null) service = new DepartmentServiceImpl();

        return service;
    }

    @Override
    public Collection<String> getAll() {
        return repository.getAll();
    }

    @Override
    public String[] getAllInStrings(){
        return repository.getAll().toArray(new String[0]);
    }

    @Override
    public boolean add(@Nonnull String object) {
        return repository.add(object);
    }

    @Override
    public boolean add(@Nonnull Collection<String>objects){
        Set<String>old = new LinkedHashSet<>(repository.getAll());
        old.addAll(objects);
        return repository.rewrite(old);
    }

    @Override
    public boolean remove(@Nonnull String object) {
        return repository.remove(object);
    }

    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        if (oldObject.equals(newObject)) return true;
        return repository.set(oldObject, newObject);
    }

    @Override
    public boolean clear() {
        return repository.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<String>departments){
        return repository.rewrite(departments);
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultDepartments.get());
    }
}