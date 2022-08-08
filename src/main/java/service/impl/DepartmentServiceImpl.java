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
    private final Set<String> mainSet;

    private DepartmentServiceImpl(){
        repository = new DepartmentRepositorySQLite();
        mainSet = new LinkedHashSet<>(repository.getAll());
    }

    public DepartmentServiceImpl(@Nonnull Repository<String> repository){
        this.repository = repository;
        mainSet = new LinkedHashSet<>(this.repository.getAll());
    }

    public static DepartmentServiceImpl getInstance(){
        if (service == null) service = new DepartmentServiceImpl();

        return service;
    }

    @Override
    public Collection<String> getAll() {
        return mainSet;
    }

    @Override
    public String[] getAllInStrings(){
        return mainSet.toArray(new String[0]);
    }

    @Override
    public boolean add(@Nonnull String object) {
        if (mainSet.add(object)) {
            return repository.add(object);
        } else return false;
    }

    @Override
    public boolean add(@Nonnull Collection<String>objects){
        if (mainSet.addAll(objects)){
            return repository.rewrite(mainSet);
        }else return false;
    }

    @Override
    public boolean remove(@Nonnull String object) {
        if (mainSet.remove(object)) {
            return repository.remove(object);
        } else return false;
    }

    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        if (oldObject.equals(newObject)) return true;

        ArrayList<String> list = new ArrayList<>(mainSet);
        int indexOfOld = list.indexOf(oldObject);
        int indexOfNew = list.indexOf(newObject);
        if (indexOfOld >= 0 && indexOfNew < 0){
            list.set(indexOfOld, newObject);
            mainSet.clear();
            mainSet.addAll(list);
            return true;
        }else return false;
    }

    @Override
    public boolean clear() {
        if (repository.clear()){
            mainSet.clear();
            return true;
        }else return false;
    }

    @Override
    public boolean rewrite(@Nonnull Collection<String>departments){
        if (repository.rewrite(departments)){
            mainSet.clear();
            return mainSet.addAll(departments);
        }else return false;
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultDepartments.get());
    }
}